package com.example.quotepad.nav_frags.today;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quotepad.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuoteOfTheDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuoteOfTheDayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String quo, aut;
    private TextView quote, author;

    public QuoteOfTheDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuoteOfTheDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuoteOfTheDayFragment newInstance(String param1, String param2) {
        QuoteOfTheDayFragment fragment = new QuoteOfTheDayFragment();
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
        return inflater.inflate(R.layout.fragment_quote_of_the_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        quote = getActivity().findViewById(R.id.today_quote);
        author =  getActivity().findViewById(R.id.today_author);

        loadData();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://zenquotes.io/api/today";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            quo = jsonObject.getString("q");
                            aut = jsonObject.getString("a");

                            quo =  "\"" + quo +  "\"" ;

                            //Log.i(TAG, "onResponse:  "+quo + " " + aut);
                            setTextToTextView(quo," - " + aut);
                            quote.setText(quo);
                            author.setText("-"+aut);
                            progressDialog.dismiss();
                            btnSaveData(quo,aut);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void loadData() {
        File file = getActivity()
                .getFileStreamPath("QuoteOfTheDay.txt");

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getActivity().openFileInput("QuoteOfTheDay.txt")));

                String st, qu = "";

                while ((st = reader.readLine()) != null){
                    qu = qu + st;
                }
                String[] separated = qu.split("\"");
                Log.i(TAG, "loadData: " + separated[1]);
                String separated2[] = qu.split("; ");
                    // Print the string
                Log.i(TAG, "loadData: " + separated2[1]);
                reader.close();
                setTextToTextView(separated2[0],separated2[1]);
            } catch (IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void setTextToTextView(String quotes, String auths) {
        quote.setText(quotes);
        author.setText("-"+ auths);
    }

    public void btnSaveData(String quotes, String author) {
        try {
            FileOutputStream file = getActivity().openFileOutput("QuoteOfTheDay.txt", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(file);

            outputStreamWriter.write(quotes + "; " + author);

            outputStreamWriter.flush();
            outputStreamWriter.close();
            Toast.makeText(getActivity(), "Successfully saved", Toast.LENGTH_LONG)
                    .show();

        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

}