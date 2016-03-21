package org.openhds.mobile.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openhds.mobile.OpenHDS;
import org.openhds.mobile.clip.database.Database.PregnancyControlTable;
import org.openhds.mobile.clip.model.PregnancyControl;
import org.openhds.mobile.database.queries.Converter;
import org.openhds.mobile.database.queries.Queries;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * A LocationVisit represents a single visit to a specific location. This class
 * is built incrementally, meaning first the user constructs the location
 * hierarchy selection (region, sub-region, village, and round), one item at a
 * time. Then they must either set or create a location. After a location is
 * set, they can proceed to create a visit. Once a visit is created, the
 * LocationVisit cannot be changed, that is, a visit is considered started. Only
 * new events can be added to the location visit at this point. Once the user
 * completes a visit, a new LocationVisit is returned that contains pre-filled
 * location hierarchy selection (region, sub-region, village and round) assuming
 * the field worker will work in the same area.
 */
public class LocationVisit implements Serializable {

    private static final long serialVersionUID = -36602612353821830L;

    private FieldWorker fieldWorker;
    private LocationHierarchy hierarchy1;
    private LocationHierarchy hierarchy2;
    private LocationHierarchy hierarchy3;
    private LocationHierarchy hierarchy4;
    private LocationHierarchy hierarchy5;
    private LocationHierarchy hierarchy6;
    private LocationHierarchy hierarchy7;
    private LocationHierarchy hierarchy8;
   // private int levelNumbers;
    private String lowestLevelExtId;
    private String lowestLevelName;
    private Round round;

    private Location location;
    private Visit visit;

    private Individual selectedIndividual;

    public LocationVisit completeVisit() {
        LocationVisit visit = new LocationVisit();
        visit.fieldWorker = fieldWorker;
        visit.hierarchy1 = hierarchy1;
        visit.hierarchy2 = hierarchy2;
        visit.hierarchy3 = hierarchy3;
        visit.hierarchy4 = hierarchy4;
        visit.hierarchy5 = hierarchy5;
        visit.hierarchy6 = hierarchy6;
        visit.hierarchy7 = hierarchy7;
        visit.hierarchy8 = hierarchy8;
        visit.round = round;

        return visit;
    }

    public FieldWorker getFieldWorker() {
        return fieldWorker;
    }

    public void setFieldWorker(FieldWorker fieldWorker) {
        this.fieldWorker = fieldWorker;
    }

    public LocationHierarchy getHierarchy1() {
        return hierarchy1;
    }

    public LocationHierarchy getHierarchy2() {
        return hierarchy2;
    }
    
    public LocationHierarchy getHierarchy3() {
        return hierarchy3;
    }

    public LocationHierarchy getHierarchy4() {
        return hierarchy4;
    }

    public LocationHierarchy getHierarchy5() {
		return hierarchy5;
	}

	public LocationHierarchy getHierarchy6() {
		return hierarchy6;
	}

	public LocationHierarchy getHierarchy7() {
		return hierarchy7;
	}

	public LocationHierarchy getHierarchy8() {
		return hierarchy8;
	}

	public Round getRound() {
        return round;
    }

    public Individual getSelectedIndividual() {
        return selectedIndividual;
    }

    public void setSelectedIndividual(Individual selectedIndividual) {
        this.selectedIndividual = selectedIndividual;
    }

    public void setHierarchy1(LocationHierarchy region) {
        this.hierarchy1 = region;
    	this.lowestLevelExtId = hierarchy1.getExtId();
    	this.lowestLevelName = hierarchy1.getName();
        clearLevelsBelow(1);
    }
    
    public void setHierarchy2(LocationHierarchy subRegion) {
        this.hierarchy2 = subRegion;
    	this.lowestLevelExtId = hierarchy2.getExtId();
    	this.lowestLevelName = hierarchy2.getName();
        clearLevelsBelow(2);
    }
    
    public void setHierarchy3(LocationHierarchy hierarchy3) {
        this.hierarchy3 = hierarchy3;
    	this.lowestLevelExtId = hierarchy3.getExtId();
    	this.lowestLevelName = hierarchy3.getName();
        clearLevelsBelow(3);
    }

