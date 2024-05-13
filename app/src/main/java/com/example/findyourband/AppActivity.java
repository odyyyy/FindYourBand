package com.example.findyourband;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.example.findyourband.fragments.MainPageFragment;
import com.example.findyourband.fragments.MyAccountSettingsFragment;
import com.example.findyourband.fragments.VacancyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.findyourband.databinding.ActivityAppBinding;

import java.util.List;

public class AppActivity extends AppCompatActivity {
    private final static String TAG = "Logging";
    private ActivityAppBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;


        NavController navController = Navigation.findNavController(this, R.id.app_fragment_container);
        NavigationUI.setupWithNavController(navView, navController);


        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {

                MainPageFragment fragment = new MainPageFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment,
                        "MainPageFragment").addToBackStack(null).commit();

                navController.navigate(R.id.navigation_home);
            } else if (id == R.id.navigation_vacancy) {

                VacancyPageFragment fragment = new VacancyPageFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.app_fragment_container, fragment,
                        "VacancyPageFragment").addToBackStack(null).commit();
                navController.navigate(R.id.navigation_vacancy);

            } else if (id == R.id.navigation_my_account_settings) {
                MyAccountSettingsFragment fragment = new MyAccountSettingsFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.app_fragment_container, fragment,
                        "MyAccountSettingsFragment").addToBackStack(null).commit();

                navController.navigate(R.id.navigation_my_account_settings);

            }
            return true;

        });
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO: Сохранение последнего фрагмента в Bundle
        Log.d("Logging", "Saving fragment: " + "start");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.app_fragment_container);
        if (currentFragment != null) {
            Log.d("Logging", "Saving fragment: " + currentFragment.getTag());
            outState.putString("fragment", currentFragment.getTag());
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Logging", "Restoring fragment: " + "start");
        Log.d("Logging", String.valueOf(savedInstanceState));
        if (savedInstanceState.containsKey("fragment")) {
            String fragmentTag = savedInstanceState.getString("fragment");
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null) {
                Log.d("Logging", "Restoring fragment3 : " + fragmentTag);
                Log.d("Logging", "Restoring fragment 4: " + fragment);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, fragmentTag).commit();
            }
        }

    }
}
