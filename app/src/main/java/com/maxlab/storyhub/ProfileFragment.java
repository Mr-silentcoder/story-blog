package com.maxlab.storyhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxlab.storyhub.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    public ProfileFragment () {
        // Required empty public constructor
    }

    FragmentProfileBinding binding;
    FirebaseFirestore database;
    User user;
    ProgressDialog dialog;
    FirebaseAuth auth;
    private InterstitialAd mInterstitialAd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading");
        auth = FirebaseAuth.getInstance();
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(getActivity());

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
        dialog.show();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dialog.dismiss();
                user = documentSnapshot.toObject(User.class);
                binding.userNameedit.setText(user.getName());
                binding.emailBoxedit.setText(user.getEmail());


            }
        });


        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName  = binding.userNameedit.getText().toString();
                String oldpasswd = binding.oldPasswordBox.getText().toString();
                String newPassword = binding.newPassword.getText().toString();
                String oldPassword = user.getPasswd();
                FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
                dialog.show();
                //checking old pass equal to db
                if (oldpasswd.equals(oldPassword)){
                    // if old pass ok, then check user name change or not
                    if(userName.equals(user.getName())) {
                        // user name equal, then check new password box value
                        if (newPassword.equals("")){
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();
                            binding.newPassword.setError("Enter a password");
                        }else {
                            // update passwd
                            usr.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                database.collection("users")
                                                        .document(auth.getUid())
                                                        .update("passwd", newPassword);

                                                Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        // check pass box value
                    }else  if (newPassword.equals("")){
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();
                        binding.newPassword.setError("Enter a password");

                    }else{
                        //if user name change, run this
                        usr.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            database.collection("users")
                                                    .document(auth.getUid())
                                                    .update("name", userName,"passwd",newPassword);

                                            Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            dialog.dismiss();
                                            binding.newPassword.setError("Error");
                                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                    // only change user name, then run this
                }else if(!userName.equals(user.getName())){
                    dialog.dismiss();
                    database.collection("users")
                            .document(auth.getUid())
                            .update("name", userName);
                    Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();

                 //passwd, email box value checking
                }else if(newPassword.equals("")){
                    dialog.dismiss();
                    if(oldpasswd.equals("")){
                        binding.oldPasswordBox.setError("Enter old password");
                        binding.newPassword.setError("Enter a password");
                        Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();
                        binding.newPassword.setError("Enter a password");
                    }
                }else if(oldpasswd.equals("")){
                    dialog.dismiss();
                    if (newPassword.equals("")) {

                        binding.oldPasswordBox.setError("Enter a password");
                        binding.newPassword.setError("Enter a password");
                        Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();

                    }else{
                        binding.oldPasswordBox.setError("Enter old password");
                        Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Make sure your entry is correct", Toast.LENGTH_SHORT).show();
                    binding.oldPasswordBox.setError("Incorrect");

                }


            }
        });



        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Log Out")
                        .setIcon(R.drawable.ic_error)
                        .setMessage("Are You Sure Want to logout ? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                auth.signOut();
                                startActivity(new Intent(getContext(),LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "Back", Toast.LENGTH_SHORT).show();
                            }
                        }).create();

                alertDialog.show();

            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}