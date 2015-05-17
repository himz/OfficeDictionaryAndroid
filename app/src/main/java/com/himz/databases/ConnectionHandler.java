package com.himz.databases;

import android.content.Context;

import com.himz.entities.Phrase;
import com.himz.officedictionary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by himanshu on 2/24/15.
 */
public class ConnectionHandler {

    public static StringBuilder responseString;

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
