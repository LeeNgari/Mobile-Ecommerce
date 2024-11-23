package com.example.exam;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import model.MobileDetails;

public class ProductDetails extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileDetails product = (MobileDetails) getIntent().getSerializableExtra("mobileDetails");

        TextView detailTitleTextView = findViewById(R.id.detailTitleTextView);
        TextView detailDescriptionTextView = findViewById(R.id.detailDescriptionTextView);
        TextView detailPriceTextView = findViewById(R.id.detailPriceTextView);
        TextView detailRatingTextView = findViewById(R.id.detailRatingTextView);
        TextView detailCategoryTextView = findViewById(R.id.detailCategoryTextView);
        ImageView picImageView = findViewById(R.id.detailPicImageView);

        detailTitleTextView.setText(product.getName());
        detailDescriptionTextView.setText("Ksh " + product.getDescription());
        detailPriceTextView.setText("Ksh " + product.getPrice());
        detailRatingTextView.setText("Rating: " + product.getRating());
        detailCategoryTextView.setText("Category: " + product.getCategory());
        Picasso.get().load(product.getPic()).into(picImageView);
    }
}