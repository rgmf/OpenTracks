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

package de.dennisguse.opentracks.fragments;

import android.location.Location;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dennisguse.opentracks.TrackStubUtils;
import de.dennisguse.opentracks.chart.ChartPoint;
import de.dennisguse.opentracks.chart.ChartView;
import de.dennisguse.opentracks.content.data.TrackPoint;
import de.dennisguse.opentracks.content.sensor.SensorDataSet;
import de.dennisguse.opentracks.util.UnitConversions;

/**
 * Tests {@link ChartFragment}.
 *
 * @author Youtao Liu
 */
@RunWith(AndroidJUnit4.class)
public class ChartFragmentTest {

    private static final double HOURS_PER_UNIT = 60.0;

    private ChartFragment chartFragment;

    @BeforeClass
    public static void preSetUp() {
        // Prepare looper for Android's message queue
        if (Looper.myLooper() == null) Looper.prepare();
    }

    @AfterClass
    public static void finalTearDown() {
        if (Looper.myLooper() != null) Looper.myLooper().quit();
    }

    @Before
    public void setUp() {
        boolean chartByDistance = false;
        chartFragment = (ChartFragment) ChartFragment.newInstance(chartByDistance);
        chartFragment.setChartView(new ChartView(ApplicationProvider.getApplicationContext(), chartByDistance));
        chartFragment.setTrackStatisticsUpdater(TrackStubUtils.INITIAL_TIME);
    }

    /**
     * Tests the logic to get the incorrect values of sensor in {@link ChartFragment#createPendingPoint(TrackPoint)}.
     */
    @Test
    public void testCreatePendingPoint_sensorIncorrect() {
        TrackPoint trackPoint = TrackStubUtils.createDefaultTrackPoint();

        // No input.
        ChartPoint point = chartFragment.createPendingPoint(trackPoint);
        Assert.assertEquals(Float.NaN, point.getHeartRate(), 0.01);
        Assert.assertEquals(Float.NaN, point.getCadence(), 0.01);
        Assert.assertEquals(Float.NaN, point.getPower(), 0.01);

        // Input incorrect state.
        // Creates SensorData.
        SensorDataSet sensorDataSet = new SensorDataSet(SensorDataSet.DATA_UNAVAILABLE, SensorDataSet.DATA_UNAVAILABLE);
        trackPoint.setSensorDataSet(sensorDataSet);
        // Test.
        point = chartFragment.createPendingPoint(trackPoint);
        Assert.assertEquals(Float.NaN, point.getHeartRate(), 0.01);
        Assert.assertEquals(Float.NaN, point.getCadence(), 0.01);
        Assert.assertEquals(Float.NaN, point.getPower(), 0.01);
    }

    /**
     * Tests the logic to get the correct values of sensor in {@link ChartFragment#createPendingPoint(TrackPoint)}.
     */
    @Test
    public void testCreatePendingPoint_sensorCorrect() {
        TrackPoint trackPoint = TrackStubUtils.createDefaultTrackPoint();
        // No input.
        ChartPoint point = chartFragment.createPendingPoint(trackPoint);
        Assert.assertEquals(Float.NaN, point.getHeartRate(), 0.01);
        Assert.assertEquals(Float.NaN, point.getCadence(), 0.01);
        Assert.assertEquals(Float.NaN, point.getPower(), 0.01);

        // Creates SensorData.
        SensorDataSet sensorDataSet = new SensorDataSet(100, 101, 102);

        // Creates SensorDataSet.
        trackPoint.setSensorDataSet(sensorDataSet);
        // Test.
        point = chartFragment.createPendingPoint(trackPoint);
        Assert.assertEquals(100.0, point.getHeartRate(), 0.01);
        Assert.assertEquals(101.0, point.getCadence(), 0.01);
        Assert.assertEquals(102.0, point.getPower(), 0.01);
    }

