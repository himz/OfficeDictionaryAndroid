package com.himz.databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.himz.entities.Phrase;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple database access helper class. Defines the basic
 * <p/>
 * Inserting this file as template holder for our database for bite app.
 */
public class LocalDbAdapter {

    /* Global Constants */
    private static final String TABLE_PHRASE_NAME = "Phrase";
    /* Once a row is filled in the phrase table, that is never deleted. Only modified */
    private static final String CREATE_PHRASE_TABLE = "create table " + TABLE_PHRASE_NAME + "(phraseID integer, phraseText text, meaning text, usage text, upVotes integer, downVotes integer)";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context mCtx;

	/*Database creation sql statement*/


    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "officeDictionaryDB";


    private static final int DATABASE_VERSION = 2;
    private List<Phrase> allPhrase;


    private static class DatabaseHelper extends SQLiteOpenHelper {

        private Context context;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            System.out.println("In DatabaseHelper constructor");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("In onCreate()");
            db.execSQL(CREATE_PHRASE_TABLE);
            /* Also seed data for default values */
            seedData(db);

        }

        public void seedData(SQLiteDatabase db) {
            //@ToDo add Seed data here
            createPhraseRow(db, 1, "Ball is in your court", "To tell other person politely, that they have to make a decision", "We have delivered as per schedule, ball is in your court now to test the feature and provide feedback", 10, 2);
            createPhraseRow(db, 2, "Let's get the ball rolling", "Ask to get started on a task", "We have sufficient data points to get the ball rolling on the task", 15, 1);
        }


        private long createPhraseRow(SQLiteDatabase db, int phraseID, String phraseText, String meaning, String usage, int upVotes, int downVotes) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("phraseID", phraseID);
            initialValues.put("phraseText", phraseText);
            initialValues.put("meaning", meaning);
            initialValues.put("usage", usage);
            initialValues.put("upVotes", upVotes);
            initialValues.put("downVotes", downVotes);
            Log.d(TAG, "Inserting values in Phrase Table");
            return db.insert(TABLE_PHRASE_NAME, null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS TripIt");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public LocalDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the bite database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws android.database.SQLException if the database could be neither opened or created
     */
    public LocalDbAdapter open() throws SQLException {

        System.out.println("In open()");

        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();

        return this;
    }

    /**
     * @brief Close the adapter
     */
    public void close() {
        mDbHelper.close();
    }

    public Phrase getPhraseFromID(int phraseID) {
        Phrase phrase = new Phrase();

        Cursor c = null;
        c = mDb.rawQuery("select * from " + TABLE_PHRASE_NAME + " where phraseID = " + "\"" + phraseID + "\"", null);

        try {
            if (c.moveToFirst()) {
                do {
                    /* Assuming only one restaurant of same name for now. Else only the last restaurant will be shown */
                    phrase.setId(Integer.parseInt(c.getString(0)));
                    phrase.setPhraseText(c.getString(1));
                    phrase.setMeaning(c.getString(2));
                    phrase.setUsage(c.getString(3));
                    phrase.setUpVotes(Integer.parseInt(c.getString(4)));
                    phrase.setDownVotes(Integer.parseInt(c.getString(5)));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phrase;
    }

    public List<Phrase> getAllPhrase() {
        Phrase phrase;
        allPhrase = new ArrayList<Phrase>();
        Cursor c = null;
        c = mDb.rawQuery("select * from " + TABLE_PHRASE_NAME, null);

        try {
            if (c.moveToFirst()) {
                do {
                    phrase = new Phrase();
                    /* Assuming only one restaurant of same name for now. Else only the last restaurant will be shown */
                    phrase.setId(Integer.parseInt(c.getString(0)));
                    phrase.setPhraseText(c.getString(1));
                    phrase.setMeaning(c.getString(2));
                    phrase.setUsage(c.getString(3));
                    phrase.setUpVotes(Integer.parseInt(c.getString(4)));
                    phrase.setDownVotes(Integer.parseInt(c.getString(5)));
                    allPhrase.add(phrase);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allPhrase;
    }


    public int getPhraseCount() {
        return fetchRowCountOfTable(TABLE_PHRASE_NAME);
    }

    /**
     * Remove all users and groups from database.
     */
    public void deleteAllRowsFromTable(String tableName) {
        mDb.delete(tableName, null, null);
    }

    /**
     * Function to update the description of the activity
     *
     * @param description
     * @param activityID
     * @return true, if updated and false if not updated
     */
    public boolean updateDescriptionOfActivity(String description, int activityID) {
        String strSQL = "UPDATE ActivityTable SET description = " + "\"" + description + "\"" + "WHERE activityID = " + "\"" + activityID + "\"";
        if (strSQL != null) {
            try {
                mDb.execSQL(strSQL);
                return true;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Function to return the last activityID from the table
     *
     * @return
     */
    public int fetchRowCountOfTable(String tableName) {
        Cursor c = null;
        c = mDb.rawQuery("select count(*) from " + tableName, null);
        c.moveToFirst();
        int rowCount = c.getInt(0);
        return rowCount;
    }
} 