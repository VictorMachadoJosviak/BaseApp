package com.example.victor.baseapp;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.pm.ResolveInfo;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private ListView mlvTextMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkVoiceRecognition();
                speak();
            }
        }, 0, 10000);
    }

    public void checkVoiceRecognition() {

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {

            Toast.makeText(this, "Voice recognizer not present",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());


        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);


           intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

           startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            if(resultCode == RESULT_OK) {

                 ArrayList<String> textMatchList = data
                 .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                 if (!textMatchList.isEmpty()) {

                      if (textMatchList.get(0).contains("search")) {

                             String searchQuery = textMatchList.get(0);
                                                                searchQuery = searchQuery.replace("search","");
                             Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                             search.putExtra(SearchManager.QUERY, searchQuery);
                             startActivity(search);
                          } else {

                              mlvTextMatches
                           .setAdapter(new ArrayAdapter<String>(this,
                                     android.R.layout.simple_list_item_1,
                                     textMatchList));
                          }

                     }

                }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
                 showToastMessage("Audio Error");
                }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
                 showToastMessage("Client Error");
                }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
                 showToastMessage("Network Error");
                }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
                 showToastMessage("No Match");
                }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
                 showToastMessage("Server Error");
                }
           super.onActivityResult(requestCode, resultCode, data);
          }
              void showToastMessage(String message){
           Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
          }
}
