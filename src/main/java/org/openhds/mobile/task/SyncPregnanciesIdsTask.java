package org.openhds.mobile.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.openhds.mobile.R;
import org.openhds.mobile.clip.model.PregnacyIdentification;
import org.openhds.mobile.clip.model.PregnancyControl;
import org.openhds.mobile.listener.SyncDatabaseListener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * AsyncTask responsible for downloading the OpenHDS "database", that is a
 * subset of the OpenHDS database records. It does the downloading
 * incrementally, by downloading parts of the data one at a time. For example,
 * it gets all locations and then retrieves all individuals. Ordering is
 * somewhat important here, because the database has a few foreign key
 * references that must be satisfied (e.g. individual references a location
 * location)
 */
public class SyncPregnanciesIdsTask extends
		AsyncTask<Void, Integer, HttpTask.EndResult> {

	private static final String API_PATH = "/api/rest";

	private SyncDatabaseListener listener;
	private ContentResolver resolver;

	private UsernamePasswordCredentials creds;
	private ProgressDialog dialog;
	private HttpGet httpGet;
	private HttpClient client;

	private String baseurl;
	private String username;
	private String password;

	String lastExtId;

	private final List<ContentValues> values = new ArrayList<ContentValues>();
	private final ContentValues[] emptyArray = new ContentValues[] {};

	private State state;
	private Entity entity;

	private boolean isDownloadingZipFile;
	
	private enum State {
		DOWNLOADING, SAVING
	}

	private enum Entity {
		LOCATION_HIERARCHY, LOCATION, ROUND, VISIT, RELATIONSHIP, INDIVIDUAL, SOCIALGROUP, LOCATION_HIERARCHY_LEVELS, SETTINGS, PREGNANCY_IDS
	}

	private Context mContext;
	
	public SyncPregnanciesIdsTask(String url, String username, String password,
			ProgressDialog dialog, Context context,
			SyncDatabaseListener listener) {
		this.baseurl = url;
		this.username = username;
		this.password = password;
		this.dialog = dialog;
		this.listener = listener;
		this.resolver = context.getContentResolver();
		this.mContext = context;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		StringBuilder builder = new StringBuilder();
		switch (state) {
		case DOWNLOADING:
			builder.append(mContext.getString(R.string.sync_task_downloading) + " ");
			break;
		case SAVING:
			builder.append(mContext.getString(R.string.sync_task_saving) + " ");
			break;
		}

		switch (entity) {
		case PREGNANCY_IDS:
			builder.append(mContext.getString(R.string.sync_task_clip_pregnancy_ids));
			break;			
		}

		if (values.length > 0) {
			String msg = " " + mContext.getString(R.string.sync_task_saved)  + " " + values[0]  + " " +  mContext.getString(R.string.sync_task_items);
			if (state == State.DOWNLOADING && isDownloadingZipFile){
				msg = " " + mContext.getString(R.string.sync_task_saved)  + " " + values[0]  + "KB";
			}
			builder.append(msg);
		}

		dialog.setMessage(builder.toString());
	}

	@Override
	protected HttpTask.EndResult doInBackground(Void... params) {
		creds = new UsernamePasswordCredentials(username, password);

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
		HttpConnectionParams.setSoTimeout(httpParameters, 90000);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8192);
		client = new DefaultHttpClient(httpParameters);

		// at this point, we don't care to be smart about which data to
		// download, we simply download it all
		deleteAllTables();
		//deleteAllClipDatabase();
		try {	
			
			entity = Entity.PREGNANCY_IDS;
			//processUrl("http://sap.manhica.net:4700/files/clip/pregnancy_control.xml"); //changing to control
			processUrl("http://sap.manhica.net:4780/manhica-dbsync/api/clip-explorer/pregnancy_control/zip"); //changing to control
		} catch (Exception e) {
			e.printStackTrace();
			return HttpTask.EndResult.FAILURE;
		}

		return HttpTask.EndResult.SUCCESS;
	}

	private void deleteAllTables() {
		// ordering is somewhat important during delete. a few tables have
		// foreign keys	
		deleteAllClipDatabase();
	}
	
	private String getAppStoragePath(){
		File root = Environment.getExternalStorageDirectory();
		String destinationPath = root.getAbsolutePath() + File.separator
				+ "Android" + File.separator + "data" + File.separator
				+ "org.openhds.mobile" + File.separator + "files" + File.separator + "downloads" + File.separator;

		File baseDir = new File(destinationPath);
		if (!baseDir.exists()) {
			boolean created = baseDir.mkdirs();
			if (!created) {
				return destinationPath;
			}
		}
		
		return destinationPath;
	}
	
	private InputStream saveFileToStorage(InputStream inputStream) throws Exception {

		String path = getAppStoragePath() + "pregcontrol.zip";
		FileOutputStream fout = new FileOutputStream(path);
		byte[] buffer = new byte[10*1024];
		int len = 0;
		long total = 0;

		publishProgress();

		while ((len = inputStream.read(buffer)) != -1){
			fout.write(buffer, 0, len);
			total += len;
			int perc =  (int) ((total/(1024)));
			publishProgress(perc);
		}

		fout.close();
		inputStream.close();

		FileInputStream fin = new FileInputStream(path);

		return fin;
	}
	
	private void processZIPDocument(InputStream inputStream) throws Exception {

		Log.d("zip", "processing zip file");


		ZipInputStream zin = new ZipInputStream(inputStream);
		ZipEntry entry = zin.getNextEntry();

		Log.d("zip-entry", ""+entry);
		
		if (entry != null){
			processXMLDocument(zin);
			zin.closeEntry();
		}

		zin.close();
	}

	private void processUrl(String url) throws Exception {
		state = State.DOWNLOADING;
		publishProgress();

		this.isDownloadingZipFile = url.endsWith("zip");
		
		httpGet = new HttpGet(url);
		processResponse();
	}

	private void processResponse() throws Exception {
		InputStream inputStream = getResponse();
		
		if (this.isDownloadingZipFile){
			InputStream zipInputStream = saveFileToStorage(inputStream);
			if (zipInputStream != null){
				Log.d("download", "zip = "+zipInputStream);
				processZIPDocument(zipInputStream);
				zipInputStream.close();
			}
				
		}else{
			if (inputStream != null)
				processXMLDocument(inputStream);
		}
	}

	private InputStream getResponse() throws AuthenticationException, ClientProtocolException, IOException {
		HttpResponse response = null;

		//httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet));
		//httpGet.addHeader("content-type", "application/xml");
		response = client.execute(httpGet);
		
		//Handle 404
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND){
			throw new RuntimeException("404 Not found.");
		}		

		HttpEntity entity = response.getEntity();
		return entity.getContent();
	}

	private void processXMLDocument(InputStream content) throws Exception {
		state = State.SAVING;

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);

		XmlPullParser parser = factory.newPullParser();
		parser.setInput(new InputStreamReader(content));

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT && !isCancelled()) {
			String name = null;

			switch (eventType) {
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("count")) {
					parser.next();
					int cnt = Integer.parseInt(parser.getText());
					publishProgress(cnt);
					parser.nextTag();
				} else if(name.equalsIgnoreCase("pregnanciesControl")) {
					Log.d("zip-xmladasd", "asd");
					processPregnancyControlParams(parser);
				}
				break;
			}
			eventType = parser.next();
		}
	}
				
	private boolean notEndOfXmlDoc(String element, XmlPullParser parser)
			throws XmlPullParserException {
		return !element.equals(parser.getName())
				&& parser.getEventType() != XmlPullParser.END_TAG
				&& !isCancelled();
	}
	
	protected void onPostExecute(HttpTask.EndResult result) {
		listener.collectionComplete(result);
	}

	private void processPregnancyControlParams(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		state = State.SAVING;
		entity = Entity.PREGNANCY_IDS;
		
		org.openhds.mobile.clip.database.Database database = new org.openhds.mobile.clip.database.Database(mContext);
        database.open();
        database.beginTransaction();
		
        values.clear();
		
		int count = 0;
		List<PregnancyControl> valuesTb = new ArrayList<PregnancyControl>();
		
		Log.d("executing", "pregnancy control xml");
        
		parser.nextTag();
		
		while (notEndOfXmlDoc("pregnanciesControl", parser)) {
			count++;
						
			PregnancyControl pregnancyControl = new PregnancyControl();
			
			parser.nextTag(); //individualId
			pregnancyControl.setIndividualId(parser.nextText());
			
			//Log.d("individualId", parser.nextText());
			
			parser.nextTag(); //permId						
			pregnancyControl.setPermId(parser.nextText());
			
			//Log.d("permId", parser.nextText());
			
			parser.nextTag(); //pregnancyId
			pregnancyControl.setPregnancyId(parser.nextText());
			
			//Log.d("pregnancyId", parser.nextText());
			
			parser.nextTag(); //hasDelivered
			pregnancyControl.setHasDelivered(parser.nextText().equalsIgnoreCase("true")? 1 : 0);
			
			//Log.d("hasDelivered", parser.nextText());
			
			parser.nextTag(); //visitNumber
			pregnancyControl.setHasDelivered(Integer.parseInt(parser.nextText()));
			
			//Log.d("visitNumber", parser.nextText());
			
			parser.nextTag(); //antepartumVisits
			pregnancyControl.setAntepartumVisits(Integer.parseInt(parser.nextText()));
			
			//Log.d("antepartumVisits", parser.nextText());
			
			parser.nextTag(); //postpartumVisits
			pregnancyControl.setPostpartumVisits(Integer.parseInt(parser.nextText()));
			
			//Log.d("postpartumVisits", parser.nextText());
			
			parser.nextTag(); //dob
			pregnancyControl.setDateOfBirth(parser.nextText());
			
			//Log.d("dob", parser.nextText());
			
			parser.nextTag(); //estDob
			pregnancyControl.setEstimatedDob(parser.nextText());			
			//Log.d("estDob", parser.nextText());
			
			parser.nextTag(); //nrBabies
			pregnancyControl.setNrBabies(Integer.parseInt(parser.nextText()));			
			//Log.d("nrBabies", parser.nextText());
			
			parser.nextTag(); //baby1
			pregnancyControl.setBaby1(parser.nextText());			
			//Log.d("baby1", parser.nextText());
			
			parser.nextTag(); //baby2
			pregnancyControl.setBaby2(parser.nextText());			
			//Log.d("baby2", parser.nextText());
			
			parser.nextTag(); //baby3
			pregnancyControl.setBaby3(parser.nextText());			
			//Log.d("baby3", parser.nextText());
			
			parser.nextTag(); //baby4
			pregnancyControl.setBaby4(parser.nextText());			
			//Log.d("baby4", parser.nextText());
			
			parser.nextTag(); //baby5
			pregnancyControl.setBaby5(parser.nextText());			
			//Log.d("baby5", parser.nextText());
			
			parser.nextTag(); //baby6
			pregnancyControl.setBaby6(parser.nextText());			
			//Log.d("baby6", parser.nextText());
			
			parser.nextTag(); //baby7
			pregnancyControl.setBaby7(parser.nextText());			
			//Log.d("baby7", parser.nextText());
			
			parser.nextTag(); //baby8
			pregnancyControl.setBaby8(parser.nextText());			
			//Log.d("baby8", parser.nextText());
			
			parser.nextTag(); //baby9
			pregnancyControl.setBaby9(parser.nextText());			
			//Log.d("baby9", parser.nextText());
			
			//valuesTb.add(pregnancyControl);
						
			database.insert(pregnancyControl);
			
			if (count % 100 == 0) {
				//persistLocations();
				//values.clear();
				publishProgress(count);
			}
			
			parser.nextTag(); // </pregnancyIdentification>
			parser.nextTag(); // <pregnancyIdentification>			
			
		}
						
		/*
		if (!valuesTb.isEmpty()) {
			count = 0;
			for (PregnancyControl p : valuesTb){
				count++;
				database.insert(p);
				publishProgress(count);
			}
		}
		*/
		
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}		
		
	private void deleteAllClipDatabase(){
		org.openhds.mobile.clip.database.Database database = new org.openhds.mobile.clip.database.Database(mContext);
        database.open();
        
        database.delete(PregnacyIdentification.class, null, null);
        database.delete(PregnancyControl.class, null, null);
        
        database.close();
	}

}
