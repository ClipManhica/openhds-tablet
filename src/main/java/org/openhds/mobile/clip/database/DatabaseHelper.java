package org.openhds.mobile.clip.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

		
	public DatabaseHelper(Context context) {
		super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PREGNANCY_IDENTIFICATION);
		db.execSQL(CREATE_TABLE_PREGNANCY_CONTROL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public static final String CREATE_TABLE_PREGNANCY_IDENTIFICATION = " "
	 		+ "CREATE TABLE " + Database.PregnancyId.TABLE_NAME + "(" 
			 + Database.PregnancyId.COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			 + Database.PregnancyId.COLUMN_INDIVIDUAL_ID + " TEXT,"
			 + Database.PregnancyId.COLUMN_PERM_ID + " TEXT,"
			 + Database.PregnancyId.COLUMN_PREGNANCY_ID + " TEXT,"
			 + Database.PregnancyId.COLUMN_COUNT + " INTEGER);"
			 
			 + " CREATE UNIQUE INDEX IDX_PREGNANCY_INDV_ID ON " + Database.PregnancyId.TABLE_NAME
             + "(" +  Database.PregnancyId.COLUMN_INDIVIDUAL_ID + ");"
	 		;
	
	public static final String CREATE_TABLE_PREGNANCY_CONTROL = " "
	 		+ "CREATE TABLE " + Database.PregnancyControlTable.TABLE_NAME + "(" 
			 + Database.PregnancyControlTable.COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			 + Database.PregnancyControlTable.COLUMN_INDIVIDUAL_ID + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_PERM_ID + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_PREGNANCY_ID + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_ESTIMATED_DOB + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_DATE_OF_BIRTH + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_HAS_DELIVERED + " INTEGER,"
			 + Database.PregnancyControlTable.COLUMN_VISIT_NUMBER + " INTEGER,"
			 + Database.PregnancyControlTable.COLUMN_ANTEPARTUM_VISITS + " INTEGER,"
			 + Database.PregnancyControlTable.COLUMN_POSTPARTUM_VISITS + " INTEGER,"
			 + Database.PregnancyControlTable.COLUMN_NR_BABIES + " INTEGER,"
			 + Database.PregnancyControlTable.COLUMN_BABY_1 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_2 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_3 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_4 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_5 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_6 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_7 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_8 + " TEXT,"
			 + Database.PregnancyControlTable.COLUMN_BABY_9 + " TEXT);"
			 
			 
			 + " CREATE UNIQUE INDEX IDX_PREGNANCY_CNTL_ID ON " + Database.PregnancyControlTable.TABLE_NAME
             + "(" +  Database.PregnancyControlTable.COLUMN_INDIVIDUAL_ID + ");"
	 		;

}
