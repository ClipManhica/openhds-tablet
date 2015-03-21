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
		db.execSQL(CREATE_TABLE_PREGNANCY_IDENTIFICATION);;
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

}
