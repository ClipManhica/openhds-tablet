package org.openhds.mobile.clip.model;

import java.io.Serializable;

import org.openhds.mobile.clip.database.Database;

import android.content.ContentValues;

public class PregnacyIdentification implements Serializable, org.openhds.mobile.clip.database.Table {

	private static final long serialVersionUID = -8973040054481039467L;
	private int id;
	private String individualId;
	private String permId;
	private String pregnancyId;	
	private int count;
			
	public PregnacyIdentification(String individualId, String permId, String pregnancyId, int count) {	
		this.individualId = individualId;
		this.permId = permId;
		this.pregnancyId = pregnancyId;
		this.count = count;
	}
	
	public PregnacyIdentification() {
		// TODO Auto-generated constructor stub
	}

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getPermId() {
		return permId;
	}

	public void setPermId(String permId) {
		this.permId = permId;
	}

	public String getPregnancyId() {
		return pregnancyId;
	}

	public void setPregnancyId(String pregnancyId) {
		this.pregnancyId = pregnancyId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}		
	
	public int getId(){
		return 0;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getTableName() {
		return Database.PregnancyId.TABLE_NAME;
	}
	
	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
				
		cv.put(Database.PregnancyId.COLUMN_INDIVIDUAL_ID, individualId);
		cv.put(Database.PregnancyId.COLUMN_PERM_ID, permId);
		cv.put(Database.PregnancyId.COLUMN_PREGNANCY_ID, pregnancyId);
		cv.put(Database.PregnancyId.COLUMN_COUNT, count);
		
		return cv;
	}
	
	public String[] getColumnNames() {		
		return Database.PregnancyId.ALL_COLUMNS;
	}	
	
}