    public void setHierarchy4(LocationHierarchy village) {
        this.hierarchy4 = village;
    	this.lowestLevelExtId = hierarchy4.getExtId();
    	this.lowestLevelName = hierarchy4.getName();
        clearLevelsBelow(4);
    }
    
    public void setHierarchy5(LocationHierarchy hierarchy5) {
        this.hierarchy5 = hierarchy5;
    	this.lowestLevelExtId = hierarchy5.getExtId();
    	this.lowestLevelName = hierarchy5.getName();
        clearLevelsBelow(5);
    }
    
    public void setHierarchy6(LocationHierarchy hierarchy6) {
        this.hierarchy6 = hierarchy6;
    	this.lowestLevelExtId = hierarchy6.getExtId();
    	this.lowestLevelName = hierarchy6.getName();
        clearLevelsBelow(6);
    }
    
    public void setHierarchy7(LocationHierarchy hierarchy7) {
        this.hierarchy7 = hierarchy7;
    	this.lowestLevelExtId = hierarchy7.getExtId();
    	this.lowestLevelName = hierarchy7.getName();
        clearLevelsBelow(7);
    }

    public void setHierarchy8(LocationHierarchy hierarchy8) {
        this.hierarchy8 = hierarchy8;
    	this.lowestLevelExtId = hierarchy8.getExtId();
    	this.lowestLevelName = hierarchy8.getName();
        clearLevelsBelow(8);
    }
    
    public void clearLevelsBelow(int i) {
        switch (i) {
        case 0:
            hierarchy1 = null;
        case 1:
            hierarchy2 = null;
        case 2:
            hierarchy3 = null;
        case 3:
            hierarchy4 = null;
        case 4:
        	hierarchy5 = null;
        case 5:
        	hierarchy6 = null;
        case 6:
        	hierarchy7 = null;
        case 7:
        	hierarchy8 = null;
        case 8:
        	round = null;
        case 9:
        	location = null;
        case 10:
        	selectedIndividual = null;
        }
    }

   

    public void setRound(Round round) {
        this.round = round;
        clearLevelsBelow(9);
    }
    
    public void setLocation(Location location) {
        this.location = location;
        clearLevelsBelow(10);
    }    

    public Location getLocation() {
        return location;
    }

    public int getLevelOfHierarchySelected() {
        if (hierarchy1 == null) {
            return 0;
        }

        if (hierarchy2 == null) {
            return 1;
        }
        
        if (hierarchy3 == null) {
            return 2;
        }

        if (hierarchy4 == null) {
            return 3;
        }

        if (hierarchy5 == null) {
            return 4;
        }

        if (hierarchy6 == null) {
            return 5;
        }
        
        if (hierarchy7 == null) {
            return 6;
        }

        if (hierarchy8 == null) {
            return 7;
        }
        
        if (round == null) {
            return 8;
        }

        return 9;
    }

    public void createLocation(ContentResolver resolver) {
    	if (lowestLevelExtId==null) {
    		setLatestLevelExtId(resolver);
    		setLatestLevelName(resolver);
    	}
        String locationId = generateLocationId(resolver);
        String locationName = generateLocationName(resolver);
        
        location = new Location();
        location.setExtId(locationId);
        location.setHierarchy(lowestLevelExtId);
        location.setName(locationName);      
    }
    
