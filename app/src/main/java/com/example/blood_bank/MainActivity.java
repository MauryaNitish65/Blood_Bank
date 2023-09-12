package com.example.blood_bank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.blood_bank.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.container.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, binding.drawerLayout, binding.container.toolbar, R.string.closeDrawer, R.string.openDrawer);
        //noinspection deprecation
        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(MainActivity.this);

        // Bottom Navigation
        binding.container.content.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bottom_nav_home)
                Toast.makeText(MainActivity.this, "Bottom " + getString(R.string.home), Toast.LENGTH_SHORT).show();
            else if (id == R.id.bottom_nav_donate) {
                Toast.makeText(MainActivity.this, "Bottom " + getString(R.string.donate), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Bottom " + getString(R.string.request), Toast.LENGTH_SHORT).show();
            }

            return false;
        });
        binding.container.content.bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_drawer_map_view) {
            Toast.makeText(this, R.string.map_view, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_profile) {
            Toast.makeText(this, R.string.my_profile, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_requests) {
            Toast.makeText(this, R.string.requests, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_hospital_service) {
            Toast.makeText(this, R.string.hospital_service, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_blood_bank_service) {
            Toast.makeText(this, R.string.blood_bank_service, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_logout) {
            Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_about_us) {
            Toast.makeText(this, R.string.about_us, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.rate_us, Toast.LENGTH_SHORT).show();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}