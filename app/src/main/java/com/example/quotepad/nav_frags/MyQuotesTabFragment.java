package com.example.quotepad.nav_frags;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quotepad.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyQuotesTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyQuotesTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ViewPager viewPager;
    private TabLayout tabLayout;

    private UploadedQuotesFragment uploadedQuotesFragment;
    private FavouriteQuotesFragment favouriteQuotesFragment;

    public MyQuotesTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyQuotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyQuotesTabFragment newInstance(String param1, String param2) {
        MyQuotesTabFragment fragment = new MyQuotesTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_quotes_tab, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewPager = getActivity().findViewById(R.id.my_quotes_view_pager);
        tabLayout = getActivity().findViewById(R.id.my_quotes_tabs);

        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        prepareViewPager(viewPager);
    }

    private void prepareViewPager(ViewPager viewPager) {
        // Initialize main adapter
        MyQuotesTabFragment.MainAdapter adapter=new MyQuotesTabFragment.MainAdapter(getActivity().getSupportFragmentManager());

        // Initialize main fragment
        uploadedQuotesFragment = new UploadedQuotesFragment();
        favouriteQuotesFragment = new FavouriteQuotesFragment();

        Bundle bundle=new Bundle();
        bundle.putString("title","One");
        uploadedQuotesFragment.setArguments(bundle);
        adapter.addFragment(uploadedQuotesFragment,"Uploaded");

        bundle=new Bundle();
        bundle.putString("title","Two");
        favouriteQuotesFragment.setArguments(bundle);
        adapter.addFragment(favouriteQuotesFragment,"Favourite");

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
            Drawable icon= ContextCompat.getDrawable(getActivity().getApplicationContext()
                    ,imageList[position]);

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