    //Generate the locationName to handle the HouseNo Code (##-####-###) from Manhiça DSS
 	private String generateLocationName(ContentResolver resolver) { 		 		 
 		
 		 String cluster = (isClip() ? getClusterId(getLatestLevelName())+"-" : "" );
 		
         Cursor cursor = resolver.query(OpenHDS.Locations.CONTENT_ID_URI_BASE,
                 new String[] { OpenHDS.Locations.COLUMN_LOCATION_NAME }, OpenHDS.Locations.COLUMN_LOCATION_NAME
                         + " LIKE ?", new String[] { cluster + getLatestLevelName() + "%" }, OpenHDS.Locations.COLUMN_LOCATION_NAME
                         + " ASC");

         String generatedName = null;
         String baseName = cluster + getLatestLevelName(); 
         
         List<String> allLocations = new ArrayList<String>();
         
         while(cursor.moveToNext()){
        	 allLocations.add(cursor.getString(0));
         }
         
         //001-999
         //NEXT       
         if (allLocations.size() > 0) {
         	
         	int nextCode = 0;        	       	        	
         	        	
         	for (int i=1; i<=999; i++){
         		
         		try{
         			String locName = String.format(baseName + "-" + "%03d", i);
         			
         			//Log.d("possible-locname", ""+locName);         			
         			if (allLocations.contains(locName)){
         				continue;
         			}else{
         				nextCode = i;
         				break;
         			}
         			
         		}catch (Exception e){        			
         			e.printStackTrace();         			
         			break;
         		}        		
         	}
         	
         	if (nextCode==0){
         		generatedName = "COULDNT GENERATE";
         	}else{
         		generatedName = String.format(getLatestLevelName() + "-" + "%03d", nextCode);
         	}        	
             
         } else {
             generatedName = getLatestLevelName() + "-001";
         }

         allLocations.clear();
         cursor.close();
         return cluster + generatedName;
     }

    private void setLatestLevelExtId (ContentResolver resolver) {
        Cursor curs = Queries.getAllHierarchyLevels(resolver);
        List<LocationHierarchyLevel> lhll = Converter.toLocationHierarchyLevelList(curs); 
        curs.close();
        
        int levelNumbers = lhll.size() -1;
        
        if (levelNumbers==1) {
        	this.lowestLevelExtId = hierarchy1.getExtId();
        }  else if (levelNumbers==2) {
        	this.lowestLevelExtId = hierarchy2.getExtId();
        }  else if (levelNumbers==3) {
        	this.lowestLevelExtId = hierarchy3.getExtId();
        }  else if (levelNumbers==4) {
        	this.lowestLevelExtId = hierarchy4.getExtId();
        }  else if (levelNumbers==5) {
        	this.lowestLevelExtId = hierarchy5.getExtId();
        }  else if (levelNumbers==6) {
        	this.lowestLevelExtId = hierarchy6.getExtId();
        }  else if (levelNumbers==8) {
        	this.lowestLevelExtId = hierarchy7.getExtId();
        }  else if (levelNumbers==9) {
        	this.lowestLevelExtId = hierarchy8.getExtId();
        }
        location.setHierarchy(lowestLevelExtId);
    }
    
    public String getLatestLevelExtId () {
    	return lowestLevelExtId;
    }
   
    private void setLatestLevelName(ContentResolver resolver) {
        Cursor curs = Queries.getAllHierarchyLevels(resolver);
        List<LocationHierarchyLevel> lhll = Converter.toLocationHierarchyLevelList(curs); 
        curs.close();
        
        int levelNumbers = lhll.size() -1;
        
        if (levelNumbers==1) {
        	this.lowestLevelName = hierarchy1.getName();
        }  else if (levelNumbers==2) {
        	this.lowestLevelName = hierarchy2.getName();
        }  else if (levelNumbers==3) {
        	this.lowestLevelName = hierarchy3.getName();
        }  else if (levelNumbers==4) {
        	this.lowestLevelName = hierarchy4.getName();
        }  else if (levelNumbers==5) {
        	this.lowestLevelName = hierarchy5.getName();
        }  else if (levelNumbers==6) {
        	this.lowestLevelName = hierarchy6.getName();
        }  else if (levelNumbers==8) {
        	this.lowestLevelName = hierarchy7.getName();
        }  else if (levelNumbers==9) {
        	this.lowestLevelName = hierarchy8.getName();
        }
        //location.setHierarchy(lowestLevelExtId);
    }
    
    public String getLatestLevelName () {
    	return lowestLevelName;
    }
    
