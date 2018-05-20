package com.example.sameershekhar.locationalarm;

/**
 * Created by sameershekhar on 17-Mar-18.
 */

import com.example.sameershekhar.locationalarm.POJO.Example;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by navneet on 17/7/16.
 */
public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/directions/json?key=AIzaSyBVlKnLIjspl6T494UKmUuO74RDmf0FPD4")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}


/*<de.hdodenhof.circleimageview.CircleImageView
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:id="@+id/deletebutton"
            android:layout_below="@+id/time"
            android:layout_marginTop="@dimen/mg10dp"
            android:layout_marginRight="@dimen/mg10dp"
            android:layout_alignParentRight="true"
            android:layout_width="24dp"
            android:layout_height="24dp"
            android:src="@drawable/delete"
            app:civ_border_width="2dp"
            app:civ_border_color="#3ea416"/>*/
