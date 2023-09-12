package com.example.blood_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.blood_bank.databinding.ActivitySignUpBinding;

import java.util.ArrayList;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        boolean [] selectedDiseases;
        String []blood= getResources().getStringArray(R.array.blood);
        String []diseases=getResources().getStringArray(R.array.diseases);
        ArrayAdapter<String>ap=new ArrayAdapter<>(this,R.layout.blood_dropdown,blood);
        ArrayList<String>disease=new ArrayList<>();
        binding.bloodGroup.setAdapter(ap);
        selectedDiseases=new boolean[diseases.length];
        binding.already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
                finish();
            }
        });
        binding.diseases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SignUp.this);
                builder.setTitle("Have you any of these diseases?");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(diseases, selectedDiseases, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            disease.add(diseases[i]);
                        }
                        else{
                            disease.remove(diseases[i]);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder selectedDisease=new StringBuilder();
                        for(int pk=0;pk<disease.size();pk++)
                        {
                            selectedDisease.append(disease.get(pk));
                            if(pk!=disease.size()-1)
                                selectedDisease.append(",");
                        }
                        binding.diseases.setText(selectedDisease.toString());
                    }
                });
                builder.show();
            }
        });
        binding.signUpProgressBar.
    }
}