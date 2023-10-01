package com.example.blood_bank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.blood_bank.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private RadioButton genderSelected;
    private DatePickerDialog picker;
    private static final String TAG="SignUp";
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
        binding.already.setOnClickListener(view -> {
            Intent intent=new Intent(SignUp.this,LogIn.class);
            startActivity(intent);
            finish();
        });
        binding.diseases.setOnClickListener(view -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(SignUp.this);
            builder.setTitle("Have you any of these diseases?");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(diseases, selectedDiseases, (dialogInterface, i, b) -> {
                if(b){
                    disease.add(diseases[i]);
                }
                else{
                    disease.remove(diseases[i]);
                }
            });
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                StringBuilder selectedDisease=new StringBuilder();
                for(int pk=0;pk<disease.size();pk++)
                {
                    selectedDisease.append(disease.get(pk));
                    if(pk!=disease.size()-1)
                        selectedDisease.append(",");
                }
                binding.diseases.setText(selectedDisease.toString());
            });
            builder.show();
        });
        binding.gender.clearCheck();
        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                picker=new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        binding.dob.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },year,month,day);
                picker.show();
            }
        });
        binding.signUp.setOnClickListener(view -> {
            int selectedGender=binding.gender.getCheckedRadioButtonId();
            genderSelected=findViewById(selectedGender);

            String sign_name= Objects.requireNonNull(binding.name.getText()).toString();
            String sign_phone= Objects.requireNonNull(binding.phone.getText()).toString();
            String sign_address= Objects.requireNonNull(binding.address.getText()).toString();
            String sign_email= Objects.requireNonNull(binding.email.getText()).toString();
            String sign_bloodGroup=binding.bloodGroup.getText().toString();
            String sign_dob= Objects.requireNonNull(binding.dob.getText()).toString();
            String sign_password= Objects.requireNonNull(binding.setpassword.getText()).toString();
            String sign_confirmPassword= Objects.requireNonNull(binding.confirmpassword.getText()).toString();
            String sign_Diseases=binding.diseases.getText().toString();
            String sign_gender;
            String mobileRegex="[6-9][0-9]{9}";
            Matcher mobileMatcher;
            Pattern mobilePattern= Pattern.compile(mobileRegex);
            mobileMatcher=mobilePattern.matcher(sign_phone);
            if(TextUtils.isEmpty(sign_name))
            {
                Toast.makeText(SignUp.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                binding.name.setError("Full name is required");
                binding.name.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_phone)) {
                Toast.makeText(SignUp.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                binding.phone.setError("Phone number is required");
                binding.phone.requestFocus();
            }
            else if(sign_phone.length()!=10) {
                Toast.makeText(SignUp.this, "Please re-enter your phone number", Toast.LENGTH_SHORT).show();
                binding.phone.setError("Phone number should be of 10 digits");
                binding.phone.requestFocus();
            }
            else if(!mobileMatcher.find()){
                Toast.makeText(SignUp.this, "Please re-enter your phone number", Toast.LENGTH_SHORT).show();
                binding.phone.setError("Phone number is not valid");
                binding.phone.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_email)) {
                Toast.makeText(SignUp.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                binding.email.setError("Email is required");
                binding.email.requestFocus();
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(sign_email).matches()) {
                Toast.makeText(SignUp.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                binding.email.setError("Valid email is required");
                binding.email.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_address)) {
                Toast.makeText(SignUp.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                binding.address.setError("Address is required");
                binding.address.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_bloodGroup)) {
                Toast.makeText(SignUp.this, "Please select your blood group", Toast.LENGTH_SHORT).show();
                binding.bloodGroup.setError("Blood Group is required");
                binding.bloodGroup.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_dob)) {
                Toast.makeText(SignUp.this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
                binding.dob.setError("Date of birth is required");
                binding.dob.requestFocus();
            }
            else if(binding.gender.getCheckedRadioButtonId()==-1)
            {
                Toast.makeText(SignUp.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                genderSelected.setError("Gender is required");
                genderSelected.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_password)) {
                Toast.makeText(SignUp.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                binding.setpassword.setError("Password is required");
                binding.setpassword.requestFocus();
            }
            else if(sign_password.length()<6)
            {
                Toast.makeText(SignUp.this, "Password should be of at least 6 digits", Toast.LENGTH_SHORT).show();
                binding.setpassword.setError("Password too weak");
                binding.setpassword.requestFocus();
            }
            else if(TextUtils.isEmpty(sign_confirmPassword)) {
                Toast.makeText(SignUp.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                binding.confirmpassword.setError("Password Confirmation is required");
                binding.confirmpassword.requestFocus();
            }
            else if(!sign_password.equals(sign_confirmPassword)){
                Toast.makeText(SignUp.this, "Confirm Password should be same", Toast.LENGTH_SHORT).show();
                binding.confirmpassword.setError("Password confirmation is required");
                binding.confirmpassword.requestFocus();
                binding.setpassword.clearComposingText();
                binding.confirmpassword.clearComposingText();
            }
            else if(TextUtils.isEmpty(sign_Diseases)) {
                Toast.makeText(SignUp.this, "Please select your disease if any otherwise select none", Toast.LENGTH_SHORT).show();
                binding.diseases.setError("Disease selection is required");
                binding.diseases.requestFocus();
            }
            else{
                sign_gender=genderSelected.getText().toString();
                binding.signUpProgressBar.setVisibility(View.VISIBLE);
                registerUser(sign_name,sign_phone,sign_email,sign_address,sign_bloodGroup,sign_dob,sign_gender,sign_password,sign_Diseases);
            }
        });
    }

    private void registerUser(String signName, String signPhone, String signEmail, String signAddress, String signBloodGroup, String signDob, String signGender, String signPassword, String signDiseases) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(signEmail,signPassword).addOnCompleteListener(SignUp.this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                FirebaseUser firebaseUser=auth.getCurrentUser();

                UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(signName).build();
                assert firebaseUser != null;
                firebaseUser.updateProfile(userProfileChangeRequest);
                AccessUserDetails newUserData=new AccessUserDetails(signPhone,signAddress,signBloodGroup,signDob,signGender,signDiseases);
                DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
                referenceProfile.child(firebaseUser.getUid()).setValue(newUserData).addOnCompleteListener(task1 -> {
                    if(task.isSuccessful())
                    {
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(this, "User registered successfully. Please verify your email.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SignUp.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(this, "User registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    binding.signUpProgressBar.setVisibility(View.GONE);
                });
            }
            else{
                try {
                    throw task.getException();
                }
                catch (FirebaseAuthWeakPasswordException e)
                {
                    binding.setpassword.setError("Your password id too weak. Kindly use a mix of alphabets, numbers and special symbols");
                    binding.setpassword.requestFocus();
                }
                catch (FirebaseAuthInvalidCredentialsException e)
                {
                    binding.setpassword.setError("Your email is invalid or already in use. Kindly re-enter email.");
                    binding.setpassword.requestFocus();
                }
                catch (FirebaseAuthUserCollisionException e)
                {
                    binding.setpassword.setError("User is already registered with this email. Use another email");
                    binding.setpassword.requestFocus();
                }
                catch(Exception e)
                {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                binding.signUpProgressBar.setVisibility(View.GONE);
            }
        });
    }
}