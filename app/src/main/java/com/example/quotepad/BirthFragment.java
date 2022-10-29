package com.example.quotepad;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
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
import com.example.quotepad.adapter.BirthAdapter;
import com.example.quotepad.adapter.RandomQuotesAdapter;
import com.example.quotepad.model.RandomModel;
import com.example.quotepad.swipe.SwipeToDeleteCallback;
import com.example.quotepad.swipe.SwipeToFav;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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
 * Use the {@link BirthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BirthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    ArrayList arrayList = new ArrayList<>();
    ArrayList arrayList2 = new ArrayList<>();
    ArrayList arrayList3 = new ArrayList<>();
    BirthAdapter birthAdapter;

    public BirthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BirthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BirthFragment newInstance(String param1, String param2) {
        BirthFragment fragment = new BirthFragment();
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
        return inflater.inflate(R.layout.fragment_birth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.birth_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        PlayOn();
    }

    private void PlayOn() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://today.zenquotes.io/api/29/10";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("data");
                    Log.i(TAG, "onResponse: " + jsonObject);
                    jsonArray = jsonObject.getJSONArray("Births");

                    int s = jsonArray.length();
                    if(s>50)
                    {
                        s = 50;
                    }
                    for(int n = 0; n < s; n++)
                    {
                        jsonObject = jsonArray.getJSONObject(n);
                        String currentString = jsonObject.getString("text");
                        String[] separated = currentString.split("&");
                        Log.i(TAG, "onResponse: " + separated[0]);
                        String born = separated[0];
                        currentString = separated[1];
                        separated = currentString.split(";");
                        Log.i(TAG, "onResponse: " +  separated[1]);
                        currentString = separated[1];
                        separated = currentString.split("\\(d.");
                        String name = separated[0];
                        currentString = separated[1];
                        separated = currentString.split("\\)");
                        arrayList3.add(born + " - " + separated[0]);

                        if(name.indexOf(",") !=-1)
                        {
                            separated = name.split(", ");
                            arrayList.add(separated[0]);
                            arrayList2.add(separated[1]);
                        }
                        else
                        {
                            arrayList.add(name);
                            arrayList2.add("");
                        }
                    }
                    birthAdapter = new BirthAdapter(arrayList, arrayList2, arrayList3, getActivity());
                    recyclerView.setAdapter(birthAdapter); // set the Adapter to RecyclerView
                    progressDialog.dismiss();

                    birthAdapter.notifyDataSetChanged();
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