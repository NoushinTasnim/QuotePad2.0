package com.example.quotepad;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotepad.nav_frags.home.DiscoverFragment;
import com.example.quotepad.nav_frags.home.UploadedQuotesFragment;
import com.example.quotepad.nav_frags.home.UploadQuoteFragment;
import com.example.quotepad.nav_frags.profile.UserProfileActivity;
import com.example.quotepad.nav_frags.today.OnThisDayActivity;
import com.example.quotepad.user.UserActivity;
import com.example.quotepad.nav_frags.contact.ContactUsFragment;
import com.example.quotepad.nav_frags.home.FavouriteQuotesFragment;
import com.example.quotepad.nav_frags.today.QuoteOfTheDayFragment;
import com.example.quotepad.nav_frags.home.RandomQuotesFragment;
import com.example.quotepad.nav_frags.contact.WhoAreWeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView nav_user_name, nav_name;
    ImageView menuIcon;
    TextView tv;
    Animation aniFade, aniFade2;

    DatabaseReference reference;
    Query checkUser;

    String name, em, username, ph, pass, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerLayout = findViewById(R.id.quote_drawer_layout);
        navigationView = findViewById(R.id.quote_navigation_view);
        menuIcon = findViewById(R.id.quote_menu_icon);

        tv = findViewById(R.id.quote_tab_name);

        View headerView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        nav_name = (TextView) headerView.findViewById(R.id.nav_header_name);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUser = reference.orderByChild("id").equalTo(uid);

        aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        aniFade2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

        loadData();

        loadFragment(new QuoteOfTheDayFragment());
        Menu menu = navigationView.getMenu();
        MenuItem item1 = menu.getItem(0);
        tv.setText("Quote of the day");
        item1.setChecked(true);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username = snapshot.child(uid).child("username").getValue(String.class);
                    name = snapshot.child(uid).child("name").getValue(String.class);
                    em = snapshot.child(uid).child("email").getValue(String.class);
                    ph = snapshot.child(uid).child("phone").getValue(String.class);
                    pass = snapshot.child(uid).child("password").getValue(String.class);

                    btnSaveData(name,username,em);

                    nav_user_name.setText(username );
                    nav_name.setText(name);
                    Log.i(TAG, "onDataChange: " + username);
                }
                else{
                    nav_user_name.setText("Anonymous");
                    nav_name.setText("name" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                nav_user_name.setText("Anonymous");
                nav_name.setText("name" );
            }
        });

        this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Fragment current = getCurrentFragment();
                        if (current instanceof ContactUsFragment) {
                            navigationView.setCheckedItem(R.id.contact_btn);
                        } else {
                            navigationView.setCheckedItem(R.id.who_are_we_btn);
                        }
                    }
                });

        naviagtionDrawer();
    }

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.frag_container);
    }

    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int it = -1;
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem item = menu.getItem(i);
                    if(item.hasSubMenu())
                    {
                        Log.i(TAG, "onNavigationItemSelected: haass " + item);
                        Menu menu2 = item.getSubMenu();
                        for (int ij = 0; ij < menu2.size(); ij++) {
                            MenuItem item2 = menu2.getItem(ij);
                            Log.i(TAG, "onNavigationItemSelected: jgifs " + item2);
                            if (item2.isChecked()) {
                                it = item2.getItemId();
                                item2.setChecked(false);
                                Log.i(TAG, "onNavigationItemSelected: selft " + it);
                            }
                        }
                    }
                    else
                    {
                        if (item.isChecked()) {
                            Log.i(TAG, "onNavigationItemSelected: nopes " + item);
                            it = item.getItemId();
                            item.setChecked(false);
                        }
                    }
                }
                menuItem.setChecked(false);
                int id = menuItem.getItemId();

                Log.i(TAG, "onNavigationItemSelected: " + it + " " + menuItem);

                if(it==id)
                {
                    menuItem.setChecked(true);
                    return false;
                }
                else
                {
                    switch (id) {
                        case R.id.nav_q_o_day:
                            navigationView.setCheckedItem(R.id.nav_q_o_day);
                            tv.setText("Quote Of The Day");
                            loadFragment(new QuoteOfTheDayFragment());
                            break;

                        case R.id.nav_on_this_day:
                            navigationView.setCheckedItem(R.id.nav_on_this_day);
                            startActivity(new Intent(MainActivity.this, OnThisDayActivity.class));
                            break;

                        case R.id.nav_discover:
                            navigationView.setCheckedItem(R.id.nav_discover);
                            tv.setText("Discover Quotes");
                            loadFragment(new DiscoverFragment());
                            break;

                        case R.id.nav_ran:
                            navigationView.setCheckedItem(R.id.nav_ran);
                            tv.setText("Random Quotes");
                            loadFragment(new RandomQuotesFragment());
                            break;

                        case R.id.contact_btn:
                            navigationView.setCheckedItem(R.id.contact_btn);
                            tv.setText("Contact Us");
                            loadFragment(new ContactUsFragment());
                            break;

                        case R.id.nav_fav:
                            tv.setText("Favourite Quotes");
                            navigationView.setCheckedItem(R.id.nav_fav);
                            loadFragment(new FavouriteQuotesFragment());
                            break;

                        case R.id.nav_up:
                            tv.setText("Upload Quote");
                            navigationView.setCheckedItem(R.id.nav_up);
                            loadFragment(new UploadQuoteFragment());
                            break;

                        case R.id.nav_my_quotes:
                            tv.setText("My Quotes");
                            navigationView.setCheckedItem(R.id.nav_my_quotes);
                            loadFragment(new UploadedQuotesFragment());
                            break;

                        case R.id.nav_user_profile:
                             navigationView.setCheckedItem(R.id.nav_user_profile);
                             startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                             finish();
                             break;

                        case R.id.sign_out:
                            navigationView.setCheckedItem(R.id.sign_out);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                            break;

                        case R.id.who_are_we_btn:
                            tv.setText("Who Are We?");
                            navigationView.setCheckedItem(R.id.who_are_we_btn);
                            loadFragment(new WhoAreWeFragment());
                            break;

                        default:
                            break;
                    }
                    menuItem.setChecked(true);
                    setTitle(menuItem.getTitle());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();

        moveTaskToBack(true);
    }
    private void loadFragment(Fragment fragment) {
        if(getCurrentFragment() != fragment){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if(getCurrentFragment()!=null)
            {
                fm.beginTransaction().remove(getCurrentFragment()).commit();
            }

            ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.frag_container, fragment);
            //ft.commit();
            ft.commitNow();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void loadData() {
        File file = MainActivity.this
                .getFileStreamPath("CurrentUser.txt");

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader( MainActivity.this.openFileInput("CurrentUser.txt")));

                String st, qu = "";

                while ((st = reader.readLine()) != null){
                    qu = qu + st;
                }
                String[] separated = qu.split(" ; ");
                Log.i(TAG, "loadData: " + separated[1]);
                String separated2[] = qu.split(" ; ");
                // Print the string
                Log.i(TAG, "loadData: " + separated2[1]);
                nav_name.setText(separated[0]);
                nav_user_name.setText(separated2[0]);
                reader.close();

            } catch (IOException e) {
                Toast.makeText( MainActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public void btnSaveData(String name, String username, String email) {
        try {
            FileOutputStream file = openFileOutput("CurrentUser.txt", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(file);

            outputStreamWriter.write(name + " ; " + username + " ; " + email);

            outputStreamWriter.flush();
            outputStreamWriter.close();
            Toast.makeText(MainActivity.this, "Successfully saved", Toast.LENGTH_LONG)
                    .show();

        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}