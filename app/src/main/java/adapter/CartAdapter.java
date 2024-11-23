package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private OnCartUpdatedListener cartUpdatedListener;

    public CartAdapter(List<CartItem> cartItems, OnCartUpdatedListener cartUpdatedListener) {
        this.cartItems = cartItems;
        this.cartUpdatedListener = cartUpdatedListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Bind data to views
        holder.title.setText(item.getTitle());
        holder.price.setText("Ksh " + item.getTotalPrice());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // Load product image using Picasso
        Picasso.get().load(item.getImageUrl()).into(holder.productImage);

        // Increase quantity
        holder.increaseQuantity.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            cartUpdatedListener.onCartUpdated();
            notifyItemChanged(position); // Refresh the updated item
        });

        // Decrease quantity
        holder.decreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartUpdatedListener.onCartUpdated();
                notifyItemChanged(position);
            }
        });

        // Remove item
        holder.removeItem.setOnClickListener(v -> {
            cartItems.remove(position);
            cartUpdatedListener.onCartUpdated();
            notifyItemRemoved(position); // Notify item removal
            notifyItemRangeChanged(position, cartItems.size()); // Adjust remaining items
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, quantity;
        Button increaseQuantity, decreaseQuantity, removeItem;
        ImageView productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cart_item_title);
            price = itemView.findViewById(R.id.cart_item_price);
            quantity = itemView.findViewById(R.id.cart_item_quantity);
            increaseQuantity = itemView.findViewById(R.id.increase_quantity);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);
            removeItem = itemView.findViewById(R.id.remove_item);
            productImage = itemView.findViewById(R.id.cart_item_image); // Added ImageView for displaying image
        }
    }

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }
}