package org.openhds.mobile.clip.adapter;

import org.openhds.mobile.OpenHDS;
import org.openhds.mobile.R;
import org.openhds.mobile.database.queries.Converter;
import org.openhds.mobile.database.queries.Queries;
import org.openhds.mobile.model.SocialGroup;

import com.google.android.gms.internal.ex;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends SimpleCursorAdapter {

	private Context mContext;	  
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
	
	public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		
		this.mContext = context;
		this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {	
		 return inflater.inflate(layout, null);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {		
		super.bindView(view, context, cursor);
	
		LinearLayout linear = (LinearLayout) view;
				
		if (linear.getChildCount()==2){
			
			LinearLayout textLayout = (LinearLayout) linear.getChildAt(1);
			
			//view.get
			//Log.d("view", textLayout+"");			
			
			TextView text1 = (TextView) textLayout.getChildAt(0);
	        TextView text2 = (TextView) textLayout.getChildAt(1);
	        TextView text3 = (TextView) textLayout.getChildAt(2);
	        
	        int indexOfEndType = cursor.getColumnIndex(OpenHDS.Individuals.COLUMN_RESIDENCE_END_TYPE);
	        
	        
	        if (indexOfEndType != -1){ //Is displaying Individuals
	        	String endType = cursor.getString(indexOfEndType);
	        	
	        	/*
	        	String extId =  cursor.getString(cursor.getColumnIndex(OpenHDS.Individuals.COLUMN_INDIVIDUAL_EXTID));
	        	boolean isHead = false;
	        	
	        	SocialGroup sg = null;
	        	Cursor crs = Queries.getSocialGroupsByIndividualExtId(mContext.getContentResolver(),extId);
	        	if (crs.moveToFirst()){
	        		sg = Converter.convertToSocialGroup(crs);
	        		isHead = extId.equals(sg.getGroupHead());
	        	}
	        	
	        	if (isHead){
	        		text2.setText(text2.getText()+" <HEAD>");
	        	}
	        	*/
	        	
	        	if (endType.equals("DTH")){
	        		text1.setTextColor(Color.GREEN);
	        		text2.setTextColor(Color.GREEN);
	        		text3.setTextColor(Color.GREEN);
	        		text2.setText(text2.getText()+" <DTH>");
	        	}
	        }
	        
	      
		}
		
	}
	
}
