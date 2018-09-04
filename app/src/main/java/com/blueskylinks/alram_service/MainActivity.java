package com.blueskylinks.alram_service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import in.ac.srmuniv.alarmservice.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private PendingIntent pendingIntent;
    GregorianCalendar calendar;
    Button b1;
    AlarmManager alarmManager;
    TextView messageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageText = (TextView) findViewById(R.id.textView);
        b1 = (Button) findViewById(R.id.button1);
      //  readMessage("/storage/sdcard/message.json");
    //    b1.setOnClickListener(new OnClickListener() {

         //   private boolean flag = true;

          /*  @Override
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                    b1.setText("Start update");
                    alarmManager.cancel(pendingIntent);
                } else {
                    flag = true;
                    b1.setText("Stop update");
                    alarmManager.setRepeating(AlarmManager.RTC,
                            calendar.getTimeInMillis(), 15000, pendingIntent);
                }
            }
        });*/
        calendar = (GregorianCalendar) Calendar.getInstance();
        Intent myIntent = new Intent(MainActivity.this, UpdatingService.class);
       // Messenger messenger = new Messenger(handler);
       /* myIntent.putExtra("MESSENGER", messenger);
        myIntent.setData(Uri
                .parse("http://10.0.2.2/message/message.json"));// nawinsandroidtutorial.site90.com/message
        myIntent.putExtra("urlpath",
                "http://10.0.2.2/message/message.json");*/
        startService(myIntent);
        pendingIntent = PendingIntent.getService(MainActivity.this, 0,
                myIntent, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                15000, pendingIntent);
        Log.i("...","alram Manager Service started");
        Toast.makeText(this, "alram Manager Service started", Toast.LENGTH_SHORT).show();
    }

    public void stop_service(View view){
        Intent myIntent = new Intent(MainActivity.this, UpdatingService.class);
        alarmManager.cancel(pendingIntent);
        stopService(myIntent);
    }
   /* void readMessage(String path) {
        File myFile = new File(path.toString());// Environment.getExternalStorageDirectory().getPath()+"/message.json");
        FileInputStream fIn;
        try {
            fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(
                    fIn));
            String aDataRow = "";
            String aBuffer = "";

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;

            }
            myReader.close();
            try {
                JSONObject jObj = new JSONObject(aBuffer);
                aBuffer = jObj.getString("message");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            messageText.setText(aBuffer);
        } catch (FileNotFoundException e) {
            messageText.setText("No file");
            e.printStackTrace();
        } catch (IOException e) {
            messageText.setText("IO Error");
            e.printStackTrace();
        }
    }*/
}