    /**
     * Tests the logic to get the value of metric Distance in {@link ChartFragment#createPendingPoint(TrackPoint)}.
     */
    @Test
    public void testCreatePendingPoint_distanceMetric() {
        chartFragment.setChartByDistance(true);
        // Resets last location and writes first location.
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(0.0, point.getTimeOrDistance(), 0.01);

        // The second is a same location, just different time.
        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();
        point = chartFragment.createPendingPoint(trackPoint2);
        Assert.assertEquals(0.0, point.getTimeOrDistance(), 0.01);

        // The third location is a new location, and use metric.
        TrackPoint trackPoint3 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint3.setLatitude(23);
        point = chartFragment.createPendingPoint(trackPoint3);

        // Computes the distance between Latitude 22 and 23.
        float[] results = new float[4];
        Location.distanceBetween(trackPoint2.getLatitude(), trackPoint2.getLongitude(),
                trackPoint3.getLatitude(), trackPoint3.getLongitude(), results);
        double distance1 = results[0] * UnitConversions.M_TO_KM;
        Assert.assertEquals(distance1, point.getTimeOrDistance(), 0.01);

        // The fourth location is a new location, and use metric.
        TrackPoint trackPoint4 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint4.setLatitude(24);
        point = chartFragment.createPendingPoint(trackPoint4);

        // Computes the distance between Latitude 23 and 24.
        Location.distanceBetween(trackPoint3.getLatitude(), trackPoint3.getLongitude(),
                trackPoint4.getLatitude(), trackPoint4.getLongitude(), results);
        double distance2 = results[0] * UnitConversions.M_TO_KM;
        Assert.assertEquals((distance1 + distance2), point.getTimeOrDistance(), 0.01);
    }

    /**
     * Tests the logic to get the value of imperial Distance in {@link ChartFragment#createPendingPoint(TrackPoint)}.
     */
    @Test
    public void testCreatePendingPoint_distanceImperial() {
        // By distance.
        chartFragment.setChartByDistance(true);
        // Setups to use imperial.
        chartFragment.setMetricUnits(false);

        // The first is a same location, just different time.
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(0.0, point.getTimeOrDistance(), 0.01);

        // The second location is a new location, and use imperial.
        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint2.setLatitude(23);
        point = chartFragment.createPendingPoint(trackPoint2);

        /*
         * Computes the distance between Latitude 22 and 23.
         * And for we set using * imperial, the distance should be multiplied by UnitConversions.KM_TO_MI.
         */
        float[] results = new float[4];
        Location.distanceBetween(trackPoint1.getLatitude(), trackPoint1.getLongitude(), trackPoint2.getLatitude(), trackPoint2.getLongitude(), results);
        double distance1 = results[0] * UnitConversions.M_TO_KM * UnitConversions.KM_TO_MI;
        Assert.assertEquals(distance1, point.getTimeOrDistance(), 0.01);

        // The third location is a new location, and use imperial.
        TrackPoint trackPoint3 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint3.setLatitude(24);
        point = chartFragment.createPendingPoint(trackPoint3);

        /*
         * Computes the distance between Latitude 23 and 24.
         * And for we set using * imperial, the distance should be multiplied by UnitConversions.KM_TO_MI.
         */
        Location.distanceBetween(trackPoint2.getLatitude(), trackPoint2.getLongitude(), trackPoint3.getLatitude(), trackPoint3.getLongitude(), results);
        double distance2 = results[0] * UnitConversions.M_TO_KM * UnitConversions.KM_TO_MI;
        Assert.assertEquals(distance1 + distance2, point.getTimeOrDistance(), 0.01);
    }

    /**
     * Tests the logic to get the values of time in {@link ChartFragment#createPendingPoint(TrackPoint)}.
     */
    @Test
    public void testCreatePendingPoint_time() {
        chartFragment.setChartByDistance(false);
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(0.0, point.getTimeOrDistance(), 0.01);
        long timeSpan = 222;
        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint2.setTime(trackPoint1.getTime() + timeSpan);
        point = chartFragment.createPendingPoint(trackPoint2);
        Assert.assertEquals((double) timeSpan, point.getTimeOrDistance(), 0.01);
    }

