package de.dennisguse.opentracks.util;

import android.content.Context;

import de.dennisguse.opentracks.R;

public class ActivityTypeUtils {
    static final String AIRPLANE = "AIRPLANE";
    static final String BIKE = "BIKE";
    static final String BOAT = "BOAT";
    static final String DRIVE = "DRIVE";
    static final String RUN = "RUN";
    static final String SKI = "SKI";
    static final String SNOW_BOARDING = "SNOW_BOARDING";
    static final String UNKNOWN = "UNKNOWN";
    static final String WALK = "WALK";

    static final int[] AIRPLANE_LIST = new int[]{R.string.activity_type_airplane, R.string.activity_type_commercial_airplane, R.string.activity_type_rc_airplane};
    static final int[] BIKE_LIST = new int[]{R.string.activity_type_biking, R.string.activity_type_cycling, R.string.activity_type_dirt_bike, R.string.activity_type_motor_bike, R.string.activity_type_mountain_biking, R.string.activity_type_road_biking, R.string.activity_type_track_cycling};
    static final int[] BOAT_LIST = new int[]{R.string.activity_type_boat, R.string.activity_type_ferry, R.string.activity_type_motor_boating, R.string.activity_type_rc_boat};
    static final int[] DRIVE_LIST = new int[]{R.string.activity_type_atv, R.string.activity_type_driving, R.string.activity_type_driving_bus, R.string.activity_type_driving_car};
    static final int[] RUN_LIST = new int[]{R.string.activity_type_running, R.string.activity_type_street_running, R.string.activity_type_track_running, R.string.activity_type_trail_running};
    static final int[] SKI_LIST = new int[]{R.string.activity_type_cross_country_skiing, R.string.activity_type_skiing};
    static final int[] SNOW_BOARDING_LIST = new int[]{R.string.activity_type_snow_boarding};
    static final int[] WALK_LIST = new int[]{R.string.activity_type_hiking, R.string.activity_type_off_trail_hiking, R.string.activity_type_speed_walking, R.string.activity_type_trail_hiking, R.string.activity_type_walking};

    // List of sports that use speed (in km/h or mi/h) by default.
    static final int[] SPEED_SPORTS = {
            // All airplane categories.
            R.string.activity_type_airplane, R.string.activity_type_commercial_airplane, R.string.activity_type_rc_airplane,
            // All bike categories.
            R.string.activity_type_biking, R.string.activity_type_cycling, R.string.activity_type_dirt_bike, R.string.activity_type_motor_bike, R.string.activity_type_mountain_biking, R.string.activity_type_road_biking, R.string.activity_type_track_cycling,
            // All boat categories.
            R.string.activity_type_boat, R.string.activity_type_ferry, R.string.activity_type_motor_boating, R.string.activity_type_rc_boat,
            // All drive categories.
            R.string.activity_type_atv, R.string.activity_type_driving, R.string.activity_type_driving_bus, R.string.activity_type_driving_car,

    };

    /**
     * Returns true if category (activity type) is in the SPEED_SPORTS array.
     * Otherwise returns false.
     *
     * @param context  the context.
     * @param category the name of the category, activity type.
     */
    public static boolean isSpeedSport(Context context, String category) {
        for (int i : SPEED_SPORTS) {
            if (context.getString(i).equals(category)) {
                return true;
            }
        }
        return false;
    }
}
