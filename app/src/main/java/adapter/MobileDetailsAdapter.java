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
         holder.mobileName.setText(mobileDetailsList.get(position).getName());
         holder.mobilePrice.setText(String.valueOf(mobileDetailsList.get(position).getPrice()));
         holder.mobileRating.setText(mobileDetailsList.get(position).getRating());
         Picasso.get().load(mobileDetailsList.get(position).getPic()).into(holder.mobilePic);
    }

    @Override
    public int getItemCount(){
        return mobileDetailsList.size();
    }
    public class MobileDetailsHolder extends RecyclerView.ViewHolder  {

        private TextView mobileName, mobilePrice, mobileRating, mobileDescription;
        private ImageView mobilePic;
        public MobileDetailsHolder(@NonNull View itemView) {
            super(itemView);

            mobileName = itemView.findViewById(R.id.name);
            mobilePrice = itemView.findViewById(R.id.price);
            mobileRating = itemView.findViewById(R.id.rating);

            mobilePic = itemView.findViewById(R.id.img);
        }
    }
}
