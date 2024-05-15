package com.example.findyourband;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findyourband.fragments.MainPageFragment;
import com.example.findyourband.fragments.settings.MyAccountSettingsFragment;
import com.example.findyourband.fragments.vacancies.VacancyPageFragment;
import com.example.findyourband.services.ParserInBackground;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.databinding.ActivityAppBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppActivity extends AppCompatActivity {
    private final static String TAG = "Logging";
    private ActivityAppBinding binding;

    String userId;

    private RecyclerView newsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Получение данных из бд и кеширование их в SharedPreferences

        writeLoginFromFirebaseToSharedPreferences();
        loadDataIsUserBandLeader();

        BottomNavigationView navView = binding.navView;


        NavController navController = Navigation.findNavController(this, R.id.app_fragment_container);
        NavigationUI.setupWithNavController(navView, navController);

        newsRecyclerView = binding.container.findViewById(R.id.app_fragment_container).findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        new ParserInBackground(getApplicationContext()).execute();


        // Слушатели событий на переход по вкладкам
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

    private void writeLoginFromFirebaseToSharedPreferences() {

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String login = dataSnapshot.child("login").getValue(String.class);
                ((TextView)binding.container.findViewById(R.id.app_fragment_container).findViewById(R.id.welcomeTextView)).setText("Добро пожаловать,\n" + login + "!");
                // Добавление логина в sharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("login", login);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AppActivity.this, "Ошибка при получении данных пользователя", Toast.LENGTH_SHORT).show();
            }
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

    private void loadDataIsUserBandLeader() {

        DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference().child("bands");

        bandsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("bandLeader", snapshot.exists());
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppActivity.this, "Ошибка при чтении данных! " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
