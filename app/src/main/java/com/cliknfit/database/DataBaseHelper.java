package com.cliknfit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cliknfit.pojo.CardDBModel;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by prince on 18/09/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "clicknfitdatabase";
    private Context mcontext;

    /*for database*/
    private static final String TABLE_CARD = "card";

    private static final String ID = "id";
    public static final String CARDTYPE = "cardtype";
    private static final String CARDNUMBER = "cardnumber";
    private static final String EXPDATE = "expdate";
    private static final String CVV = "cvv";

    private String CREATE_CARD_TABLE = "CREATE TABLE " +TABLE_CARD + "("
            + ID + " INTEGER PRIMARY KEY,"
            + CARDNUMBER + " TEXT,"
            + CARDTYPE + " TEXT,"
            + EXPDATE + " TEXT,"
            + CVV + " TEXT" + ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CARD_TABLE);
        // Create tables again
        onCreate(db);
    }


    public void addCard(CardDBModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CARDNUMBER, model.getCardnumber());
        values.put(CARDTYPE, model.getCardtype());
        values.put(EXPDATE, model.getExpdate());
        values.put(CVV, model.getCvv());

        // Inserting Row
        db.insert(TABLE_CARD, null, values);
        db.close(); // Closing database connection
    }

    // code to get all Card details in a list view
    public ArrayList<CardDBModel> getAllCards() {
        ArrayList<CardDBModel> contactList = new ArrayList<CardDBModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CARD;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CardDBModel model = new CardDBModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setCardnumber(cursor.getString(1));
                model.setCardtype(cursor.getString(2));
                model.setExpdate(cursor.getString(3));
                model.setCvv(cursor.getString(4));

                // Adding model to list
                contactList.add(model);
            } while (cursor.moveToNext());
        }
        // return model list
        return contactList;
    }


    // Deleting single contact
    public void deleteContact(CardDBModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARD, ID + " = ?",
                new String[] { String.valueOf(model.getId()) });
        db.close();
    }

}