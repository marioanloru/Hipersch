package com.tfg.hipersch;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements ScheduleFragment.OnFragmentInteractionListener,
        StatisticsFragment.OnFragmentInteractionListener,
        ManageTasksFragment.OnFragmentInteractionListener,
        TasksFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    public final static String SHARED_PREF_NAME = "hipersch.SHARED_PREF";
    public final static String CURRENT_MODE_KEY = "hipersch.CURRENT_MODE";

    @BindView(R.id.bottom_navigation) BottomNavigationView _bottomNavigation;
    @BindView(R.id.navigation_schedule) BottomNavigationItemView _navigationSchedule;
    @BindView(R.id.navigation_tasks) BottomNavigationItemView _navigationTasks;
    @BindView(R.id.navigation_profile) BottomNavigationItemView _navigationProfile;
    @BindView(R.id.navigation_statistics) BottomNavigationItemView _navigationStatistics;
    @BindView(R.id.currentModeGroup) MaterialButtonToggleGroup _currentModeGroup;
    @BindView(R.id.cyclingButtom) MaterialButton _cyclingButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setCurrentMode("running", true);

        //  Check if comes from athlete
        Intent intent = getIntent();
        try {
            String athlete = intent.getStringExtra(TrainerLogin.ATHLETE);
        } catch (Exception e) {
            System.out.println("No es un entrenador");
        }
        //  get token from app shared info
        showMainFragment();

        System.out.println("Getting user info");
        //  getUserData(TokenManager.getToken(this).toString());

        _currentModeGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
             @Override
             public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                 switch (checkedId) {
                     case R.id.cyclingButtom:
                         setCurrentMode("cycling", isChecked);
                         //updateFragment("cycling");
                         break;
                     case R.id.runningButtom:
                         setCurrentMode("running", isChecked);
                         //updateFragment("running");
                         break;
                     case R.id.swimmingButtom:
                         setCurrentMode("swimming", isChecked);
                         //updateFragment("swimming");
                         break;
                 }

                 getCurrentMode();
             }
         });

        _bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_schedule:
                        ScheduleFragment scheduleFragment = new ScheduleFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, scheduleFragment)
                                .commit();
                        setActivityTitle("Schedule");
                        break;

                    case R.id.navigation_tasks:
                        TasksFragment tasksFragment = new TasksFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, tasksFragment)
                                .commit();
                        setActivityTitle("Tasks");
                        break;

                    case R.id.navigation_profile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, profileFragment)
                                .commit();
                        setActivityTitle("Profile");
                        break;

                    case R.id.navigation_statistics:
                        StatisticsFragment statisticsFragment = new StatisticsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, statisticsFragment)
                                .commit();
                        setActivityTitle("Statistics");
                        break;
                }

                return true;
            }

        });
    }


    public void showMainFragment() {
        ProfileFragment fragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        getSupportActionBar().setTitle("Profile");
    }

    public void setCurrentMode(String mode, boolean isChecked) {
        System.out.println("--------Seteo currento mode: " + mode);
        if (isChecked) {
            SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(CURRENT_MODE_KEY, mode);
            editor.apply();
        }
    }

    public void setActivityTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public String getCurrentMode() {
        SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(CURRENT_MODE_KEY, "");
    }

    public String getToken() {
        return TokenManager.getToken(this);
    }

    public String getSharedPrefName() {
        return SHARED_PREF_NAME;
    }

    public String getCurrentModeKey() {
        return CURRENT_MODE_KEY;
    }


    @Override
    public void onBackPressed() {
        // Do nothing on back pressed at main activity
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
