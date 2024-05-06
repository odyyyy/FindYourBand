package com.example.findyourband;

import android.os.Bundle;
import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.findyourband.databinding.ActivityAppBinding;

public class AppActivity extends AppCompatActivity {

    private ActivityAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = binding.navView;


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_app);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                MainPageFragment fragment = new MainPageFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment,
                        "MainPageFragment").addToBackStack(null).commit();
                navController.navigate(R.id.navigation_home);
            } else if (id == R.id.navigation_vacancy) {
                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment,
                        "VacancyPageFragment").addToBackStack(null).commit();
                navController.navigate(R.id.navigation_vacancy);
            } else if (id == R.id.navigation_my_account_settings) {
                MyAccountSettingsFragment fragment = new MyAccountSettingsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment,
                        "MyAccountSettingsFragment").addToBackStack(null).commit();
                navController.navigate(R.id.navigation_my_account_settings);

            }


            return true;

        });
    }

}