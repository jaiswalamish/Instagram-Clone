package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagram.fragments.homeFragment;
import com.example.instagram.fragments.notificationFragment;
import com.example.instagram.fragments.profileFragment;
import com.example.instagram.fragments.searchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {


    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()){
                  case R.id.nav_home:
                      fragment=new homeFragment();
                      break;

                  case R.id.nav_search:
                      fragment=new searchFragment();
                      break;

                  case R.id.nav_heart:
                      fragment=new notificationFragment();
                      break;

                  case R.id.nav_person:
                      fragment=new profileFragment();
                      break;

                  case R.id.nav_add:
                     fragment=null;
                     startActivity(new Intent(MainActivity.this,postActivity.class));
                     break;
              }
              if(fragment!=null){
                  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
              }
              return true;
            }
        });
//        Bundle bundle=getIntent().getExtras();
//        if(bundle!=null){
//            String id=bundle.getString("publisher");
//            getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("profileId",id).apply();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new profileFragment()).commit();
//            bottomNavigationView.setSelectedItemId(R.id.nav_person);
//
//        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new homeFragment()).commit();
    }
}