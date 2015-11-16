package org.openhds.mobile.clip.database;

import org.openhds.mobile.clip.model.PregnacyIdentification;
import org.openhds.mobile.clip.model.PregnancyControl;

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
	
	public static PregnancyControl cursorToPregnancyControl(Cursor cursor){
		PregnancyControl pid = new PregnancyControl();
		
		pid.setId(cursor.getInt(cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_ID)));
		pid.setIndividualId(cursor.getString(cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_INDIVIDUAL_ID)));
		pid.setPermId(cursor.getString(cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_PERM_ID)));
		pid.setPregnancyId(cursor.getString(cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_PREGNANCY_ID)));
		pid.setDateOfBirth(cursor.getString((cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_DATE_OF_BIRTH))));
		pid.setEstimatedDob(cursor.getString((cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_ESTIMATED_DOB))));
		pid.setHasDelivered(cursor.getInt((cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_HAS_DELIVERED))));
		pid.setVisitNumber(cursor.getInt((cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_VISIT_NUMBER))));
		pid.setAntepartumVisits(cursor.getInt((cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_ANTEPARTUM_VISITS))));
		pid.setPostpartumVisits(cursor.getInt((cursor.getColumnIndex(Database.PregnancyControlTable.COLUMN_POSTPARTUM_VISITS))));
				
		return pid;
	}
		
}
