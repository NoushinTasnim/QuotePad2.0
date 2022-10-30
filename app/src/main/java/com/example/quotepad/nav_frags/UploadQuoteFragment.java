package com.example.quotepad.nav_frags;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.model.QuotesModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadQuoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadQuoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputLayout upload_quote;
    private Button upload_btn;
    private AutoCompleteTextView autoCompleteTextView;
    private CheckBox publicity;

    private String type;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public UploadQuoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadQuoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadQuoteFragment newInstance(String param1, String param2) {
        UploadQuoteFragment fragment = new UploadQuoteFragment();
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
        return inflater.inflate(R.layout.fragment_upload_quote, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        upload_btn = getActivity().findViewById(R.id.upload_btn);
        upload_quote = getActivity().findViewById(R.id.upload_quote);

        autoCompleteTextView = getActivity().findViewById(R.id.quoteType);
        publicity = getActivity().findViewById(R.id.upload_publicity);

        //We will use this data to inflate the drop-down items
        String[] Subjects = new String[]{   "Happy", "Gloomy", "Romantic", "Inspiring",
                "Past", "Death", "Fear", "Success", "Failure",
                "Future", "Loneliness", "Confidence"};

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Subjects);
        autoCompleteTextView.setAdapter(adapter);

        //to get selected value add item click listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = autoCompleteTextView.getText().toString().trim();
                //Toast.makeText(getActivity(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //Get all the values
                String quote = upload_quote.getEditText().getText().toString().trim();

                upload_quote.setErrorEnabled(false);

                if(TextUtils.isEmpty(quote))
                {
                    upload_quote.setError("Type your quote");
                }
                else if(TextUtils.isEmpty(type)){
                    autoCompleteTextView.setError("Please select a genre for your quote");
                }
                else if(quote.length() < 15){
                    upload_quote.setError("Too Short");
                }
                else
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd LLLL,yyyy HH:mm:ss", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference();
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    QuotesModel helperClass = new QuotesModel("\"" + quote + "\"",type,currentDateTime);
                    reference.child("users").child(currentuser).child("quote").child(currentDateTime).setValue(helperClass);
                    Query checkUser = reference.child("users").orderByChild("id").equalTo(currentuser);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String username = snapshot.child(currentuser).child("username").getValue(String.class);
                                String new_id = currentDateTime + ";" + currentuser;

                                if(publicity.isChecked())
                                {
                                    QuotesModel discoverModel = new QuotesModel("\"" + quote + "\"",currentDateTime,type, username, new_id);
                                    reference.child("quotes").child(new_id).setValue(discoverModel);
                                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                publicity.setChecked(false);
                                upload_quote.getEditText().setText("");
                                autoCompleteTextView.setText("");

                                Log.i(TAG, "onDataChange: " + username);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });//Register Button method end
    }
}