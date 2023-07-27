package com.example.blood_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    TextView already;
    AutoCompleteTextView blood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        String st[]= getResources().getStringArray(R.array.blood);
        blood=findViewById(R.id.blood_group);
        ArrayAdapter<String>ap=new ArrayAdapter<>(this,R.layout.blood_dropdown,st);
        blood.setAdapter(ap);
        already=findViewById(R.id.already);
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
                finish();
            }
        });
    }
}