package adapter;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import model.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    private List<CartItem> cartItems;

    public CheckOutAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CheckOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item_layout, parent, false);
        return new CheckOutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutViewHolder holder, int position) {
        Log.d("CheckOutAdapter", "Binding item at position: " + position);
        CartItem item = cartItems.get(position);

        // Bind data to views
        holder.title.setText(item.getTitle());
        holder.price.setText("Ksh " + item.getTotalPrice());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // Load product image using Picasso
        Log.d("CheckOutAdapter", "Image URL: " + item.getImageUrl());
        Picasso.get().load(item.getImageUrl()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CheckOutViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, quantity;
        ImageView productImage;

        public CheckOutViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.checkout_item_title);
            price = itemView.findViewById(R.id.checkout_item_price);
            quantity = itemView.findViewById(R.id.checkout_item_quantity);
            productImage = itemView.findViewById(R.id.checkout_item_image);
        }
    }
}
