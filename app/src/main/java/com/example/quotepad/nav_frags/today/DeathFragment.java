package com.example.quotepad.nav_frags.today;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.quotepad.R;
import com.example.quotepad.adapter.OnThisDayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeathFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList arrayList = new ArrayList<>();
    ArrayList arrayList2 = new ArrayList<>();
    ArrayList arrayList3 = new ArrayList<>();
    OnThisDayAdapter onThisDayAdapter;
    String name;

    RecyclerView recyclerView;

    public DeathFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeathFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeathFragment newInstance(String param1, String param2) {
        DeathFragment fragment = new DeathFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.death_rv);

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        Log.i(TAG, "PlayOn: " + currentDateTime);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://today.zenquotes.io/api/" + currentDateTime;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("data");
                    Log.i(TAG, "onResponse: " + jsonObject);
                    jsonArray = jsonObject.getJSONArray("Deaths");
                    arrayList3.clear();
                    arrayList2.clear();
                    arrayList.clear();
                    int s = jsonArray.length();

                    for(int n = 0; n < s; n++)
                    {
                        jsonObject = jsonArray.getJSONObject(n);
                        String currentString = jsonObject.getString("text");
                        String[] separated = currentString.split("&");
                        Log.i(TAG, "onResponse: " + separated[0]);
                        String death = separated[0];
                        currentString = separated[1];
                        separated = currentString.split("; ");
                        Log.i(TAG, "onResponse: " +  separated[1]);
                        currentString = separated[1];

                        if(currentString.indexOf("b.") !=-1)
                        {
                            Log.i(TAG, "onResponse: here");
                            separated = currentString.split("\\(b.");
                            name = separated[0];
                            Log.i(TAG, "onResponse: 1 " + name);
                            currentString = separated[1];
                            Log.i(TAG, "onResponse: 1 " + currentString);
                            separated = currentString.split("\\)");
                            arrayList3.add(separated[0] + " - " + death);
                        }
                        else
                        {
                            name = currentString;
                            arrayList3.add(" - " + death);
                        }

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
                    progressDialog.dismiss();
                    onThisDayAdapter = new OnThisDayAdapter(arrayList, arrayList2, arrayList3, getActivity());
                    recyclerView.setAdapter(onThisDayAdapter); // set the Adapter to RecyclerView

                    onThisDayAdapter.notifyDataSetChanged();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_death, container, false);
    }
}