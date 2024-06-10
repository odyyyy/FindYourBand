package com.example.findyourband.fragments.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.MainActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMyAccountSettingsBinding;
import com.example.findyourband.services.FirebaseQueriesServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountSettingsFragment extends Fragment {
    FragmentMyAccountSettingsBinding binding;
    String login;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyAccountSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setUserLoginInUpperBar();

        setButtonsIfUserIsBandLeader();

        binding.manageBandButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        binding.createBandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String login = preferences.getString("login", "Пользователь");

                FirebaseQueriesServices.isUserAlreadyInBand(login, new FirebaseQueriesServices.BandCheckCallback() {

                    @Override
                    public void onCheckCompleted(boolean isInBand) {
                        if (isInBand) {
                            Toast.makeText(getContext(), "Вы уже состоите в группе! Сначала покиньте её!", Toast.LENGTH_LONG).show();
                        } else {
                            AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_createBandFragment);
                        }
                    }
                });
            }
        });


        binding.myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_userProfileFragment);
            }
        });

        binding.myVacanciesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_myVacanciesFragment);

            }
        });

        binding.myRequestsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_myRequestsFragment);
            }
        });

//        binding.favoritesButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
        binding.settingsButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_settingsFragment);

            }
        });

        binding.aboutAppButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_aboutAppFragment);
            }
        });

        binding.instructionButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_instructionFragment);
            }
        });

        binding.aboutAuthorButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_aboutAuthorFragment);
            }
        });

        binding.signOutButtonAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Вы уверены что хотите выйти из аккаунта?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }
        });

        return view;
    }

    private void setButtonsIfUserIsBandLeader() {

        if (isUserBandLeader()) {
            binding.manageBandButton.setVisibility(View.VISIBLE);
            binding.createBandButton.setVisibility(View.GONE);
        } else {
            binding.manageBandButton.setVisibility(View.GONE);
        }
    }

    private boolean isUserBandLeader() {

        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return preferences.getBoolean("bandLeader", false);

    }

    private void setUserLoginInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        login = preferences.getString("login", "Пользователь");
        binding.userImgAndName.nicknameTextView.setText(login);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Вы уверены что хотите расформировать группу?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        disbandGroup();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void disbandGroup() {
        DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference().child("bands");
        DatabaseReference bandsVacanciesRef = FirebaseDatabase.getInstance().getReference().child("vacancies").child("from_bands");
        bandsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bandSnapshot : dataSnapshot.getChildren()) {
                    Iterable<DataSnapshot> members = bandSnapshot.child("memberUserLogins").getChildren();
                    for (DataSnapshot member : members) {
                        if (member.getValue(String.class).equals(login)) {
                            bandsVacanciesRef.child(bandSnapshot.getKey()).removeValue();

                            bandSnapshot.getRef().removeValue();
                            Toast.makeText(getContext(), "Группа успешно расформирована!", Toast.LENGTH_LONG).show();
                            binding.manageBandButton.setVisibility(View.GONE);
                            binding.createBandButton.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                }
                Toast.makeText(getContext(), "Ошибка при расформировании группы!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Ошибка: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

}