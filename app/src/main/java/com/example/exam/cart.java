package com.example.exam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import adapter.CartAdapter;
import model.CartItem;
import utilities.CartManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cart extends AppCompatActivity implements CartAdapter.OnCartUpdatedListener {

    private RecyclerView recyclerView;
    private TextView totalPriceTextView;
    private CartAdapter cartAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price);
        Button checkoutButton = findViewById(R.id.checkout_button);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView with CartAdapter
        cartAdapter = new CartAdapter(CartManager.getInstance().getCartItems(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        // Register as a listener for cart updates
        CartManager.getInstance().setOnCartUpdatedListener(this);

        updateTotalPrice();

        checkoutButton.setOnClickListener(v -> {
            proceedToCheckout();
        });
    }

    @Override
    public void onCartUpdated() {
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            totalPrice += item.getTotalPrice();
        }
        totalPriceTextView.setText("Ksh " + totalPrice);
    }

    private void proceedToCheckout() {
        // Get the current user's UID
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            // Prepare the cart items to add to Firestore
            List<Map<String, Object>> orderHistory = new ArrayList<>();
            for (CartItem item : CartManager.getInstance().getCartItems()) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("title", item.getTitle());
                itemData.put("price", item.getPrice());
                itemData.put("imageUrl", item.getImageUrl());
                itemData.put("quantity", item.getQuantity());
                orderHistory.add(itemData);
            }

            // Update Firestore: Add cart items to orderHistory in the user's document
            for (Map<String, Object> itemData : orderHistory) {
                db.collection("users").document(userId)
                        .update("orderHistory", FieldValue.arrayUnion(itemData))
                        .addOnSuccessListener(aVoid -> {
                            // Clear the cart after successful checkout
                            CartManager.getInstance().clearCart();
                            if (cartAdapter != null) {
                                cartAdapter.notifyDataSetChanged();  // Refresh the cart view
                            }

                            Toast.makeText(cart.this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                            finish();  // Close the activity after checkout
                        })
                        .addOnFailureListener(e -> {
                            e.printStackTrace();  // Log the error for debugging
                            Toast.makeText(cart.this, "Failed to checkout. Please try again.", Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(cart.this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartManager.getInstance().setOnCartUpdatedListener(null); // Unregister the listener
    }
}