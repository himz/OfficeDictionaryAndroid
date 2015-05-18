package com.himz.officedictionary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.himz.entities.ParsePhrase;
import com.himz.helpers.App;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class PostActivity extends Activity {
    // UI references.
    private EditText phraseEditText;
    private EditText usageEditText;
    private EditText meaningEditText;
    private TextView characterCountTextView;
    private Button postButton;

    private int maxCharacterCount = App.getConfigHelper().getPostMaxCharacterCount();
    private ParseGeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);

        Intent intent = getIntent();

        phraseEditText = (EditText) findViewById(R.id.phrase_edittext);
        usageEditText = (EditText) findViewById(R.id.phrase_usage_text);
        meaningEditText = (EditText) findViewById(R.id.phrase_meaning_text);
        phraseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updatePostButtonState();
                updateCharacterCountTextViewText();
            }
        });

        characterCountTextView = (TextView) findViewById(R.id.character_count_textview);

        postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                post();
            }
        });

        updatePostButtonState();
        updateCharacterCountTextViewText();
    }

    private void post() {
        String text = phraseEditText.getText().toString().trim();
        String usage = usageEditText.getText().toString().trim();
        String meaning = meaningEditText.getText().toString().trim();


        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(PostActivity.this);
        dialog.setMessage(getString(R.string.progress_post));
        dialog.show();

        // Create a post.
        ParsePhrase parsePhrase = new ParsePhrase();

        // Set the location to the current user's location
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
                dialog.dismiss();
                finish();
            }
        });
    }

    private String getPostEditTextText() {
        return phraseEditText.getText().toString().trim();
    }

    private void updatePostButtonState() {
        int length = getPostEditTextText().length();
        boolean enabled = length > 0 && length < maxCharacterCount;
        postButton.setEnabled(enabled);
    }

    private void updateCharacterCountTextViewText() {
        String characterCountString = String.format("%d/%d", phraseEditText.length(), maxCharacterCount);
        characterCountTextView.setText(characterCountString);
    }
}
