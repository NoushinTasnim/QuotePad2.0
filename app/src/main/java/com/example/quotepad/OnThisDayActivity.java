package com.example.quotepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.example.quotepad.adapter.BirthAdapter;
import com.example.quotepad.nav_frags.RandomQuotesFragment;
import com.example.quotepad.nav_frags.UploadQuoteFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class OnThisDayActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private BirthFragment birthFragment;
    private DeathFragment deathFragment;
    private EventsFragment eventsFragment;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OnThisDayActivity.this,QuoteActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_this_day);

        viewPager = findViewById(R.id.on_this_day_view_pager);
        tabLayout = findViewById(R.id.on_this_day_tabs);

        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        prepareViewPager(viewPager);
    }

    private void prepareViewPager(ViewPager viewPager) {
        // Initialize main adapter
        MainAdapter adapter=new MainAdapter(getSupportFragmentManager());

        // Initialize main fragment
        birthFragment = new BirthFragment();
        deathFragment = new DeathFragment();
        eventsFragment = new EventsFragment();

        Bundle bundle=new Bundle();
        bundle.putString("title","Events");
        eventsFragment.setArguments(bundle);
        adapter.addFragment(eventsFragment,"Events");

        bundle=new Bundle();
        bundle.putString("title","One");
        birthFragment.setArguments(bundle);
        adapter.addFragment(birthFragment,"Birth");

        bundle=new Bundle();
        bundle.putString("title","Two");
        deathFragment.setArguments(bundle);
        adapter.addFragment(deathFragment,"Death");

        // set adapter
        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {
        // Initialize arrayList
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> fragment_title =new ArrayList<>();

        int[] imageList={
                R.drawable.ic_baseline_favorite_24,
                R.drawable.ic_baseline_home_24,
                R.drawable.ic_baseline_home_24
        };

        // Create constructor
        public void addFragment(Fragment fragment,String s)
        {
            // Add fragment
            fragments.add(fragment);
            // Add title
            fragment_title.add(s);
        }

        public MainAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            // return fragment position
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Return fragment array list size
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            // Initialize drawable
            Drawable icon= ContextCompat.getDrawable(getApplicationContext(), imageList[position]);

            // set bound
            icon.setBounds(0,0,icon.getIntrinsicWidth(),
                    icon.getIntrinsicHeight());

            // Initialize spannable string
            SpannableString spannableString=new SpannableString("   "+ fragment_title.get(position));

            // Initialize image span
            ImageSpan imageSpan=new ImageSpan(icon,ImageSpan.ALIGN_CENTER);

            // Set span
            spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // return spannable string
            return spannableString;
        }
    }
}