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

package de.dennisguse.opentracks;

import android.location.Location;

import de.dennisguse.opentracks.content.data.Track;
import de.dennisguse.opentracks.content.data.TrackPoint;
import de.dennisguse.opentracks.content.sensor.SensorDataSet;

/**
 * Commons utilities for creating stubs of track, location.
 *
 * @author Youtao Liu
 */
public class TrackStubUtils {

    public static final double INITIAL_ALTITUDE = 22;
    public static final long INITIAL_TIME = 1000L;
    private static final String LOCATION_PROVIDER = "gps";
    private static final double INITIAL_LATITUDE = 22;
    private static final double INITIAL_LONGITUDE = 22;
    private static final float INITIAL_ACCURACY = 5;
    private static final float INITIAL_SPEED = 10;
    private static final float INITIAL_BEARING = 3.0f;
    // Used to change the value of latitude, longitude, and altitude.
    private static final double DIFFERENCE = 0.01;

    /**
     * Gets a a {@link Track} stub with specified number of locations.
     *
     * @param numberOfLocations the number of locations for the track
     * @return a track stub.
     */
    public static Track createTrack(int numberOfLocations) {
        Track track = new Track();
        for (int i = 0; i < numberOfLocations; i++) {
            track.addTrackPoint(createDefaultTrackPoint(INITIAL_LATITUDE + i * DIFFERENCE, INITIAL_LONGITUDE + i * DIFFERENCE, INITIAL_ALTITUDE + i * DIFFERENCE));
        }

        return track;
    }

    /**
     * Create a MyTracks location with default values.
     *
     * @return a track stub.
     */
    public static TrackPoint createDefaultTrackPoint() {
        return createDefaultTrackPoint(INITIAL_LATITUDE, INITIAL_LONGITUDE, INITIAL_ALTITUDE);
    }

    /**
     * Creates a {@link TrackPoint} stub with specified values.
     *
     * @return a SensorDataSetLocation stub.
     */
    private static TrackPoint createDefaultTrackPoint(double latitude, double longitude, double altitude) {
        Location location = new Location(LOCATION_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAltitude(altitude);
        location.setAccuracy(INITIAL_ACCURACY);
        location.setSpeed(INITIAL_SPEED);
        location.setTime(INITIAL_TIME);
        location.setBearing(INITIAL_BEARING);

        return new TrackPoint(location, new SensorDataSet(Float.NaN, Float.NaN));
    }
}
