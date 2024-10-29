package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MobileDetails;

public class MobileDetailsAdapter extends RecyclerView.Adapter<MobileDetailsAdapter.MobileDetailsHolder> {

    // List of mobile details to be displayed
    private List<MobileDetails> mobileDetailsList;
    // Context for inflating views
    private Context context;

    // Constructor to initialize the adapter
    public MobileDetailsAdapter(List<MobileDetails> mobileDetailsList, Context context) {
        this.mobileDetailsList = mobileDetailsList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MobileDetailsAdapter.MobileDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new MobileDetailsHolder(view); // Return a new ViewHolder instance
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MobileDetailsAdapter.MobileDetailsHolder holder, int position) {
        // Get the mobile details for the current position
        MobileDetails mobileDetails = mobileDetailsList.get(position);

        // Set the data to the views
        holder.mobileName.setText(mobileDetails.getName());
        holder.mobilePrice.setText("Ksh " + mobileDetails.getPrice());
        holder.mobileRating.setText(mobileDetails.getRating());

        // Load the image using Picasso library
        Picasso.get().load(mobileDetails.getPic()).into(holder.mobilePic);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mobileDetailsList.size();
    }

    // ViewHolder class to hold the views for each item
    public class MobileDetailsHolder extends RecyclerView.ViewHolder {

        // UI elements for mobile details
        private TextView mobileName, mobilePrice, mobileRating;
        private ImageView mobilePic;

        public MobileDetailsHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize UI elements
            mobileName = itemView.findViewById(R.id.name);
            mobilePrice = itemView.findViewById(R.id.price);
            mobileRating = itemView.findViewById(R.id.rating);
            mobilePic = itemView.findViewById(R.id.img);
        }
    }
}
