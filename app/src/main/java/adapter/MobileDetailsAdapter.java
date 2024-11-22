package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.ProductDetails;
import com.example.exam.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.CartItem;
import model.MobileDetails;
import utilities.CartManager;

public class MobileDetailsAdapter extends RecyclerView.Adapter<MobileDetailsAdapter.MobileDetailsHolder> {

    private List<MobileDetails> mobileDetailsList;
    private Context context;

    public MobileDetailsAdapter(List<MobileDetails> mobileDetailsList, Context context) {
        this.mobileDetailsList = mobileDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MobileDetailsAdapter.MobileDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new MobileDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobileDetailsAdapter.MobileDetailsHolder holder, int position) {
        MobileDetails mobileDetails = mobileDetailsList.get(position);

        holder.mobileName.setText(mobileDetails.getName());
        holder.mobilePrice.setText("Ksh " + mobileDetails.getPrice());
        holder.mobileRating.setText(mobileDetails.getRating());

        // Load product image using Picasso
        Picasso.get().load(mobileDetails.getPic()).into(holder.mobilePic);

        // Handle "Add to Cart" button click
        holder.addToCartButton.setOnClickListener(v -> {
            // Create a CartItem with the mobile details, including the image URL
            CartItem cartItem = new CartItem(
                    mobileDetails.getName(),
                    mobileDetails.getDescription(),  // Assuming this field is available in the MobileDetails class
                    mobileDetails.getPrice(),
                    1,  // Default quantity is 1
                    mobileDetails.getPic()  // Pass the image URL as part of the cart item
            );

            // Add the item to the cart using CartManager
            CartManager.getInstance().addItemToCart(cartItem);
            Toast.makeText(context, mobileDetails.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });

        // Handle item click to open product details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("mobileDetails", mobileDetails);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mobileDetailsList.size();
    }

    public class MobileDetailsHolder extends RecyclerView.ViewHolder {

        private TextView mobileName, mobilePrice, mobileRating;
        private ImageView mobilePic;
        private Button addToCartButton;

        public MobileDetailsHolder(@NonNull View itemView) {
            super(itemView);

            mobileName = itemView.findViewById(R.id.name);
            mobilePrice = itemView.findViewById(R.id.price);
            mobileRating = itemView.findViewById(R.id.rating);
            mobilePic = itemView.findViewById(R.id.img);
            addToCartButton = itemView.findViewById(R.id.addToCart);
        }
    }
}
