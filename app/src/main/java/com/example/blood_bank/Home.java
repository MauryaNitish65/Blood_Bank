package com.example.blood_bank;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blood_bank.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends Fragment {
    private FragmentHomeBinding binding;
    private String name;
    private FirebaseAuth auth;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        if(firebaseUser==null)
        {
            Toast.makeText(getActivity(), "Something went wrong! User details are not available at the moment", Toast.LENGTH_SHORT).show();
        }
        else{
            showUserProfile(firebaseUser);
        }

        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        DatabaseReference referenceData= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceData.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AccessUserDetails userData=snapshot.getValue(AccessUserDetails.class);
                if(userData!=null)
                {
                    name=firebaseUser.getDisplayName();
                    binding.welcomeName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}