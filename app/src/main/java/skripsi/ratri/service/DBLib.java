package skripsi.ratri.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ratri on 2/6/2015.
 */
public class DBLib extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_kuliner";
    public DBLib(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE if not exists user " +
                        "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "email TEXT);");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public Cursor getUser(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select _id,name,email " +
                "From user Limit 1",new String [] {});
        return cur;
    }

    public void insertUser(String name,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        db.insert("user",null, cv);
        db.close();
    }

    public void clearUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        dropUser(db);
        createUser(db);
        db.close();
    }

    public void dropUser(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS user");
    }
    public void createUser(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE if not exists user " +
                        "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "email TEXT);");
    }
}
