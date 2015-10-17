package com.himz.officedictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.himz.databases.DashboardManager;
import com.himz.entities.ParsePhrase;
import com.himz.entities.Phrase;
import com.himz.helpers.App;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class DashboardActivity extends ActionBarActivity implements ActionBar.TabListener {

    public static App app;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Keep the fragments in the memory
        mViewPager.setOffscreenPageLimit(3);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                Fragment f = ((SectionsPagerAdapter)mViewPager.getAdapter()).getItem(position);
                View ll = f.getView();

            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
                return true;
            }
        });
        menu.findItem(R.id.action_create_phrase).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(DashboardActivity.this, PostActivity.class));
                return true;
            }
        });
        menu.findItem(R.id.action_refresh_page).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Fragment f = ((SectionsPagerAdapter)mViewPager.getAdapter()).getItem(0);
                ((PlaceholderFragment)f).refreshPhraseList(1);
                ((PlaceholderFragment)f).refreshPhraseList(2);
                ((PlaceholderFragment)f).refreshPhraseList(3);

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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }

            View ll = inflater.inflate(R.layout.fragment_dashboard, container, false);
            List<String> data = new ArrayList<String>();
            List<Phrase> phraseList = new ArrayList<Phrase>();
            //phraseList = DashboardManager.getAllPhrase(getActivity().getApplication());
            phraseList = DashboardManager.getUpVotedPhraseFromServer(getActivity().getApplication());
            if(app.phraseList == null || app.phraseList.size() == 0)
                app.phraseList = phraseList;

            CustomAdapter adapter = new CustomAdapter(this.getActivity(), app.phraseList);
            ListView listView = (ListView) ll.findViewById(R.id.list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View view,
                                        int pos, long id) {

                    Phrase p = (Phrase)listView.getAdapter().getItem(pos);
                    int phraseID = p.getId();
                    Intent myintent=new Intent(getActivity(), MainActivity.class).putExtra("phraseID", phraseID);
                    startActivity(myintent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    toast((String) textView.getText());
                }
            });
            int sectionNumber = (int)this.getArguments().get(ARG_SECTION_NUMBER);
            if(sectionNumber == 1) {
                refreshPhraseList(1);
            } else if (sectionNumber == 2) {
                refreshPhraseList(2);
            } else {
                refreshPhraseList(3);
            }
            return ll;
        }

        public void refreshPhraseList(int state) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Phrases");
            if(state == 1) {
                //New
                query.addDescendingOrder("createdAt");
            }
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> parsePhraseList, ParseException e) {
                    if (e == null) {
                        // If there are results, update the list of posts
                        // and notify the adapter
                        int sectionNumber = (int)getArguments().get(ARG_SECTION_NUMBER);
                        
                        app.phraseList.clear();
                        for (ParseObject parsePhrase : parsePhraseList) {
                            ParsePhrase pr = (ParsePhrase) parsePhrase;
                            Phrase phrase = new Phrase(pr.getObjectId(), pr.getPhraseText(),pr.getMeaning(),pr.getUsage());
                            app.phraseList.add(phrase);
                        }
                        ListView listView = (ListView) getView().findViewById(R.id.list);
                        ((CustomAdapter)listView.getAdapter()).notifyDataSetChanged();

                        //((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                    } else {
                        Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                    }
                }
            });
        }

        private void toast(String text) {
            Toast.makeText(getActivity(),
                    String.format("Item clicked: %s", text), Toast.LENGTH_SHORT)
                    .show();
        }

    }

}
