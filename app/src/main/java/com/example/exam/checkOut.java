package com.example.exam;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import adapter.CheckOutAdapter;
import model.CartItem;

import java.util.ArrayList;

public class checkOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the cart items from the intent
        ArrayList<CartItem> cartItems = getIntent().getParcelableArrayListExtra("cartItems");

        Log.d("CheckOutActivity", "Cart Items: " + cartItems.size());

        // Initialize UI elements
        TextView deliveryFeeTextView = findViewById(R.id.delivery_fee);
        TextView internationalCustomsFeeTextView = findViewById(R.id.international_customs_fee);
        TextView totalTextView = findViewById(R.id.total);
        RecyclerView cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view);

        // Set up RecyclerView with CheckOutAdapter
        CheckOutAdapter checkOutAdapter = new CheckOutAdapter(cartItems);
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemsRecyclerView.setAdapter(checkOutAdapter);

        Log.d("CheckOutActivity", "Adapter set: " + (cartItemsRecyclerView.getAdapter() != null));

        // Calculate and display the total price
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        totalTextView.setText("Total: Ksh " + totalPrice);

        // Hardcoded delivery and international customs fees
        deliveryFeeTextView.setText("Delivery fees: KSH 359");
        internationalCustomsFeeTextView.setText("International Customs Fee: KSH 168");

        // Confirm Order Button
        Button confirmOrderButton = findViewById(R.id.confirm_order_button);
        confirmOrderButton.setOnClickListener(v -> {
            // Handle the order confirmation logic here
        });
    }
}