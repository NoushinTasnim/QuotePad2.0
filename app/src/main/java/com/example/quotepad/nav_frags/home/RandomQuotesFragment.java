package com.example.quotepad.nav_frags.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quotepad.R;
import com.example.quotepad.model.QuotesModel;
import com.example.quotepad.swipe.SwipeToDeleteCallback;
import com.example.quotepad.swipe.SwipeToFav;
import com.example.quotepad.adapter.RandomQuotesAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RandomQuotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RandomQuotesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList arrayList = new ArrayList<>();
    ArrayList arrayList2 = new ArrayList<>();
    RandomQuotesAdapter randomQuotesAdapter;
    CoordinatorLayout coordinatorLayout;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public RandomQuotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RandomQuotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RandomQuotesFragment newInstance(String param1, String param2) {
        RandomQuotesFragment fragment = new RandomQuotesFragment();
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
        return inflater.inflate(R.layout.fragment_random_quotes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.recycler_view);
        swipeRefreshLayout = getActivity().findViewById(R.id.swipeContainer);
        coordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Refreshing Feed...", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                int si = arrayList.size();
                arrayList.clear();
                arrayList2.clear();
                randomQuotesAdapter.notifyItemRangeRemoved(0, si);
                PlayOn();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setRefreshing(true);

        PlayOn();

        swipeRefreshLayout.setRefreshing(false);
        enableSwipeToDeleteAndUndo();
        enableSwipeFav();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final String item = randomQuotesAdapter.getQuotes().get(position);
                final String item2 = randomQuotesAdapter.getAuthors().get(position);

                randomQuotesAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        randomQuotesAdapter.restoreItem(item, item2, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void enableSwipeFav() {
        SwipeToFav swipeToFav = new SwipeToFav(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final String item = randomQuotesAdapter.getQuotes().get(position);
                final String item2 = randomQuotesAdapter.getAuthors().get(position);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference();
                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());

                QuotesModel helperClass = new QuotesModel(item,item2);
                reference.child("users").child(currentuser).child("fav").child(currentDateTime).setValue(helperClass);

                randomQuotesAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Added to favourites.", Snackbar.LENGTH_LONG);

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToFav);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void PlayOn() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://zenquotes.io/api/quotes";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;

                    int s = jsonArray.length();
                    for(int n = 0; n < s; n++)
                    {
                        jsonObject = jsonArray.getJSONObject(n);
                        arrayList.add(jsonObject.getString("q"));
                        arrayList2.add("-" +(jsonObject.getString("a")));
                    }
                    randomQuotesAdapter = new RandomQuotesAdapter(getActivity(), arrayList, arrayList2);
                    recyclerView.setAdapter(randomQuotesAdapter); // set the Adapter to RecyclerView
                    progressDialog.dismiss();

                    randomQuotesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), "Could not fetch data. Restart App " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}