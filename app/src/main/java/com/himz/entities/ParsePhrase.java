package com.himz.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data Model for Phrases to be put online and accessed
 *
 * Created by himanshu on 5/18/15.
 */
@ParseClassName("Phrases")
public class ParsePhrase extends ParseObject {
    int id;
    String phraseText;
    String meaning;
    String usage;

    int upVotes;
    int downVotes;

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

    public String getPhraseText() {
        return getString("phraseText");
    }

    public void setPhraseText(String value) {
        put("phraseText", value);
    }

    public String getMeaning() {
        return getString("meaning");
    }

    public void setMeaning(String value) {
        put("meaning", value);
    }
    public String getUsage() {
        return getString("usage");
    }

    public void setUsage(String value) {
        put("usage", value);
    }



    public static ParseQuery<ParsePhrase> getQuery() {
        return ParseQuery.getQuery(ParsePhrase.class);
    }
}
