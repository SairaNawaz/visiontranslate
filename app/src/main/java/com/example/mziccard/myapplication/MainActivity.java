package com.example.mziccard.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.mziccard.myapplication.Fragments.DashboardFragment;
import com.example.mziccard.myapplication.Fragments.LanguageAdpater;
import com.example.mziccard.myapplication.Fragments.OcrCaptureFragment;
import com.example.mziccard.myapplication.Fragments.TextFragment;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler = new Handler();
    private TranslateOptions translate_options;

    LanguageAdpater languageAdpater;
    Spinner to_language;
    List<Language> languages = new ArrayList<>();

    public static Translate google_translate;
    public static String target_language = "ar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();
    }

    void initViews() {

        //    from_language = findViewById(R.id.from_language);
        to_language = findViewById(R.id.to_language);

        new RetrieveLanguages().execute();

        loadFragment(this
                , R.string.tag_dashboard, null);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_view);
        if (f instanceof DashboardFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
        //    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_view);
            if (f instanceof DashboardFragment) {
                finish();
            } else {
                super.onBackPressed();
            }

        } else if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }
//        else if (id == R.id.nav_manage) {
//
//        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(Context context, final int fTagId,
                             Bundle bundle) {
        final FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        final String tagString = Integer.toString(fTagId);
        Fragment cFragment = fm.findFragmentByTag(tagString);
        if (cFragment == null) {
            final Fragment fragment = getFragmentByTag(fTagId);
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {

                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.container_view, fragment, tagString);
                    fragmentTransaction.addToBackStack(tagString);
                    fragmentTransaction.commit();
                    fm.executePendingTransactions();
                }
            };

            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }
        } else {
            fm.popBackStackImmediate(tagString, 0);
        }
    }

    Fragment getFragmentByTag(int tag) {
        Fragment fragment = null;
        switch (tag) {
            case R.string.tag_ocr:
                fragment = new OcrCaptureFragment();
                break;
            case R.string.tag_dashboard:
                fragment = new DashboardFragment();
                break;
            case R.string.tag_text:
                fragment = new TextFragment();
                break;
        }

        return fragment;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

    class RetrieveLanguages extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        @Override
        protected String doInBackground(String... params) {

            translate_options = TranslateOptions.newBuilder()
                    .setApiKey(getString(R.string.GApp_Key))
                    .build();

            google_translate = translate_options.getService();

            languages = google_translate.listSupportedLanguages();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            languageAdpater = new LanguageAdpater(MainActivity.this, android.R.layout.simple_spinner_item,
                    languages);
            languageAdpater.setDropDownViewResource(R.layout.language_item);
            to_language.setAdapter(languageAdpater);

            for (int i = 0; i < languages.size(); i++) {
                if (languages.get(i).getCode().equalsIgnoreCase("ar")) {
                    to_language.setSelection(i);
                    break;
                }
            }
            to_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    target_language = languages.get(position).getCode();

                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_view);
                    if (f instanceof TextFragment) {
                        ((TextFragment) f).updateTranslation();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }
    }
}
