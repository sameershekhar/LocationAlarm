package com.example.sameershekhar.locationalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.Services.MyService;
import com.example.sameershekhar.locationalarm.data.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.sameershekhar.locationalarm.data.DataBaseHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static com.example.sameershekhar.locationalarm.MainActivity.dataBaseHelper;

/**
 * Created by sameershekhar on 14-Mar-18.
 */

public class TakeInput extends AppCompatActivity implements PlaceSelectionListener  {

    private String location;
    private LatLng lat;
    private EditText description;
    private Button button;
    private Toolbar toolbar;
    private DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet);
        final String LOG_TAG = "PlaceSelectionListener";
         final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Location");
        setSupportActionBar(toolbar);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());


        description=(EditText)findViewById(R.id.inputDescription);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search a Location");
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);


        button=(Button)findViewById(R.id.startTracking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/ hh:mm");
                String currentDateandTime = sdf.format(new Date());
                long milliSecond = Calendar.getInstance().getTimeInMillis();
                Alarm alarm = new Alarm();
                alarm.setLocation(location);
                alarm.setDescription(description.getText().toString());
                //alarm.setLatLng(lat);
                alarm.setDateTime(currentDateandTime);
                alarm.setMilliSecond(milliSecond);
                //DestinationFragment.alarmList.add(0, alarm);

                //DestinationFragment.mAdapter.notifyDataSetChanged();

                dataBaseHelper.addAlarm(alarm);
                //MainActivity.checkedAlarm.add(alarm);
                // dest = alarmLocation.getText().toString();
                //MainActivity.alarmArrayList=dataBaseHelper.getAllAlarms();

                //stopService(new Intent(TakeInput.this,MyService.class));
                startService(new Intent(TakeInput.this, MyService.class));
                Intent intent=new Intent(TakeInput.this,MainActivity.class);
                startActivity(intent);
               // Toast.makeText(getApplicationContext(),location.getText().toString()+ "  "+ description.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPlaceSelected(Place place) {
        location=place.getName().toString();
        lat=place.getLatLng();

        Log.i("LOG_TAG", "Place Selected: " + place.getName());
    }

    @Override
    public void onError(Status status) {
        Log.e("LOG_TAG", "onError: Status = " + status.toString());
    }
}


