package com.himz.officedictionary;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.himz.databases.DashboardManager;
import com.himz.entities.Phrase;
import com.himz.helpers.App;


public class MainActivity extends ActionBarActivity {
    static App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtPhrase = (TextView) findViewById(R.id.textView2);
        TextView txtMeaning = (TextView) findViewById(R.id.textView3);
        TextView txtUsage = (TextView) findViewById(R.id.textView1);

        String s= getIntent().getStringExtra("phraseID");
        Integer phraseID = getIntent().getIntExtra("phraseID", 1);
        Phrase phrase = new Phrase();
        //Phrase phrase = DashboardManager.getRandomPhrase(this.getApplication());
        //Phrase phrase = DashboardManager.getPhraseFromID(this.getApplication(), phraseID);
        for (int i = 0; i < app.phraseList.size() ; i++) {
            if(((Phrase)app.phraseList.get(i)).getId() == phraseID)
                phrase = app.phraseList.get(i);
        }
        if(phrase == null)
            return;
        txtPhrase.setText(phrase.getPhraseText());
        txtMeaning.setText(phrase.getMeaning());
        txtUsage.setText(phrase.getUsage());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
        });
        menu.findItem(R.id.action_create_phrase).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
