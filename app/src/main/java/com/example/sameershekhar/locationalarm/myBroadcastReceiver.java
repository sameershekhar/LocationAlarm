package com.example.sameershekhar.locationalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by sameershekhar on 28-Dec-16.
 */
public class myBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        mediaPlayer=MediaPlayer.create(context,R.raw.xyz);
        mediaPlayer.start();

    }
}
