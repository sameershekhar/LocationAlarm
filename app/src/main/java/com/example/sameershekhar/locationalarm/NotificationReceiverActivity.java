package com.example.sameershekhar.locationalarm;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sameershekhar.locationalarm.Services.MyService;

public class NotificationReceiverActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView location;
    private TextView description;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Location");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        location = (TextView) findViewById(R.id.location);
        description = (TextView) findViewById(R.id.description);


            //location.setText(MyService.location);
            //description.setText(MyService.location);

        button = (Button) findViewById(R.id.stopTracking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(NotificationReceiverActivity.this, MyService.class));
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        String tabNumber;

        if(extras != null) {
            tabNumber = extras.getString("screen");
            location.setText(tabNumber);

        } else {
            Log.d("TEMP", "Extras are NULL");

        }
    }


}
