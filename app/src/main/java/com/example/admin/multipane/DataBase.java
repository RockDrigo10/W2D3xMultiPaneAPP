package com.example.admin.multipane;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 8/9/2017.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Artits";

    public static final String TABLE_NAME ="Artits";
    public static final String CONTACT_ID ="ID";
    public static final String CONTACT_NAME ="Name";
    public static final String CONTACT_NATIONALITY ="Nationality";
    public static final String CONTACT_GENDER ="Gender";
    public static final String CONTACT_IMG ="IMG";

    private static final String TAG = "MyDBTag";
    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONTACT_NAME + " TEXT, " +
                CONTACT_NATIONALITY + " TEXT, " +
                CONTACT_GENDER + " TEXT, " +
                CONTACT_IMG + " BLOB " +
                ")";
        Log.d(TAG, "onCreate: "+CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void saveNewArtist(Artist artist){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_NAME, artist.getName());
        contentValues.put(CONTACT_NATIONALITY, artist.getNationality());
        contentValues.put(CONTACT_GENDER, artist.getGender());
        contentValues.put(CONTACT_IMG, artist.getBitmap());

        Log.d(TAG, "saveNewContact: "+artist.getName() + " " + artist.getNationality());

        database.insert(TABLE_NAME,null,contentValues);
    }
    public void uploadNewArtist(Artist artist, String id){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_NAME, artist.getName());
        contentValues.put(CONTACT_NATIONALITY, artist.getNationality());
        contentValues.put(CONTACT_GENDER, artist.getGender());
        contentValues.put(CONTACT_IMG, artist.getBitmap());
        database.update(TABLE_NAME,contentValues, CONTACT_ID+" = ? ",new String[]{id});
    }

    public void DeleteContact(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        String[] args = new String[]{id};

        String DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + CONTACT_ID + " = ? ";
        Log.d(TAG, "DeleteContact: "+DELETE);
        database.execSQL(DELETE,args);
    }

    public ArrayList<Artist> getArtists(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "";
        Cursor cursor;
        if(id.equals("-1")){
            query = "SELECT * FROM " + TABLE_NAME;
            Log.d(TAG, "getArtists: "+query + " " + id);
            cursor = database.rawQuery(query, null);
            //Log.d(TAG, "getArtists: " + cursor.getInt(0) +cursor.getString(1)+cursor.getString(2)+cursor.getString(3)+cursor.getBlob(4));
        }
        else {
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CONTACT_ID + " = ?";
            Log.d(TAG, "getArtists: "+query + " " + id);
            String parameters[] = {id};
            cursor = database.rawQuery(query, parameters);
        }
        //Cursor cursor = database.rawQuery(query, null);
        ArrayList<Artist> artists = new ArrayList();
        if(cursor.moveToFirst()){
            do{
                Log.d(TAG, "getArtists: Name:" + cursor.getString(1));
                artists.add(new Artist(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(4)));
            }while(cursor.moveToNext());
        }
        else{
            Log.d(TAG, "getArtists: empty");
        }
        Log.d(TAG, "getArtists: " + artists);
        return artists;

    }
}
