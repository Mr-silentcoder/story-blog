package com.maxlab.storyhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxlab.storyhub.databinding.ActivitySingupBinding;

public class SingupActivity extends AppCompatActivity {

    ActivitySingupBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating New Account");
        database = FirebaseFirestore.getInstance();
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, passwd;
                name = binding.userName.getText().toString();
                email = binding.emailBox.getText().toString();
                passwd = binding.passwdBox.getText().toString();
                final  User user = new User(name,email,passwd);

                if (name.equals("")) {
                    binding.userName.setError("Empty");
                    Toast.makeText(SingupActivity.this, "Please enter a Name", Toast.LENGTH_SHORT).show();

                } else if (email.equals("")) {
                    binding.emailBox.setError("Empty");
                    Toast.makeText(SingupActivity.this, "Please enter a valid Email address", Toast.LENGTH_SHORT).show();

                } else if (passwd.equals("")) {
                    binding.passwdBox.setError("Empty");
                    Toast.makeText(SingupActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();

                }else {
                    dialog.show();
                auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String userid = task.getResult().getUser().getUid();
                            database
                                    .collection("users")
                                    .document(userid)
                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       dialog.dismiss();
                                       startActivity(new Intent(SingupActivity.this, MainActivity.class));
                                       finish();
                                   }else{
                                       Toast.makeText(SingupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

                        } else {
                            dialog.dismiss();
                            Toast.makeText(SingupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

               }
            }
        });





        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}