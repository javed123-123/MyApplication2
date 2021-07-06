package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    TextView login_page;
    EditText user_name,phone_number,e_mail,profession,password;
    Button sign_up;
    FirebaseAuth firebaseAuth;
    ProgressBar progress_bar;
    FirebaseFirestore fire_store;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login_page=findViewById(R.id.log_in);
        user_name=findViewById(R.id.user_name);
        phone_number=findViewById(R.id.phone_number);
        e_mail=findViewById(R.id.e_mail);
        profession=findViewById(R.id.profession);
        progress_bar=findViewById(R.id.progress_bar);
        firebaseAuth=FirebaseAuth.getInstance();
        sign_up=findViewById(R.id.sign_up);
        password=findViewById(R.id.password);
        fire_store=FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e_mail.getText().toString();
                String Password = password.getText().toString();
                String username = user_name.getText().toString();
                String number = phone_number.getText().toString();
                String Profession = profession.getText().toString();
                if (!Pattern.matches("^(.+)@(.+)$", email)) {
                    e_mail.setError("Not a valid mail");
                    return;
                } else if (TextUtils.isEmpty(username)) {
                    user_name.setError("Username is required");
                    return;
                } else if (!Pattern.matches("[0-9]{10}", number)) {
                    phone_number.setError("Phone number should be a 10 digit number");
                } else if (TextUtils.isEmpty(Password)) {
                    password.setError("Password must not be empty");
                } else if (TextUtils.isEmpty(Profession)) {
                    profession.setError("Profession is required");
                }
                else{
                progress_bar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser User = firebaseAuth.getCurrentUser();
                            User.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUp.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fire_store.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("E-mail", email);
                            user.put("Phone Number", number);
                            user.put("Profession", Profession);
                            user.put("UserName", username);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user profile is created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Verification.class));
                        } else {
                            Toast.makeText(SignUp.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                        }
                    }
                });
            }
            }
        });
        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));
            }
        });

    }
}