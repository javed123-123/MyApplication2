package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Verification extends AppCompatActivity {
    Button verify;
    TextView not_verified;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        not_verified=findViewById(R.id.not_verified);
        verify=findViewById(R.id.verify);
        fAuth=FirebaseAuth.getInstance();
        FirebaseUser user=fAuth.getCurrentUser();
        setTitle("Verification");

        if(!user.isEmailVerified()){
            not_verified.setVisibility(View.VISIBLE);
            verify.setVisibility(View.VISIBLE);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(),"Verification Email has been sent.",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG","onFailure: Email not sent "+e.getMessage());
                        }
                    });
                }
            });
        }
        if(user.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

}