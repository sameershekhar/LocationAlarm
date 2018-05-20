package com.example.sameershekhar.locationalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.Services.MyService;
import com.example.sameershekhar.locationalarm.data.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.sameershekhar.locationalarm.MainActivity.dataBaseHelper;

/**
 * Created by sameershekhar on 14-Mar-18.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private EditText location;
    private EditText description;
    private Button button;
    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.bottom_sheet, container, false);

        //location=(EditText)view.findViewById(R.id.inputLocation);
        description=(EditText)view.findViewById(R.id.inputDescription);

        button=(Button)view.findViewById(R.id.startTracking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/ hh:mm");
                String currentDateandTime = sdf.format(new Date());
                //Date date = new Date();
                //This method returns the time in millis
                long milliSecond = Calendar.getInstance().getTimeInMillis();
                Alarm alarm = new Alarm();
                alarm.setLocation(location.getText().toString());
                alarm.setDescription(description.getText().toString());
                alarm.setDateTime(currentDateandTime);
                alarm.setMilliSecond(milliSecond);
                //Log.d("Milli",milliSecond+" ");

                //Toast.makeText(getContext(),milliSecond+"",Toast.LENGTH_LONG).show();
                dataBaseHelper.addAlarm(alarm);

                //MainActivity.alarmArrayList=dataBaseHelper.getAllAlarms();
                //getContext().stopService(new Intent(getContext(),MyService.class));
                //getActivity().startService(new Intent(getContext(), MyService.class));
                //Toast.makeText(getContext(),location.getText().toString()+ "  "+ description.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}

