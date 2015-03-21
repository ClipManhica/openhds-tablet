package org.openhds.mobile.clip.database;

import org.openhds.mobile.clip.model.PregnacyIdentification;

import android.database.Cursor;


public class Converter {
	
	public static PregnacyIdentification cursorToPregnacyIdentification(Cursor cursor){
		PregnacyIdentification pid = new PregnacyIdentification();
		
		pid.setId(cursor.getInt(cursor.getColumnIndex(Database.PregnancyId.COLUMN_ID)));
		pid.setIndividualId(cursor.getString(cursor.getColumnIndex(Database.PregnancyId.COLUMN_INDIVIDUAL_ID)));
		pid.setPermId(cursor.getString(cursor.getColumnIndex(Database.PregnancyId.COLUMN_PERM_ID)));
		pid.setPregnancyId(cursor.getString(cursor.getColumnIndex(Database.PregnancyId.COLUMN_PREGNANCY_ID)));
		pid.setCount(cursor.getInt((cursor.getColumnIndex(Database.PregnancyId.COLUMN_COUNT))));
		
		return pid;
	}
		
}
