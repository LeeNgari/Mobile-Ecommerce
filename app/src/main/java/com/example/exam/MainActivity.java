package com.example.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.MobileDetailsAdapter;
import model.MobileDetails;

public class MainActivity extends AppCompatActivity {

    // Firebase authentication and Firestore instances
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // UI components
    Button button;
    TextView textView;
    TextInputEditText searchEditText;
    Spinner categoriesSpinner;

    // User data
    FirebaseUser user;
    String firstName, lastName;

    // RecyclerView for displaying mobile details
    private RecyclerView recyclerView;
    private Context context;
    private RequestQueue requestQueue;
    private List<MobileDetails> mobileDetailsList = new ArrayList<>();
    private StringRequest stringRequest;
    private MobileDetailsAdapter adapter;

    // Search query and selected category
    private String searchQuery = "";
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge mode for the activity
        setContentView(R.layout.activity_main);

        // Set up window insets to avoid UI overlaps
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton cartIcon = findViewById(R.id.cart_icon);
        ImageButton settingsIcon = findViewById(R.id.settings_icon);

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to CartActivity
                Intent cartIntent = new Intent(MainActivity.this, cart.class);
                startActivity(cartIntent);
            }
        });

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to SettingsActivity
                Intent settingsIntent = new Intent(MainActivity.this, account.class);
                startActivity(settingsIntent);
            }
        });

        // Initialize components and fetch data
        init();
        requestJsonData(searchQuery, selectedCategory);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.user_details);

        // Get the currently authenticated user
        user = auth.getCurrentUser();

        // Fetch user details from Firestore
        db.collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                firstName = document.getString("firstName");
                                lastName = document.getString("lastName");
                                textView.setText("Hi there, " + firstName + " " + lastName);
                            } else {
                                Log.d("User", "No such document!");
                            }
                        } else {
                            Log.w("User", "Error getting document.", task.getException());
                        }
                    }
                });

        // Redirect to login if no user is authenticated
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        // Set up search functionality
        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                requestJsonData(searchQuery, selectedCategory);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up category selection
        categoriesSpinner = findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                requestJsonData(searchQuery, selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Initialize UI components
    public void init() {
        recyclerView = findViewById(R.id.mobiles_rv);
        context = MainActivity.this;
    }

    // Request mobile data from API
    public void requestJsonData(String query, String category) {
        requestQueue = Volley.newRequestQueue(context); // Create a new request queue

        String url = "https://dummyjson.com/products";
        if (!query.isEmpty()) {
            url += "/search?q=" + query;
        } else if (!category.isEmpty()) {
            url += "/category/" + category;
        }

        // Set up the string request for the API call
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            fetchTheData(jsonArray); // Process the data received
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("API call error");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        // Add the request to the request queue
        requestQueue.add(stringRequest);
    }

    // Display a toast message
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    // Process the JSON data and update the RecyclerView
    private void fetchTheData(JSONArray jsonArray) {
        mobileDetailsList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject mobile = jsonArray.getJSONObject(i);
                mobileDetailsList.add(new MobileDetails(
                        mobile.getString("title"),
                        mobile.getString("description"),
                        mobile.getString("rating"),
                        mobile.getDouble("price"),
                        mobile.getString("thumbnail"),
                        mobile.getString("category") // Add category here
                ));
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Mobile detail error");
            }
        }

        // Set up the adapter for the RecyclerView
        adapter = new MobileDetailsAdapter(mobileDetailsList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}