package com.example.sameershekhar.locationalarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.Services.MyService;
import com.example.sameershekhar.locationalarm.data.Alarm;
import com.example.sameershekhar.locationalarm.data.DataBaseHelper;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int pos;
    private TextView textView;
    private String dest;
    private EditText alarmLocation;
    private EditText alarmDescription;
    public static DataBaseHelper dataBaseHelper;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static List<Alarm> alarmArrayList=new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    public static  List<Address> addresses;
    public static LatLng origin;
    public static List<Alarm> checkedAlarm=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startService(new Intent(this, MyService.class));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        DestinationFragment destination = new DestinationFragment();
        viewPagerAdapter.addFragments(destination, "Alarms");
        viewPagerAdapter.addFragments(new MapFragment(), "CurrentLocation");
        viewPager.setAdapter(viewPagerAdapter);
         ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

            int currentPosition = 0;

            @Override
            public void onPageSelected(int newPosition) {

                FragmentLifecycle fragmentToShow = (FragmentLifecycle)viewPagerAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle)viewPagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = newPosition;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            public void onPageScrollStateChanged(int arg0) { }
        };
        viewPager.setOnPageChangeListener(pageChangeListener);
        tabLayout.setupWithViewPager(viewPager);



        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MainActivity.this,TakeInput.class));

            }
        });


            }




}