    /**
     * Tests the logic to get the value of elevation in {@link ChartFragment#createPendingPoint(TrackPoint)} by one and two points.
     */
    @Test
    public void testCreatePendingPoint_elevation() {
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();

        /*
         * At first, clear old points of elevation, so give true to the second parameter.
         * Then only one value INITIAL_ALTITUDE in buffer.
         */
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(TrackStubUtils.INITIAL_ALTITUDE, point.getElevation(), 0.01);

        /*
         * Send another value to buffer, now there are two values, INITIAL_ALTITUDE and INITIAL_ALTITUDE * 2.
         */
        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint2.setAltitude(TrackStubUtils.INITIAL_ALTITUDE * 2);
        point = chartFragment.createPendingPoint(trackPoint2);
        Assert.assertEquals((TrackStubUtils.INITIAL_ALTITUDE + TrackStubUtils.INITIAL_ALTITUDE * 2) / 2.0, point.getElevation(), 0.01);
    }

    /**
     * Tests the logic to get the value of speed in {@link ChartFragment#createPendingPoint(TrackPoint)}.
     * In this test, firstly remove all points in memory, and then fill in two points one by one.
     * The speed values of these points are 129, 130.
     */
    @Test
    public void testCreatePendingPoint_speed() {
        /*
         * At first, clear old points of speed, so give true to the second parameter.
         * It will not be filled in to the speed buffer.
         */
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint1.setSpeed(128.5f);
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(0.0, point.getSpeed(), 0.01);

        /*
         * Tests the logic when both metricUnits and reportSpeed are true.
         * This location will be filled into speed buffer.
         */
        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();

        /*
         * Add a time span here to make sure the second point is valid, the value 222 here is doesn't matter.
         */
        trackPoint2.setTime(trackPoint1.getTime() + 222);
        trackPoint2.setSpeed(130);
        point = chartFragment.createPendingPoint(trackPoint2);
        Assert.assertEquals(130.0 * UnitConversions.MS_TO_KMH, point.getSpeed(), 0.01);
    }

    /**
     * Tests the logic to compute speed when use Imperial.
     */
    @Test
    public void testCreatePendingPoint_speedImperial() {
        chartFragment.setMetricUnits(false);

        // First data point is not added to the speed buffer
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint1.setSpeed(100.0f);
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(0.0, point.getSpeed(), 0.01);

        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();

        /*
         * Add a time span here to make sure the second point and the speed is valid.
         * Speed is valid if: speedDifference > Constants.MAX_ACCELERATION * timeDifference speedDifference = 102 -100 timeDifference = 222
         */
        trackPoint2.setTime(trackPoint2.getTime() + 222);
        trackPoint2.setSpeed(102);
        point = chartFragment.createPendingPoint(trackPoint2);
        Assert.assertEquals(102.0 * UnitConversions.MS_TO_KMH * UnitConversions.KM_TO_MI, point.getSpeed(), 0.01);
    }

    /**
     * Tests the logic to get pace value when reportSpeed is false.
     */
    @Test
    public void testCreatePendingPoint_pace_nonZeroSpeed() {
        chartFragment.setReportSpeed(false);

        // First data point is not added to the speed buffer
        TrackPoint trackPoint1 = TrackStubUtils.createDefaultTrackPoint();
        trackPoint1.setSpeed(100.0f);
        ChartPoint point = chartFragment.createPendingPoint(trackPoint1);
        Assert.assertEquals(0.0, point.getSpeed(), 0.01);

        TrackPoint trackPoint2 = TrackStubUtils.createDefaultTrackPoint();

        /*
         * Add a time span here to make sure the second point and the speed is valid.
         * Speed is valid if: speedDifference > Constants.MAX_ACCELERATION * timeDifference speedDifference = 102 -100 timeDifference = 222
         */
        trackPoint2.setTime(trackPoint2.getTime() + 222);
        trackPoint2.setSpeed(102);
        point = chartFragment.createPendingPoint(trackPoint2);
        Assert.assertEquals(HOURS_PER_UNIT / (102.0 * UnitConversions.MS_TO_KMH), point.getPace(), 0.01);
    }

    /**
     * Tests the logic to get pace value when reportSpeed is false and average speed is zero.
     */
    @Test
    public void testCreatePendingPoint_pace_zeroSpeed() {
        chartFragment.setReportSpeed(false);
        TrackPoint trackPoint = TrackStubUtils.createDefaultTrackPoint();
        trackPoint.setSpeed(0);
        ChartPoint point = chartFragment.createPendingPoint(trackPoint);
        Assert.assertEquals(0.0, point.getPace(), 0.01);
    }
}
