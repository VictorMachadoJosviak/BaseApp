package com.example.victor.baseapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Victor on 20/03/2017.
 */

public class ServiceBase extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Serviço startado", Toast.LENGTH_LONG).show();

        Intent intent1 = new Intent(this,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent1);

        return super.onStartCommand(intent, flags, startId);
    }




}
