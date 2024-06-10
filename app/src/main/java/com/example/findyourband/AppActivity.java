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
import androidx.navigation.NavOptions;
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

        if (savedInstanceState != null) {
            String currentFragmentTag = savedInstanceState.getString("currentFragmentTag");
            if (currentFragmentTag != null) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.app_fragment_container, currentFragment, currentFragmentTag)
                            .commit();
                }
            }
        }
        else{
            newsRecyclerView = findViewById(R.id.newsRecyclerView);
            if (newsRecyclerView != null) {
                newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
            try{
                new ParserInBackground(getApplicationContext()).execute();
            }
            catch (Exception e){

            }


        }

        // Получение данных из бд и кеширование их в SharedPreferences
        writeLoginFromFirebaseToSharedPreferences();
        loadDataIsUserBandLeader();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.app_fragment_container);
        navController = navHostFragment.getNavController();
        BottomNavigationView navView = binding.navView;

        NavigationUI.setupWithNavController(navView, navController);

        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.from_left)
                .setExitAnim(R.anim.to_right)
                .setPopEnterAnim(R.anim.from_right)
                .setPopExitAnim(R.anim.to_left)
                .build();

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                navController.popBackStack();
                navController.navigate(R.id.navigation_home, null, navOptions);
            } else if (id == R.id.navigation_vacancy) {
                navController.navigate(R.id.navigation_vacancy, null, navOptions);
            } else if (id == R.id.navigation_my_account_settings) {
                navController.navigate(R.id.navigation_my_account_settings, null, navOptions);
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
                TextView welcomeTextView = findViewById(R.id.welcomeTextView);
                if (welcomeTextView != null) {
                    welcomeTextView.setText("Добро пожаловать,\n" + login + "!");
                }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.app_fragment_container);
        if (currentFragment != null) {
            outState.putString("currentFragmentTag", currentFragment.getTag());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String currentFragmentTag = savedInstanceState.getString("currentFragmentTag");
        if (currentFragmentTag != null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.app_fragment_container, currentFragment, currentFragmentTag)
                        .commit();
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
