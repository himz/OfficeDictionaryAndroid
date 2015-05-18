package com.himz.helpers;
import com.himz.entities.ParsePhrase;
import com.himz.entities.Phrase;
/**
 * Class to store all the global variables.
 * Mainly handling database connections for now
 * As a general practice, start moving shared state among actiities to this
 * class.
 *
 * @author himanshu
 *
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.himz.databases.LocalDbAdapter;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class App extends Application {
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "OfficeDictionary";


	/* List of global variables, to be shared across activities */
    public LocalDbAdapter db;
    public static List<Phrase> phraseList;

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		Context ctx = getApplicationContext();
		this.db = new LocalDbAdapter(ctx);
		this.db.open();
        phraseList = new ArrayList<Phrase>();

        ParseObject.registerSubclass(ParsePhrase.class);
        Parse.initialize(this, "wHSnSfgjesKSuGEURpog8W09MczF64PFcKSJp8aS",
                "Zz5wehnUDabyzkqI26MokUARC4M8QDYwHMm38fB4");

        preferences = getSharedPreferences("com.himz.officedictionary", Context.MODE_PRIVATE);

        configHelper = new ConfigHelper();
        configHelper.fetchConfigIfNeeded();
	}
	@Override
	public void onTerminate() {
		this.db.close();
		super.onTerminate();
	}
}