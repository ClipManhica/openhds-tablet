package org.openhds.mobile.clip.database;

import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class Database {
	
	public static final String DATABASE_NAME = "clip.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final class PregnancyId implements BaseColumns {
		public static final String TABLE_NAME = "pregnancy_identification";
		
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_INDIVIDUAL_ID = "individualId";
		public static final String COLUMN_PERM_ID = "permId";
		public static final String COLUMN_PREGNANCY_ID = "pregnancyId";
		public static final String COLUMN_COUNT = "count";
		
		public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_INDIVIDUAL_ID, COLUMN_PERM_ID, COLUMN_PREGNANCY_ID, COLUMN_COUNT};
	}
	
	public static final class PregnancyControlTable implements BaseColumns {
		public static final String TABLE_NAME = "pregnancy_control";
		
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_INDIVIDUAL_ID = "individualId";
		public static final String COLUMN_PERM_ID = "permId";
		public static final String COLUMN_PREGNANCY_ID = "pregnancyId";
		public static final String COLUMN_ESTIMATED_DOB = "estimatedDob";
		public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
		public static final String COLUMN_HAS_DELIVERED = "hasDelivered";
		public static final String COLUMN_VISIT_NUMBER = "visitNumber";
		public static final String COLUMN_ANTEPARTUM_VISITS = "antepartumVisits";
		public static final String COLUMN_POSTPARTUM_VISITS = "postpartumVisits";
		public static final String COLUMN_NR_BABIES = "nrBabies";
		public static final String COLUMN_BABY_1 = "baby1";
		public static final String COLUMN_BABY_2 = "baby2";
		public static final String COLUMN_BABY_3 = "baby3";
		public static final String COLUMN_BABY_4 = "baby4";
		public static final String COLUMN_BABY_5 = "baby5";
		public static final String COLUMN_BABY_6 = "baby6";
		public static final String COLUMN_BABY_7 = "baby7";
		public static final String COLUMN_BABY_8 = "baby8";
		public static final String COLUMN_BABY_9 = "baby9";
		
		public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_INDIVIDUAL_ID, COLUMN_PERM_ID, COLUMN_PREGNANCY_ID, 
													COLUMN_ESTIMATED_DOB, COLUMN_DATE_OF_BIRTH, COLUMN_HAS_DELIVERED, COLUMN_VISIT_NUMBER,
													COLUMN_ANTEPARTUM_VISITS, COLUMN_POSTPARTUM_VISITS,
													COLUMN_NR_BABIES, COLUMN_BABY_1, COLUMN_BABY_2, COLUMN_BABY_3, COLUMN_BABY_4,
													COLUMN_BABY_5, COLUMN_BABY_6, COLUMN_BABY_7, COLUMN_BABY_8, COLUMN_BABY_9};
		}
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public Database(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

    public void close() {
	    dbHelper.close();
	}
    
    public long insert(Table entity){  	    	
    	long insertId = -1;
    	
    	insertId = database.insert(entity.getTableName(), null,  entity.getContentValues());
    	
    	return insertId;
    }
    
    public long insert(Collection<? extends Table> entities){  	    	
    	long insertId = -1;
    	
    	for (Table entity : entities){
    		insertId = database.insert(entity.getTableName(), null,  entity.getContentValues());
    	}
    	
    	return insertId;
    }
    
    public int delete(Class<? extends Table> table, String whereClause, String[] whereArgs){
    	Table entity = newInstance(table);
    	
    	int deleteRows = database.delete(entity.getTableName(), whereClause, whereArgs);
    	return deleteRows;
    }
    
    public int update(Class<? extends Table> table, ContentValues values, String whereClause, String[] whereArgs){    	
    	Table entity = newInstance(table);
    	
    	int rows = database.update(entity.getTableName(), values, whereClause, whereArgs);
    	
    	return rows;
    }
    
    /*
    public int update(Table entity){    	
    	    	
    	long rows = database.update(entity.getTableName(), entity.getContentValues(), BaseColumns._ID + " = ?", new String{entity.get});
    	
    	return 0;
    }
    */
    
    public Cursor query(Class<? extends Table> table, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){    	
    	Table entity = newInstance(table);
    	
    	Cursor cursor = database.query(entity.getTableName(), entity.getColumnNames(), selection, selectionArgs, groupBy, having, orderBy);
        	
    	return cursor;
    }
    
    public Cursor query(Class<? extends Table> table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
    	Table entity = newInstance(table);
    	
    	Cursor cursor = database.query(entity.getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);
        	
    	return cursor;
    }
    
    private Table newInstance(Class<? extends Table> entity){
    	try {
			Table obj =  entity.newInstance();
			return obj;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    
}
