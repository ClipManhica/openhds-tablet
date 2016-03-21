package org.openhds.mobile.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.openhds.mobile.OpenHDS;
import org.openhds.mobile.R;
import org.openhds.mobile.clip.database.Database.PregnancyControlTable;
import org.openhds.mobile.clip.model.PregnacyIdentification;
import org.openhds.mobile.clip.model.PregnancyControl;
import org.openhds.mobile.listener.SyncDatabaseListener;
import org.openhds.mobile.model.Settings;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
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
			builder.append(" " + mContext.getString(R.string.sync_task_saved)  + " " + values[0]  + " " +  mContext.getString(R.string.sync_task_items));
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
			processUrl("http://sap.manhica.net:4700/files/clip/pregnancy_control.xml"); //changing to control
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

	private void processUrl(String url) throws Exception {
		state = State.DOWNLOADING;
		publishProgress();

		httpGet = new HttpGet(url);
		processResponse();
	}

	private void processResponse() throws Exception {
		InputStream inputStream = getResponse();
		if (inputStream != null)
			processXMLDocument(inputStream);
	}

	private InputStream getResponse() throws AuthenticationException,
			ClientProtocolException, IOException {
		HttpResponse response = null;

		httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet));
		httpGet.addHeader("content-type", "application/xml");
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
				} else if(name.equalsIgnoreCase("pregnaciesIdenfications")) {
					processPregnancyIdsParams(parser);
				} else if(name.equalsIgnoreCase("pregnanciesControl")) {
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
	
	private void processPregnancyIdsParams(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		org.openhds.mobile.clip.database.Database database = new org.openhds.mobile.clip.database.Database(mContext);
        database.open();
		
        values.clear();
		
		int count = 0;
		List<PregnacyIdentification> valuesTb = new ArrayList<PregnacyIdentification>();
		
        
		parser.nextTag();
		
		while (notEndOfXmlDoc("pregnaciesIdenfications", parser)) {
			count++;
						
			PregnacyIdentification pregId = new PregnacyIdentification();
			
			parser.nextTag(); //individualId
			pregId.setIndividualId(parser.nextText());
			
			//Log.d("individualId", parser.nextText());
			
			parser.nextTag(); //permId						
			pregId.setPermId(parser.nextText());
			
			//Log.d("permId", parser.nextText());
			
			parser.nextTag(); //pregnancyId
			pregId.setPregnancyId(parser.nextText());
			
			//Log.d("pregnancyId", parser.nextText());
			
			parser.nextTag(); //count
			pregId.setCount(Integer.parseInt(parser.nextText()));
			
			//Log.d("count", parser.nextText());
						
			valuesTb.add(pregId);
			
			publishProgress(count);

			parser.nextTag(); // </pregnancyIdentification>
			parser.nextTag(); // <pregnancyIdentification>			
			
		}
		
		state = State.SAVING;
		entity = Entity.PREGNANCY_IDS;
		
		if (!valuesTb.isEmpty()) {
			count = 0;
			for (PregnacyIdentification p : valuesTb){
				count++;
				database.insert(p);
				publishProgress(count);
			}
		}
		
		database.close();
	}		
	
	private void processPregnancyControlParams(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		org.openhds.mobile.clip.database.Database database = new org.openhds.mobile.clip.database.Database(mContext);
        database.open();
		
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
						
			valuesTb.add(pregnancyControl);
			
			publishProgress(count);

			parser.nextTag(); // </pregnancyIdentification>
			parser.nextTag(); // <pregnancyIdentification>			
			
		}
		
		state = State.SAVING;
		entity = Entity.PREGNANCY_IDS;
		
		if (!valuesTb.isEmpty()) {
			count = 0;
			for (PregnancyControl p : valuesTb){
				count++;
				database.insert(p);
				publishProgress(count);
			}
		}
		
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
