package com.blueskylinks.alram_service;

/**
 * Created by omsai on 8/30/2018.
 */
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class UpdatingService extends Service {
   MqttClient sampleClient;
    private MqttConnectOptions options;
    MqttConnectOptions mqttConnectOptions;
    MqttMessage Mmessage;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "hi there", Toast.LENGTH_LONG).show();
        mqtt_sub();
        return START_STICKY;
    }


    public void mqtt_sub()  {

        MqttCallback mqtt_callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //t1.setText(mqttMessage.toString());
                Intent intent = new Intent();
                intent.setAction("CUSTOM_INTENT");
                intent.putExtra("D1", mqttMessage.toString());

                sendBroadcast(intent);

                String s1= mqttMessage.toString();


                Log.i(s,mqttMessage.toString());
                if(s1!=null) {
                    ToneGenerator toneGenerator1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGenerator1.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING, 5000);
                }
                Toast.makeText(getApplicationContext(),"message", Toast.LENGTH_LONG).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        };

        String broker       = "tcp://13.126.9.228:1883";
        String clientId     = " ";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            Log.i("Connecting to broker: ", broker);
            sampleClient.connect(connOpts);
            Log.i("Connected", "C");
            sampleClient.setCallback(mqtt_callback);
            sampleClient.subscribe("home");


        } catch(MqttException me) {
            Log.i("reason ",String.valueOf(me.getReasonCode()));
            Log.i("msg ",String.valueOf(me.getMessage()));
            Log.i("loc ",String.valueOf(me.getLocalizedMessage()));
            Log.i("cause ",String.valueOf(me.getCause()));
            Log.i("excep ",String.valueOf(me));
            me.printStackTrace();
        }

    }

    public class MyReceiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            //    b1=findViewById(R.id.button2);
            //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
            String s1 = arg1.getStringExtra("D1");

            Log.i("BLE,,,,,,,", s1);
        }
    }
    public void onDestroy() {
        Toast.makeText(UpdatingService.this, "service Destroyed......!", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
