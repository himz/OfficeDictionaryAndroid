package com.himz.officedictionary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.himz.entities.ParsePhrase;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity which starts an intent for either the logged in (MainActivity) or logged out
 * (SignUpOrLoginActivity) activity.
 */
public class DispatchActivity extends Activity {

  public DispatchActivity() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Check if there is current user info
    if (ParseUser.getCurrentUser() != null) {

        //Temporarily seed the parse database
        //seed();
      // Start an intent for the logged in activity
      startActivity(new Intent(this, DashboardActivity.class));
    } else {
      // Start and intent for the logged out activity
      startActivity(new Intent(this, WelcomeActivity.class));
    }
  }



    private void seed(){
        post("Ball is in your court","To tell other person politely, that they have to make a decision","We have delivered as per schedule, ball is in your court now to test the feature and provide feedback");
        post("Let's get the ball rolling","Ask to get started on a task","We have sufficient data points to get the ball rolling on the task");
    }
    private void post(String text, String usage, String meaning) {


        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(DispatchActivity.this);
        dialog.setMessage(getString(R.string.progress_post));
        dialog.show();


        // Create a post.
        ParsePhrase parsePhrase = new ParsePhrase();

        parsePhrase.setPhraseText(text);
        parsePhrase.setUsage(usage);
        parsePhrase.setMeaning(meaning);
        parsePhrase.setUser(ParseUser.getCurrentUser());
        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        parsePhrase.setACL(acl);

        // Save the post
        parsePhrase.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });
    }


}
