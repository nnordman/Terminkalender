package de.nnordman.beaf;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import de.nnordman.beaf.Fragments.calendar_Fragment;
import de.nnordman.beaf.Fragments.event_Anlegen_Fragment;
import de.nnordman.beaf.Fragments.events_Fragment;

public class EventUebersicht extends AppCompatActivity {

    private Toolbar toolbar;
    private MyAdapter adapter;
    final int SPEECHINTENT_REQ_CODE = 11;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);

            switch (item.getItemId()) {
                case R.id.calendar:
                    transaction.replace(R.id.content, new calendar_Fragment()).commit();
                    toolbar.setTitle(R.string.calendar);
                    return true;
                case R.id.event_anlegen:
                    transaction.replace(R.id.content, new event_Anlegen_Fragment()).commit();
                    toolbar.setTitle(R.string.create_event);
                    return true;
                case R.id.events:
                    transaction.replace(R.id.content, new events_Fragment()).commit();
                    toolbar.setTitle(R.string.events);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_uebersicht);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.calendar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aktionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //Spracherkennung
 /*       if (id == R.id.impressum) {
            Intent speechRecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
            startActivityForResult(speechRecognitionIntent, SPEECHINTENT_REQ_CODE);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (requestCode == SPEECHINTENT_REQ_CODE && resultCode == RESULT_OK) {
            ArrayList<String> speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //Toast.makeText(this, speechResults.get(0), Toast.LENGTH_SHORT).show();
            String test = speechResults.get(0);

            if (test.equals("Event anlegen")) {
                transaction.replace(R.id.content, new event_Anlegen_Fragment()).commit();
                toolbar.setTitle(R.string.create_event);
                navigation.setSelectedItemId(R.id.event_anlegen);
            } else {
                // Toast.makeText(this, "nicht gefunden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
