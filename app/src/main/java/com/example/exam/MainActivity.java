package com.example.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.MobileDetailsAdapter;
import model.MobileDetails;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    FirebaseUser user;
    TextView textView;

    private RecyclerView recyclerView;
    private Context context;
    private RequestQueue requestQueue;
    private List<MobileDetails> mobileDetailsList = new ArrayList<>();
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        init();
        requestJsonData();

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);

        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init() {

        recyclerView = findViewById(R.id.mobiles_rv);
        context = MainActivity.this;

    }

    public void requestJsonData() {
        requestQueue = Volley.newRequestQueue(context);


        stringRequest = new StringRequest(Request.Method.GET, "https://dummyjson.com/products", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                    fetchTheData(jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Api call error");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        requestQueue.add(stringRequest);
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void fetchTheData(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject mobile = jsonArray.getJSONObject(i);
                mobileDetailsList.add(new MobileDetails(
                        mobile.getString("title"),
                        mobile.getString("description"),
                        mobile.getString("rating"),
                        mobile.getDouble("price"),
                        mobile.getString("thumbnail")
                ));
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Mobile detail error");
            }
        }

        MobileDetailsAdapter adapter = new MobileDetailsAdapter(mobileDetailsList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}