    private String generateLocationId(ContentResolver resolver) {
        Cursor cursor = resolver.query(OpenHDS.Locations.CONTENT_ID_URI_BASE,
                new String[] { OpenHDS.Locations.COLUMN_LOCATION_EXTID }, OpenHDS.Locations.COLUMN_LOCATION_EXTID
                        + " LIKE ?", new String[] { getLatestLevelExtId() + "%" }, OpenHDS.Locations.COLUMN_LOCATION_EXTID
                        + " DESC");

        String generatedId = null;
        if (cursor.moveToFirst()) {
            generatedId = generateLocationIdFrom(cursor.getString(0), resolver);
        } else {
            generatedId = getLatestLevelExtId() + "000001";
        }

        cursor.close();
        return generatedId;
    }

    private String generateLocationIdFrom(String lastGeneratedId, ContentResolver resolver) {
        try {
            int increment = Integer.parseInt(lastGeneratedId.substring(3, 9));
            int nextIncrement = increment + 1;
            return String.format(getLatestLevelExtId() + "%06d", nextIncrement);
        } catch (NumberFormatException e) {
            return getLatestLevelExtId() + "000001";
        }
    }

    public void createVisit(ContentResolver resolver) {
        String suffix= round.getRoundNumber();
    	while(suffix.length() < 3){
    		suffix="0"+suffix;
    	}
    	
        String generatedId = location.getExtId() + suffix ;


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());

