package com.example.esiapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.esiapp.fragment.CoursesFragment;
import com.example.esiapp.fragment.HomeFragment;
import com.example.esiapp.fragment.ProfileFragment;
import com.example.esiapp.fragment.ToDo;
import com.example.esiapp.fragment.feedback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    ImageView addPost;
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    MediaPlayer click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        click = MediaPlayer.create(this, R.raw.click);
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView1.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_email);
        // ImageView navUserPhot = headerView.findViewById(R.id.nav_user_photo);
        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        // now we will use Glide to load user image
        // Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
        //Add New Post
        addPost = findViewById(R.id.addpost_button);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPost.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.none);
            }
        });
        //Drawer Menu and ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView1.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView1.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                click.start();
                addPost.setVisibility(View.VISIBLE);
                break;
            case R.id.planner:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ToDo()).commit();
                click.start();
                addPost.setVisibility(View.INVISIBLE);
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                click.start();
                addPost.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent loginAcivity = new Intent(getApplicationContext(), Login.class);
                startActivity(loginAcivity);
                click.start();
                overridePendingTransition(R.anim.slide_in_left, R.anim.none);
                finish();
                break;
            case R.id.courses:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CoursesFragment()).commit();
                click.start();
                addPost.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new feedback()).commit();
                click.start();
                addPost.setVisibility(View.INVISIBLE);
                break;

            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd/.android.package-archive");
                intent.putExtra(Intent.EXTRA_STREAM, "my new app");
                startActivity(Intent.createChooser(intent,"ShareVia"));
                click.start();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer((GravityCompat.START));
        }else {
            super.onBackPressed();
        }
    }
}