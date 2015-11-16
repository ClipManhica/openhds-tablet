package org.openhds.mobile.clip.model;

import java.io.Serializable;

import org.openhds.mobile.clip.database.Database;

import android.content.ContentValues;

public class PregnancyControl implements Serializable, org.openhds.mobile.clip.database.Table {

	private static final long serialVersionUID = -8973040054481039447L;
	private int id;
	private String individualId;
	private String permId;
	private String pregnancyId;	
	private String estimatedDob;
	private String dateOfBirth;
	private int hasDelivered;
	private int visitNumber;
	private int antepartumVisits;
	private int postpartumVisits;
		
		
	public PregnancyControl(String individualId, String permId, String pregnancyId, String estimatedDob, String dateOfBirth, int hasDelivered, int visitNumber, int antepartumVisits, int postpartumVisits) {
		super();
		this.individualId = individualId;
		this.permId = permId;
		this.pregnancyId = pregnancyId;
		this.estimatedDob = estimatedDob;
		this.dateOfBirth = dateOfBirth;
		this.hasDelivered = hasDelivered;
		this.visitNumber = visitNumber;
		this.antepartumVisits = antepartumVisits;
		this.postpartumVisits = postpartumVisits;
	}

	public PregnancyControl() {
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getEstimatedDob() {
		return estimatedDob;
	}

	public void setEstimatedDob(String estimatedDob) {
		this.estimatedDob = estimatedDob;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int hasDelivered() {
		return hasDelivered;
	}

	public void setHasDelivered(int hasDelivered) {
		this.hasDelivered = hasDelivered;
	}

	public int getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(int visitNumber) {
		this.visitNumber = visitNumber;
	}

	public int getAntepartumVisits() {
		return antepartumVisits;
	}

	public void setAntepartumVisits(int antepartumVisits) {
		this.antepartumVisits = antepartumVisits;
	}

	public int getPostpartumVisits() {
		return postpartumVisits;
	}

	public void setPostpartumVisits(int postpartumVisits) {
		this.postpartumVisits = postpartumVisits;
	}

	public String getTableName() {
		return Database.PregnancyControlTable.TABLE_NAME;
	}
	
	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
				
		cv.put(Database.PregnancyControlTable.COLUMN_INDIVIDUAL_ID, individualId);
		cv.put(Database.PregnancyControlTable.COLUMN_PERM_ID, permId);
		cv.put(Database.PregnancyControlTable.COLUMN_PREGNANCY_ID, pregnancyId);
		cv.put(Database.PregnancyControlTable.COLUMN_ESTIMATED_DOB, estimatedDob);
		cv.put(Database.PregnancyControlTable.COLUMN_DATE_OF_BIRTH, dateOfBirth);
		cv.put(Database.PregnancyControlTable.COLUMN_HAS_DELIVERED, hasDelivered);
		cv.put(Database.PregnancyControlTable.COLUMN_VISIT_NUMBER, visitNumber);
		cv.put(Database.PregnancyControlTable.COLUMN_ANTEPARTUM_VISITS, antepartumVisits);
		cv.put(Database.PregnancyControlTable.COLUMN_POSTPARTUM_VISITS, postpartumVisits);
		
		return cv;
	}
	
	public String[] getColumnNames() {		
		return Database.PregnancyControlTable.ALL_COLUMNS;
	}	
	
}
