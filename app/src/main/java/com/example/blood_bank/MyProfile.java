package com.example.blood_bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blood_bank.databinding.FragmentMyProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MyProfile extends Fragment {

    private FragmentMyProfileBinding binding;
    private String name,phone,address,email,dob,bloodgroup,gender,diseases;
    private FirebaseAuth authProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);

        // Firebase
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        if(firebaseUser==null)
        {
            Toast.makeText(getActivity(), "Something went wrong! User details are not available at the moment", Toast.LENGTH_SHORT).show();
        }
        else{
            checkEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);
        }

        return binding.getRoot();
    }

    private void checkEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified())
        {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity());
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

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        DatabaseReference referenceData=FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceData.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AccessUserDetails userData=snapshot.getValue(AccessUserDetails.class);
                if(userData!=null)
                {
                    name=firebaseUser.getDisplayName();
                    phone=userData.getPhone();
                    email=firebaseUser.getEmail();
                    address=userData.getAddress();
                    bloodgroup=userData.getBloodgroup();
                    gender=userData.getGender();
                    dob=userData.getDob();
                    diseases=userData.getDiseases();

                    binding.showWelcome.setText("Welcome "+name+" !");
                    binding.fullname.setText(name);
                    binding.phone.setText(phone);
                    binding.address.setText(address);
                    binding.dob.setText(dob);
                    binding.gender.setText(gender);
                    binding.email.setText(email);
                    binding.bloodGroup.setText(bloodgroup);
                    binding.diseases.setText(diseases);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}