/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.dennisguse.opentracks.util;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import de.dennisguse.opentracks.R;

/**
 * Utilities for track icon.
 *
 * @author Jimmy Shih
 */
public class TrackIconUtils {

    private static final int ACTIVITY_UNKNOWN_LOGO = R.drawable.ic_logo_white_24dp;

    private static final LinkedHashMap<String, Pair<Integer, Integer>> MAP = new LinkedHashMap<>();

    static {
        //Reflects order in ChooseActivityTypeDialogFragmentActivity
        MAP.put(ActivityTypeUtils.UNKNOWN, new Pair<>(R.string.activity_type_unknown, ACTIVITY_UNKNOWN_LOGO));
        MAP.put(ActivityTypeUtils.RUN, new Pair<>(R.string.activity_type_running, R.drawable.ic_activity_run_24dp));
        MAP.put(ActivityTypeUtils.WALK, new Pair<>(R.string.activity_type_walking, R.drawable.ic_activity_walk_24dp));
        MAP.put(ActivityTypeUtils.BIKE, new Pair<>(R.string.activity_type_biking, R.drawable.ic_activity_bike_24dp));
        MAP.put(ActivityTypeUtils.DRIVE, new Pair<>(R.string.activity_type_driving, R.drawable.ic_activity_drive_24dp));
        // TODO Need new icons
//        MAP.put(SKI, new Pair<>(R.string.activity_type_skiing, R.drawable.ic_track_ski));
//        MAP.put(SNOW_BOARDING, new Pair<>(R.string.activity_type_snow_boarding, R.drawable.ic_track_snow_boarding));
        MAP.put(ActivityTypeUtils.AIRPLANE, new Pair<>(R.string.activity_type_airplane, R.drawable.ic_activity_flight_24dp));
        MAP.put(ActivityTypeUtils.BOAT, new Pair<>(R.string.activity_type_boat, R.drawable.ic_activity_boat_24dp));
    }

    private TrackIconUtils() {
    }

    /**
     * Gets the icon drawable.
     *
     * @param iconValue the icon value
     */
    public static int getIconDrawable(String iconValue) {
        if (iconValue == null || iconValue.equals("")) {
            return ACTIVITY_UNKNOWN_LOGO;
        }
        Pair<Integer, Integer> pair = MAP.get(iconValue);
        return pair == null ? ACTIVITY_UNKNOWN_LOGO : pair.second;
    }

    /**
     * Gets the icon activity type.
     *
     * @param iconValue the icon value
     */
    public static int getIconActivityType(String iconValue) {
        if (iconValue == null || iconValue.equals("")) {
            return R.string.activity_type_unknown;
        }
        Pair<Integer, Integer> pair = MAP.get(iconValue);
        return pair == null ? R.string.activity_type_unknown : pair.first;
    }

    /**
     * Gets all icon values.
     */
    public static List<String> getAllIconValues() {
        return new ArrayList<>(MAP.keySet());
    }

    /**
     * Gets the icon value.
     *
     * @param context      the context
     * @param activityType the activity type
     */
    public static String getIconValue(Context context, String activityType) {
        if (activityType == null || activityType.equals("")) {
            return ActivityTypeUtils.UNKNOWN;
        }
        if (inList(context, activityType, ActivityTypeUtils.AIRPLANE_LIST)) {
            return ActivityTypeUtils.AIRPLANE;
        }
        if (inList(context, activityType, ActivityTypeUtils.BIKE_LIST)) {
            return ActivityTypeUtils.BIKE;
        }
        if (inList(context, activityType, ActivityTypeUtils.BOAT_LIST)) {
            return ActivityTypeUtils.BOAT;
        }
        if (inList(context, activityType, ActivityTypeUtils.DRIVE_LIST)) {
            return ActivityTypeUtils.DRIVE;
        }
        if (inList(context, activityType, ActivityTypeUtils.RUN_LIST)) {
            return ActivityTypeUtils.RUN;
        }
//        if (inList(context, activityType, SKI_LIST)) {
//            return SKI;
//        }
//        if (inList(context, activityType, SNOW_BOARDING_LIST)) {
//            return SNOW_BOARDING;
//        }
        if (inList(context, activityType, ActivityTypeUtils.WALK_LIST)) {
            return ActivityTypeUtils.WALK;
        }
        return ActivityTypeUtils.UNKNOWN;
    }

    public static void setIconSpinner(Spinner spinner, String iconValue) {
        ArrayAdapter<StringBuilder> adapter = (ArrayAdapter<StringBuilder>) spinner.getAdapter();
        StringBuilder stringBuilder = adapter.getItem(0);
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(iconValue);
        adapter.notifyDataSetChanged();
    }

    public static ArrayAdapter<StringBuilder> getIconSpinnerAdapter(final Context context, String iconValue) {
        return new ArrayAdapter<StringBuilder>(context, android.R.layout.simple_spinner_item, new StringBuilder[]{new StringBuilder(iconValue)}) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull android.view.ViewGroup parent) {
                ImageView imageView;
                if (convertView != null) {
                    imageView = (ImageView) convertView;
                } else {
                    imageView = new ImageView(getContext());
                }

                imageView.setImageResource(TrackIconUtils.getIconDrawable(getItem(position) + ""));

                int padding = ResourceUtils.dpToPx(getContext(), 1);
                imageView.setPaddingRelative(padding, padding, padding, -padding);
                return imageView;
            }
        };
    }

    /**
     * Returns true if the activity type is in the list.
     *
     * @param context      the context
     * @param activityType the activity type
     * @param list         the list
     */
    private static boolean inList(Context context, String activityType, int[] list) {
        for (int i : list) {
            if (context.getString(i).equals(activityType)) {
                return true;
            }
        }
        return false;
    }
}
