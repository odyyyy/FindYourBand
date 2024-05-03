package com.example.findyourband;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.databinding.FragmentRegisterFirstStepBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFirstStep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFirstStep extends Fragment {

    FragmentRegisterFirstStepBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RegisterFirstStep() {
    }


    public static RegisterFirstStep newInstance(String param1, String param2) {
        RegisterFirstStep fragment = new RegisterFirstStep();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterFirstStepBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RegisterSecondStep fragment = new RegisterSecondStep();
                // проверка данных


                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.auth_fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}