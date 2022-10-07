package com.example.quotepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SignInFragment signInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.sign_view_pager);
        tabLayout = findViewById(R.id.tab_lay);

        signInFragment = new SignInFragment();

        tabLayout.setupWithViewPager(viewPager);

        TabActivity.ViewPagerAdapter viewPagerAdapter = new TabActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment(signInFragment, "Sign In");

        viewPager.setAdapter(viewPagerAdapter);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragment_title = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragment_title.add(title);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragment_title.get(position);
        }
    }
}