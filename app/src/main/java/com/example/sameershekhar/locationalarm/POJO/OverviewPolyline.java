package com.example.sameershekhar.locationalarm.POJO;

/**
 * Created by sameershekhar on 17-Mar-18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverviewPolyline {

    @SerializedName("points")
    @Expose
    private String points;

    /**
     *
     * @return
     * The points
     */
    public String getPoints() {
        return points;
    }

    /**
     *
     * @param points
     * The points
     */
    public void setPoints(String points) {
        this.points = points;
    }

}

