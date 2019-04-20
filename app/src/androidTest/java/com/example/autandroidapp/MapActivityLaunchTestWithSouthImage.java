package com.example.autandroidapp;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MapActivityLaunchTestWithSouthImage {

    @Rule
    public ActivityTestRule<MapActivity> mapActivityTestRule = new ActivityTestRule<MapActivity>(MapActivity.class);

    private MapActivity mapActivity = null;

    @Before
    public void setUp() throws Exception {
        mapActivity = mapActivityTestRule.getActivity();
    }

    @Test
    // Launching a mapActivity, with map images of city, north, and south campuses
    public void mapLaunch(){
        View view = mapActivity.findViewById(R.id.imageViewSouthCampus);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mapActivity = null;
    }
}