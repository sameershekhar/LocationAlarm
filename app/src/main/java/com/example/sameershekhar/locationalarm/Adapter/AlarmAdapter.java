package com.example.sameershekhar.locationalarm.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.Fontcache;
import com.example.sameershekhar.locationalarm.MainActivity;
import com.example.sameershekhar.locationalarm.R;
import com.example.sameershekhar.locationalarm.data.Alarm;
import com.example.sameershekhar.locationalarm.data.DataBaseHelper;

import java.util.List;

/**
 * Created by sameershekhar on 09-Mar-18.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private List<Alarm> alarmList;
    private DataBaseHelper dataBaseHelper;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location, description, time;
        public ImageView delete;
        public CheckBox checkBox;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            Typeface face1 = Fontcache.get("fonts/lattoregular.ttf", itemView.getContext());
            location = (TextView) view.findViewById(R.id.location);
            location.setTypeface(face1);
            Typeface face2 = Fontcache.get("fonts/lattolight.ttf", itemView.getContext());
            description = (TextView) view.findViewById(R.id.description);
            description.setTypeface(face2);
            time = (TextView) view.findViewById(R.id.time);
            checkBox=(CheckBox)view.findViewById(R.id.checkbox);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            //delete=(ImageView) view.findViewById(R.id.deletebutton);
        }
    }


    public AlarmAdapter(List<Alarm> alarmList, DataBaseHelper dataBaseHelper, Context context) {
        this.alarmList = alarmList;
        this.dataBaseHelper=dataBaseHelper;
        this.context=context;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Alarm alarm= alarmList.get(position);
        holder.location.setText(alarm.getLocation());
        holder.description.setText("Description: "+ alarm.getDescription());
        holder.time.setText(alarm.getDateTime());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(isChecked)
                     MainActivity.checkedAlarm.add(0,alarm);
                 else
                     MainActivity.checkedAlarm.remove(alarm);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
    public void removeItem(int pos) {
        dataBaseHelper.deleteContact(alarmList.get(pos).getLocation());
        MainActivity.checkedAlarm.remove(alarmList.get(pos));
        alarmList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void restoreItem(Alarm alarm, int pos) {
        alarmList.add(pos, alarm);
        // notify item added by position
        dataBaseHelper.addAlarm(alarmList.get(pos));
        notifyItemInserted(pos);
    }
}
