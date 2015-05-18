package com.himz.databases;

import android.content.Context;


import com.himz.entities.Phrase;
import com.himz.helpers.App;

import java.util.List;
import java.util.Random;

/**
 * File to take care of calling the database
 *
 */
public class DashboardManager {
	static App app;
    static Random rand;

    public static List<Phrase> getPopularPhraseFromServer(Context ctx) {
        return ConnectionHandler.getPopularPhraseList(ctx);
    }

    public static List<Phrase> getUpVotedPhraseFromServer(Context ctx) {
        return ConnectionHandler.getUpVotedPhraseList(ctx);
    }

    public static List<Phrase> getNewAndTrendingPhraseFromServer(Context ctx) {
        return ConnectionHandler.getNewTrendingPhraseList(ctx);
    }

	public static Phrase getPhraseFromID(Context ctx, int phraseID) {
		app = ((App)ctx.getApplicationContext());
		return app.db.getPhraseFromID(phraseID);
	}

    /* Function to get data from the database, which is not required as of now */

    public static List<Phrase> getAllPhrase(Context ctx) {
        app = ((App)ctx.getApplicationContext());
        return app.db.getAllPhrase();
    }

    public static int getPhraseCount(Context ctx) {
        app = ((App)ctx.getApplicationContext());
        return app.db.getPhraseCount();
    }

    public static Phrase getRandomPhrase(Context ctx) {
        // Get row count, generate random number and then send that phrase out.
        int noOfRows = getPhraseCount(ctx);
        // Generate random phraseID
        int randomPhraseID = randInt(1, noOfRows, rand);
        return getPhraseFromID(ctx, randomPhraseID);
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @param rand Random variable to be passed to function
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max, Random rand) {
        if(rand == null)
            rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}