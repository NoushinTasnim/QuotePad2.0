package com.example.quotepad.nav_frags.profile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quotepad.MainActivity;
import com.example.quotepad.R;
import com.example.quotepad.user.UserActivity;
import com.example.quotepad.verification.PhoneNumberVerifyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView nav_user_name, nav_name;
    ImageView menuIcon;
    LinearLayout contentView;
    TextView tv;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    Query checkUser;

    String name, em, username, ph, pass, uid;

    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerLayout = findViewById(R.id.prof_drawer);
        navigationView = findViewById(R.id.prof_navigation_view);
        menuIcon = findViewById(R.id.prof_menu_icon);

        tv = findViewById(R.id.prof_tab_name);

        View headerView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        nav_name = (TextView) headerView.findViewById(R.id.nav_header_name);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUser = reference.orderByChild("id").equalTo(uid);

        loadFragment(new SettingsFragment());
        Menu menu = navigationView.getMenu();
        MenuItem item1 = menu.getItem(1);
        tv.setText("User Profile");
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
                    if (item.isChecked()) {
                            Log.i(TAG, "onNavigationItemSelected: nopes " + item);
                            it = item.getItemId();
                            item.setChecked(false);
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

                        case R.id.change_pass:
                            tv.setText("");
                            navigationView.setCheckedItem(R.id.change_pass);
                            loadFragment(new UpdatePasswordFragment());
                            break;


                        case R.id.settings_nav:
                            tv.setText("User Profile");
                            navigationView.setCheckedItem(R.id.settings_nav);
                            loadFragment(new SettingsFragment());
                            break;

                        case R.id.nav_mail:
                            navigationView.setCheckedItem(R.id.nav_mail);

                            /*Intent intent  = new Intent(UserProfileActivity.this, UpdateEmailAddress.class);
                            intent.putExtra("pname",name);
                            intent.putExtra("mail",em);
                            intent.putExtra("user",username);
                            intent.putExtra("pass",pass);
                            intent.putExtra("set","settings");

                            Log.i(TAG, "onClick: " + name + " " + em + " " + username + " " + ph);

                            startActivity(intent);*/
                            loadFragment(new UpdateEmailAddress());

                            break;

                        case R.id.nav_user_home:
                            navigationView.setCheckedItem(R.id.nav_user_home);
                            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
                            break;


                        case R.id.delete_user:
                            navigationView.setCheckedItem(R.id.delete_user);

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(UserProfileActivity.this);
                            builder1.setMessage("You won't be able to retrieve your account. Are you sure you want to delete your account?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            FirebaseDatabase.getInstance().getReference("emails").child(username).removeValue();
                                            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User account deleted.");
                                                            }
                                                        }
                                                    });
                                            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                            startActivity(new Intent(UserProfileActivity.this, UserActivity.class));
                                            dialog.cancel();
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

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
        {
            super.onBackPressed();
        }

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

            ft.replace(R.id.prof_container, fragment);
            //ft.commit();
            ft.commitNow();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}