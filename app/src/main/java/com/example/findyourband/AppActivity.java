package com.example.findyourband;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.databinding.ActivityAppBinding;
import com.example.findyourband.services.ParserInBackground;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppActivity extends AppCompatActivity {

    private ActivityAppBinding binding;
    public static NavController navController;
    private RecyclerView newsRecyclerView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получение данных из бд и кеширование их в SharedPreferences
        writeLoginFromFirebaseToSharedPreferences();
        loadDataIsUserBandLeader();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.app_fragment_container);
        navController = navHostFragment.getNavController();
        BottomNavigationView navView = binding.navView;

        NavigationUI.setupWithNavController(navView, navController);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        if (newsRecyclerView != null) {
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        //new ParserInBackground(getApplicationContext()).execute();

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
            } else if (id == R.id.navigation_vacancy) {
                navController.navigate(R.id.navigation_vacancy);
            } else if (id == R.id.navigation_my_account_settings) {
                navController.navigate(R.id.navigation_my_account_settings);
            }
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void writeLoginFromFirebaseToSharedPreferences() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String login = dataSnapshot.child("login").getValue(String.class);
                ((TextView) findViewById(R.id.welcomeTextView)).setText("Добро пожаловать,\n" + login + "!");
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
