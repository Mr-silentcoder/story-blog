package com.maxlab.storyhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxlab.storyhub.databinding.ActivityLoginBinding;
import com.maxlab.storyhub.databinding.ForgotpasswdBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseFirestore database;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging In");

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        binding.usrLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, passwd;
                email = binding.userEmail.getText().toString();
                passwd = binding.userPasswd.getText().toString();

                if (email.equals("")){
                    binding.userEmail.setError("Empty");
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                }
                else if(passwd.equals("")){
                    binding.userPasswd.setError("Empty");
                    Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();

                }else {
                    dialog.show();
                    auth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                database.collection("users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        dialog.dismiss();
                                        user = documentSnapshot.toObject(User.class);
                                        String userpass = user.getPasswd();
                                         if (userpass.equals(passwd)){
                                             dialog.dismiss();
                                             Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                             startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                             finish();
                                         }else{
                                             database.collection("users")
                                                     .document(auth.getUid())
                                                     .update("passwd", passwd);
                                             dialog.dismiss();
                                             Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                             startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                             finish();
                                         }

                                    }
                                });

                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        binding.forgotPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ForgotpasswdBinding dialogBinding = ForgotpasswdBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                        .setView(dialogBinding.getRoot()).setCancelable(false).create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialogBinding.emailSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        auth.signOut();
                        dialog.show();
                        String userEmail = dialogBinding.forgotEmail.getText().toString();
                        if (userEmail.equals("")){
                            dialog.dismiss();
                            dialogBinding.forgotEmail.setError("Enter your email");
                            Toast.makeText(LoginActivity.this, "Enter your email address", Toast.LENGTH_SHORT).show();
                        }else {

                            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Please check your inbox or spam box for password reset link", Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }else{
                                            dialog.dismiss();
                                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        }
                    }
                });
                dialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });


                alertDialog.show();
            }
        });
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SingupActivity.class));
                finish();
            }
        });
    }
}