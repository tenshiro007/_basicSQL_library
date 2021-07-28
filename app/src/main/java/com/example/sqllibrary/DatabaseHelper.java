package com.example.sqllibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="library";
    public static final int DB_VERSION=1;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStatement="CREATE TABLE books(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,author VARCHAR,imageUrl TEXT,description TEXT)";
        db.execSQL(sqlStatement);
        initDatabase( db);
    }

    private void initDatabase(SQLiteDatabase db) {
        ContentValues firstBook=new ContentValues();
        firstBook.put("name","Landslide");
        firstBook.put("author","Michael Wolff");
        firstBook.put("imageUrl","https://d1w7fb2mkkr3kw.cloudfront.net/assets/images/book/lrg/9781/4087/9781408714652.jpg");
        firstBook.put("description","Politics has given us some shocking and confounding moments but none have come close to the careening final days of Donald Trump's presidency: the surreal stage management of his re-election campaign, his audacious election challenge, the harrowing mayhem of the storming of the Capitol and the buffoonery of the second impeachment trial. But what was really going on in the inner sanctum of the White House during these calamitous events? What did the president and his dwindling cadre of loyalists actually believe? And what were they planning?");
        db.insert("books",null,firstBook);

        ContentValues secoundBook=new ContentValues();
        secoundBook.put("name","The Cult of We");
        secoundBook.put("author"," Eliot Brown , Maureen Farrell");
        secoundBook.put("imageUrl","https://d1w7fb2mkkr3kw.cloudfront.net/assets/images/book/mid/9780/5932/9780593239759.jpg");
        secoundBook.put("description","The definitive inside story of WeWork, its audacious founder, and what its epic unraveling says about a financial system drunk on the elixir of Silicon Valley innovation—from the Wall Street Journal correspondents (recently featured in the WeWork Hulu documentary) whose scoop-filled reporting hastened the company’s downfall.");
        db.insert("books",null,secoundBook);

        ContentValues thirdBook=new ContentValues();
        thirdBook.put("name","Collision Course");
        thirdBook.put("author","  Hans Greimel , William");
        thirdBook.put("imageUrl","https://d1w7fb2mkkr3kw.cloudfront.net/assets/images/book/lrg/9781/6478/9781647820473.jpg");
        thirdBook.put("description","In Japan it's called the \"Ghosn Shock\"—the stunning arrest of Carlos Ghosn, the jet-setting CEO who saved Nissan and made it part of a global automotive empire.\n" +
                "\n" +
                "Even more shocking was his daring escape from Japan, packed into a box and put on a private jet to Lebanon after months spent in a Japanese detention center, subsisting on rice gruel.");
        db.insert("books",null,thirdBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
