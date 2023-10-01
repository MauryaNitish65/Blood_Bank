package com.example.blood_bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blood_bank.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private ActivityLogInBinding binding;
    private FirebaseAuth authProfile;
    private static final String TAG="LogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtSign.setOnClickListener(view -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
            finish();
        });
        authProfile=FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_Email= Objects.requireNonNull(binding.etEmail.getText()).toString();
                String login_Password= Objects.requireNonNull(binding.etPassword.getText()).toString();
                if(TextUtils.isEmpty(login_Email)) {
                    Toast.makeText(LogIn.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    binding.etEmail.setError("Email is required");
                    binding.etEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(login_Email).matches()) {
                    Toast.makeText(LogIn.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    binding.etEmail.setError("Valid email is required");
                    binding.etEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(login_Password)) {
                    Toast.makeText(LogIn.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    binding.etPassword.setError("Password is required");
                    binding.etPassword.requestFocus();
                }
                else{
                    binding.loginProgressbar.setVisibility(View.VISIBLE);
                    loginUser(login_Email,login_Password);
                }
            }
        });
    }

    private void loginUser(String loginEmail, String loginPassword) {
        authProfile.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    FirebaseUser firebaseUser=authProfile.getCurrentUser();
                    if(firebaseUser.isEmailVerified())
                    {
                        Toast.makeText(LogIn.this, "You are logged in now", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogIn.this,MainActivity.class));
                        finish();
                    }
                    else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                }
                else{
                    try{
                        throw task.getException();
                    }
                    catch(FirebaseAuthInvalidUserException e)
                    {
                        binding.etEmail.setError("User does not exist or no longer exist. Please register again");
                        binding.etEmail.requestFocus();
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        binding.etEmail.setError("Invalid credential. Kindly, check and re-enter");
                        binding.etEmail.requestFocus();
                    }
                    catch(Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LogIn.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                binding.loginProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LogIn.this);
        builder.setTitle("Email verification");
        builder.setMessage("Please verify your email now. You cannot login without email verification.");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser()!=null)
        {
            Toast.makeText(LogIn.this, "You are already logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LogIn.this,MainActivity.class));
            finish();
        }
        else{
            Toast.makeText(LogIn.this, "You can login now.", Toast.LENGTH_SHORT).show();
        }
    }
}