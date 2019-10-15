package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ChessTournamentManager.db";
    public static final String TABLE_NAME = "Players";
    public static final String ID_COLUMN = "ID";
    public static final String NAME_COLUMN = "NAME";
    public static final String SURNAME_COLUMN = "SURNAME";
    public static final String POLISH_RANKING_COLUMN = "POLISH_RANKING";
    public static final String INTERNATIONAL_RANKING_COLUMN = "INTERNATIONAL_RANKING";
    public static final String DATE_OF_BIRTH_COLUMN = "DATE_OF_BIRTH";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlQuery = "create table " + TABLE_NAME +
                " ("+ ID_COLUMN+ " integer primary key autoincrement, " +
                NAME_COLUMN + " varchar(30), " +
                SURNAME_COLUMN + " varchar(30), " +
                POLISH_RANKING_COLUMN + " double, " +
                INTERNATIONAL_RANKING_COLUMN + " double, " +
                DATE_OF_BIRTH_COLUMN + " varchar(30)" +")";

        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String surname, double polishRanking, double internationalRanking, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, name);
        contentValues.put(SURNAME_COLUMN, surname);
        contentValues.put(POLISH_RANKING_COLUMN, polishRanking);
        contentValues.put(INTERNATIONAL_RANKING_COLUMN, internationalRanking);
        contentValues.put(DATE_OF_BIRTH_COLUMN, date);
/*        SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pl"));//;new SimpleDateFormat("dd-mm-yyyy", new Locale("pl"));
        String stringDate = sdf.format(date);
        contentValues.put(DATE_OF_BIRTH_COLUMN, stringDate);*/

        return db.insert(TABLE_NAME, null, contentValues) != -1; //return true if insert data to database, otherwise return false
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }
}
