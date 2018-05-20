package com.example.sameershekhar.locationalarm;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.Adapter.AlarmAdapter;
import com.example.sameershekhar.locationalarm.Adapter.RecyclerItemTouchHelper;
import com.example.sameershekhar.locationalarm.data.Alarm;
import com.example.sameershekhar.locationalarm.data.DataBaseHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DestinationFragment extends Fragment implements FragmentLifecycle,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static List<Alarm> alarmList = new ArrayList<>();
    private RecyclerView recyclerView;
    public static AlarmAdapter mAdapter;
    private TextView setAlarm;
    private DataBaseHelper dataBaseHelper;
    private RelativeLayout rl;

    public DestinationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        rl=(RelativeLayout)view.findViewById(R.id.cordinator_id);
        setAlarm=(TextView)view.findViewById(R.id.setAlarm);
        dataBaseHelper = new DataBaseHelper(getContext());
        alarmList = dataBaseHelper.getAllAlarms();
        //Log.v("Aarmlist",alarmList.toString());
        //Toast.makeText(getContext(),"last loc "+alarmList.get(0).getLocation(),Toast.LENGTH_LONG).show();
        //Collections.reverse(alarmList);
        if(alarmList.size()==0)
        {
            Typeface face1 = Fontcache.get("fonts/lattoregular.ttf", getContext());
            setAlarm.setTypeface(face1);
            setAlarm.setVisibility(View.VISIBLE);
        }
        else {
            setAlarm.setVisibility(View.INVISIBLE);
        }
        mAdapter = new AlarmAdapter(alarmList, dataBaseHelper, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);



    }

    @Override
    public void onPauseFragment() {
        Log.i("TAG", "onPauseFragment()");

    }

    @Override
    public void onResumeFragment() {
        Log.i("TAG", "onResumeFragment()");

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AlarmAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String location = alarmList.get(viewHolder.getAdapterPosition()).getLocation();

            // backup of removed item for undo purpose
            final Alarm deletedItem = alarmList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(rl, location + " removed from alarm list ", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

