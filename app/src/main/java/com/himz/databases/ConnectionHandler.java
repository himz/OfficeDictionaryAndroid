package com.himz.databases;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.himz.entities.ParsePhrase;
import com.himz.entities.Phrase;
import com.himz.helpers.App;
import com.himz.officedictionary.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by himanshu on 2/24/15.
 */
public class ConnectionHandler {

    public static StringBuilder responseString;
    private int mostRecentMapUpdate;


    /*
     * Set up the query to update the map view
     */
    private void doMapQuery() {
        final int myUpdateNumber = ++mostRecentMapUpdate;
        ParseQuery<ParsePhrase> phraseQuery = ParsePhrase.getQuery();
        // Set up additional query filters
        phraseQuery.include("user");
        phraseQuery.orderByDescending("createdAt");
        // Kick off the query in the background
        phraseQuery.findInBackground(new FindCallback<ParsePhrase>() {
            @Override
            public void done(List<ParsePhrase> objects, ParseException e) {
                if (e != null) {
                    if (App.APPDEBUG) {
                        Log.d(App.APPTAG, "An error occurred while querying for map posts.", e);
                    }
                    return;
                }
                /*
                * Make sure we're processing results from
                * the most recent update, in case there
                * may be more than one in progress.
                */
                if (myUpdateNumber != mostRecentMapUpdate) {
                    return;
                }
                // Posts to show on the map
                Set<String> toKeep = new HashSet<String>();
                // Loop through the results of the search
                for (ParsePhrase post : objects) {
                    // Add this post to the list of map pins to keep
                    toKeep.add(post.getObjectId());
                    // Check for an existing marker for this post
                }
            }
        });
    }


    /*Currently only using json file to give back the data*/
    // Function to server and get response back from server -- json file
    public static List<Phrase> getPopularPhraseList(Context ctx) {
        responseString = new StringBuilder();
        InputStream is = ctx.getResources().openRawResource(R.raw.phrase);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            while ((readLine = br.readLine()) != null) {
                responseString.append(readLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseJson(responseString);

    }

    // Function to server and get response back from server -- json file
    public static List<Phrase> getNewTrendingPhraseList(Context ctx) {
        responseString = new StringBuilder();
        InputStream is = ctx.getResources().openRawResource(R.raw.phrasenewtrending);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            while ((readLine = br.readLine()) != null) {
                responseString.append(readLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseJson(responseString);

    }

    // Function to server and get response back from server -- json file
    public static List<Phrase> getUpVotedPhraseList(Context ctx) {
        responseString = new StringBuilder();
        InputStream is = ctx.getResources().openRawResource(R.raw.phraseupvoted);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            while ((readLine = br.readLine()) != null) {
                responseString.append(readLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseJson(responseString);

    }


    //parse the response from json into List of Phrase Object
    private static List<Phrase> parseJson(StringBuilder responseString) {
        List<Phrase> phraseList = new ArrayList<Phrase>();
        Phrase phrase;
        try {
            JSONObject jObject = new JSONObject(String.valueOf(responseString));
            JSONArray jArray = jObject.getJSONArray("phrases");
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String phraseID = oneObject.getString("phraseID");
                    String phraseText = oneObject.getString("phraseText");
                    String meaning = oneObject.getString("meaning");
                    String usage = oneObject.getString("usage");
                    String upvotes = oneObject.getString("upvotes");
                    String downvotes = oneObject.getString("downvotes");

                    phrase = new Phrase();
                    phrase.setId(Integer.parseInt(phraseID));
                    phrase.setPhraseText(phraseText);
                    phrase.setMeaning(meaning);
                    phrase.setUsage(usage);
                    phrase.setUpVotes(Integer.parseInt(upvotes));
                    phrase.setDownVotes(Integer.parseInt(downvotes));
                    phraseList.add(phrase);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phraseList;


    }
}
