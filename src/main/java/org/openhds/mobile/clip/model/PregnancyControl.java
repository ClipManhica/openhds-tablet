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
	private int nrBabies;
	private String baby1;
	private String baby2;
	private String baby3;
	private String baby4;
	private String baby5;
	private String baby6;
	private String baby7;
	private String baby8;
	private String baby9;
		
		
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
		
	public int getNrBabies() {
		return nrBabies;
	}

	public void setNrBabies(int nrBabies) {
		this.nrBabies = nrBabies;
	}

	public String getBaby1() {
		return baby1;
	}

	public void setBaby1(String baby1) {
		this.baby1 = baby1;
	}

	public String getBaby2() {
		return baby2;
	}

	public void setBaby2(String baby2) {
		this.baby2 = baby2;
	}

	public String getBaby3() {
		return baby3;
	}

	public void setBaby3(String baby3) {
		this.baby3 = baby3;
	}

	public String getBaby4() {
		return baby4;
	}

	public void setBaby4(String baby4) {
		this.baby4 = baby4;
	}

	public String getBaby5() {
		return baby5;
	}

	public void setBaby5(String baby5) {
		this.baby5 = baby5;
	}

	public String getBaby6() {
		return baby6;
	}

	public void setBaby6(String baby6) {
		this.baby6 = baby6;
	}

	public String getBaby7() {
		return baby7;
	}

	public void setBaby7(String baby7) {
		this.baby7 = baby7;
	}

	public String getBaby8() {
		return baby8;
	}

	public void setBaby8(String baby8) {
		this.baby8 = baby8;
	}

	public String getBaby9() {
		return baby9;
	}

	public void setBaby9(String baby9) {
		this.baby9 = baby9;
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
		cv.put(Database.PregnancyControlTable.COLUMN_NR_BABIES, nrBabies);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_1, baby1);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_2, baby2);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_3, baby3);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_4, baby4);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_5, baby5);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_6, baby6);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_7, baby7);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_8, baby8);
		cv.put(Database.PregnancyControlTable.COLUMN_BABY_9, baby9);
		
		return cv;
	}
	
	public String[] getColumnNames() {		
		return Database.PregnancyControlTable.ALL_COLUMNS;
	}	
	
}