        visit = new Visit();
        visit.setExtId(generatedId);
        visit.setDate(date);
    }

    public PregnancyOutcome createPregnancyOutcome(ContentResolver resolver, int liveBirthCount) {
        PregnancyOutcome outcome = new PregnancyOutcome();
        outcome.setMother(selectedIndividual);

        if (liveBirthCount > 0) {
        	String[] ids = generateIndividualIds(resolver, liveBirthCount);
            String[] permIds = generateIndividualPermIds(resolver, liveBirthCount);
            for (int i=0; i < ids.length; i++) {
            	String id = ids[i];
            	String permId = permIds[i];
            	
                outcome.addChildId(id);                
                outcome.addChildPermId(permId);
            }
        }

        return outcome;
    }

    private String[] generateIndividualIds(ContentResolver resolver, int liveBirthCount) {
    	
        Cursor cursor = getCursor(resolver);
        int lastIndividualCount = 0;
        if (cursor.moveToFirst()) {
            try {
                lastIndividualCount = Integer.parseInt(cursor.getString(0).substring(9, 12));
            } catch (NumberFormatException e) {
            }
        }

        int nextIndividualCount = lastIndividualCount + 1;
        String[] ids = new String[liveBirthCount];
        for (int i = 0; i < liveBirthCount; i++) {
            ids[i] = location.getExtId() + String.format("%03d", nextIndividualCount);
            nextIndividualCount += 1;
        }

        cursor.close();

        return ids;
    }

    // an option to create a new social group rather than to reference an
    // existing one
    public SocialGroup createSocialGroup(ContentResolver resolver) {
        SocialGroup sg = new SocialGroup();

        String socialGroupPrefix = getLatestLevelExtId() + location.getExtId().substring(3, 9);

        Cursor cursor = resolver.query(OpenHDS.SocialGroups.CONTENT_ID_URI_BASE,
                new String[] { OpenHDS.SocialGroups.COLUMN_SOCIALGROUP_EXTID },
                OpenHDS.SocialGroups.COLUMN_SOCIALGROUP_EXTID + " LIKE ?", new String[] { socialGroupPrefix + "%" },
                OpenHDS.SocialGroups.COLUMN_SOCIALGROUP_EXTID + " DESC");

        if (cursor.moveToNext()) {
        	cursor.close();
        	return null;
        } else {
            sg.setExtId(socialGroupPrefix + "00");
        }

        cursor.close();

        return sg;
    }

    public Individual determinePregnancyOutcomeFather(ContentResolver resolver) {
        Cursor cursor = Queries.getRelationshipByIndividualA(resolver, selectedIndividual.getExtId());
        List<Relationship> rels = Converter.toRelationshipList(cursor);
        // the selected individual will always be the 'individualA' in the
        // relationship
        cursor = Queries.getRelationshipByIndividualB(resolver, selectedIndividual.getExtId());
        rels.addAll(Converter.toRelationshipListSwapped(cursor));

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Relationship currentHusband = null;
        // must find the most current relationship
        for (Relationship rel : rels) {

            if (currentHusband == null) {
                currentHusband = rel;
            } else {
                try {
                    Date currentDate = formatter.parse(currentHusband.getStartDate());
                    Date relDate = formatter.parse(rel.getStartDate());
                    if (currentDate.before(relDate))
                        currentHusband = rel;

                } catch (ParseException e) {
                    return null;
                }
            }
        }

        if (currentHusband == null) {
            return null;
        } else {
            String fatherId = currentHusband.getIndividualB();
            cursor = Queries.getIndividualByExtId(resolver, fatherId);
            return Converter.toIndividual(cursor);
        }
    }

    public Visit getVisit() {
        return visit;
    }

    public boolean isVisitStarted() {
        return visit != null;
    }
    
    private Cursor getCursor(ContentResolver resolver){
    	Cursor cursor = resolver.query(OpenHDS.Individuals.CONTENT_ID_URI_BASE,
                new String[] { OpenHDS.Individuals.COLUMN_INDIVIDUAL_EXTID },
                OpenHDS.Individuals.COLUMN_INDIVIDUAL_EXTID + " LIKE ?", new String[] { location.getExtId() + "%" },
                OpenHDS.Individuals.COLUMN_INDIVIDUAL_EXTID + " DESC");
    	return cursor;
    }

    public String generateIndividualId(ContentResolver resolver) {   	
    	
    	Cursor cursor=getCursor(resolver);
    	
        String id = null;
        if (cursor.moveToNext()) {
        	
            int lastIncrement = Integer.parseInt(cursor.getString(0).substring(9, 12));
            int nextIncrement = lastIncrement + 1;
            id = location.getExtId() + String.format("%03d", nextIncrement);
        } else {
            id = location.getExtId() + "001";
        }

        cursor.close();

        return id;
    }
    
	public String generateIndividualPermID(ContentResolver resolver) {

		Cursor cursor = resolver
				.query(OpenHDS.Individuals.CONTENT_ID_URI_BASE,
						new String[] { OpenHDS.Individuals.COLUMN_INDIVIDUAL_LASTNAME },
						OpenHDS.Individuals.COLUMN_INDIVIDUAL_LASTNAME
								+ " LIKE ?", new String[] { location.getName()
								+ "%" },
						OpenHDS.Individuals.COLUMN_INDIVIDUAL_LASTNAME + " ASC");

		String generatedPermID = null;

		List<String> allPermIds = new ArrayList<String>();
	         
	    while(cursor.moveToNext()){
	    	allPermIds.add(cursor.getString(0));
	    }
	         		
		// 001-999
		// NEXT
		if (allPermIds.size() > 0) {

			int nextCode = 0;

			for (int i = 1; i <= 99; i++) {
				
				try {
					String permid = String.format(location.getName() + "-" + "%02d", i);
         			
         			//Log.d("possible-locname", ""+locName);         			
         			if (allPermIds.contains(permid)){
         				continue;
         			}else{
         				nextCode = i;
         				break;
         			}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (nextCode == 0) {
				generatedPermID = "COULDNT GENERATE";
			} else {
				generatedPermID = String.format(location.getName() + "-" + "%02d", nextCode);
			}
			
		} else {
			generatedPermID = location.getName() + "-01";
		}

		cursor.close();
		return generatedPermID;
	}

	public String[] generateIndividualPermIds(ContentResolver resolver,
			int liveBirthCount) {

		String[] ids = new String[liveBirthCount];

		Cursor cursor = resolver
				.query(OpenHDS.Individuals.CONTENT_ID_URI_BASE,
						new String[] { OpenHDS.Individuals.COLUMN_INDIVIDUAL_LASTNAME },
						OpenHDS.Individuals.COLUMN_INDIVIDUAL_LASTNAME
								+ " LIKE ?", new String[] { location.getName()
								+ "%" },
						OpenHDS.Individuals.COLUMN_INDIVIDUAL_LASTNAME + " ASC");

		int countBabies = 0;

		List<String> allPermIds = new ArrayList<String>();
        
	    while(cursor.moveToNext()){
	    	allPermIds.add(cursor.getString(0));
	    }
		
		// 001-999
		// NEXT
		if (cursor.moveToFirst()) {

			int nextCode = 0;

			for (int i = 1; i <= 99; i++) {
				
				try {
					
					String permid = String.format(location.getName() + "-" + "%02d", i);
         			
         			//Log.d("possible-locname", ""+locName);         			
         			if (allPermIds.contains(permid)){
         				continue;
         			}else{
         				nextCode = i;
         				         				
         				ids[countBabies++] = String.format(location.getName() + "-" + "%02d", nextCode);

						if (countBabies == liveBirthCount) {
							break;
						}
         			}
					
				} catch (NumberFormatException e) {
					cursor.moveToNext();
				}
			}

		}

		if (countBabies == 0) {
			for (int i = 0; i < liveBirthCount; i++)
				ids[i] = "COULDNT GENERATE " + i;
		}

		cursor.close();

		return ids;
	}

    private boolean isClip(){
		return true;
	}
	
	private String getClusterId(String nbCode){
		java.util.Map<String,String> maps = new java.util.HashMap<String, String>();
        
        maps.put("01", "01");
        maps.put("02", "01");
        maps.put("03", "01");
        maps.put("04", "01");
        maps.put("05", "01");
        maps.put("06", "01");
        maps.put("07", "01");
        maps.put("08", "01");
        maps.put("09", "01");
        maps.put("10", "01");
        maps.put("37", "01");
        maps.put("38", "01");
        maps.put("33", "02");
        maps.put("39", "02");
        maps.put("31", "03");
        maps.put("32", "03");
        maps.put("34", "03");
        maps.put("35", "03");
        maps.put("60", "04");
        maps.put("61", "05");
        maps.put("62", "06");
        maps.put("63", "07");
        maps.put("64", "08");
        maps.put("65", "09");
        maps.put("66", "10");
        maps.put("67", "11");
        maps.put("68", "12");
		
		String code = nbCode.substring(0,2);
		
		if (maps.containsKey(code)){
			return maps.get(code);
		}
		
		return "00";
	}
	
	public PregnancyControl getLastPregnancyControl(Context mContext, Individual individual) {
		PregnancyControl pregnancyControl = null;
		
		org.openhds.mobile.clip.database.Database db = new org.openhds.mobile.clip.database.Database(mContext);
		db.open();
		
		Cursor cursor = db.query(PregnancyControl.class, PregnancyControlTable.COLUMN_PERM_ID + " = ?", new String[] { individual.getLastName() }, null, null, PregnancyControlTable.COLUMN_PREGNANCY_ID + " DESC");
		
		Log.d("search pid", ""+individual.getLastName()+", "+cursor.getCount());
		
		if (cursor != null && cursor.moveToFirst()){ //If already exists a pregnancy_id
			
			//Create the pregnancy_id based on the last created
			pregnancyControl = org.openhds.mobile.clip.database.Converter.cursorToPregnancyControl(cursor);
		}
		
		return pregnancyControl;
	}
	
	public PregnancyControl getLastPregnancyControl(Context mContext, String individualPermId) {
		PregnancyControl pregnancyControl = null;
		
		org.openhds.mobile.clip.database.Database db = new org.openhds.mobile.clip.database.Database(mContext);
		db.open();
		
		Cursor cursor = db.query(PregnancyControl.class, PregnancyControlTable.COLUMN_PERM_ID + " = ?", new String[] { individualPermId }, null, null, PregnancyControlTable.COLUMN_PREGNANCY_ID + " DESC");
		
		//Log.d("search pid", ""+individual.getLastName()+", "+cursor.getCount());
		
		if (cursor != null && cursor.moveToFirst()){ //If already exists a pregnancy_id
			
			//Create the pregnancy_id based on the last created
			pregnancyControl = org.openhds.mobile.clip.database.Converter.cursorToPregnancyControl(cursor);
		}
		
		return pregnancyControl;
	}
}
