package com.maxlab.storyhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxlab.storyhub.databinding.AboutPageBinding;
import com.maxlab.storyhub.databinding.FragmentInstructionsBinding;
import com.maxlab.storyhub.databinding.PrivacyPolicyBinding;
import com.maxlab.storyhub.databinding.ReportProblemBinding;
import com.maxlab.storyhub.databinding.TermsConditionBinding;

import java.util.HashMap;
import java.util.Map;


public class instructions extends Fragment {


    public instructions() {
        // Required empty public constructor
    }

    FragmentInstructionsBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    ProgressDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentInstructionsBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading");
        binding.privacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrivacyPolicyBinding privacyPolicyBinding = PrivacyPolicyBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(privacyPolicyBinding.getRoot()).create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                alertDialog.show();
            }
        });

        binding.termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TermsConditionBinding termsConditionBinding = TermsConditionBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(termsConditionBinding.getRoot()).create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                alertDialog.show();
            }
        });

        binding.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportProblemBinding reportProblemBinding = ReportProblemBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(reportProblemBinding.getRoot()).create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                reportProblemBinding.reportSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                        String reportMsg = reportProblemBinding.reportBox.getText().toString();

                        Map<String, Object> report = new HashMap<>();
                        report.put("report", reportMsg);

                        database.collection("report").document(FirebaseAuth.getInstance().getUid()).set(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Request Send Successfully", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });

                    }
                });
                alertDialog.show();
            }
        });

        binding.aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutPageBinding aboutPageBinding = AboutPageBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(aboutPageBinding.getRoot()).create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                alertDialog.show();
            }
        });
        return binding.getRoot();
    }
}