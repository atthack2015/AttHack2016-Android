package att.atthack2016.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import att.atthack2016.R;
import att.atthack2016.model.PlacePrediction;
import att.atthack2016.ui.adapters.interfaces.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pedro Hernández on 09/2015.
 *
 */
public class SearchResultsAdapter extends RecyclerView.Adapter <SearchResultsAdapter.PlaceViewHolder> {

    Context context;
    List<PlacePrediction> placePredictions;
    OnItemClickListener<PlacePrediction> onItemClickListener;

    public SearchResultsAdapter(Context context) {
        this.placePredictions = new ArrayList<>();
        this.context = context;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_place, parent, false);

        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        PlacePrediction currentResult = placePredictions.get(position);

        String placeName = currentResult.getShortName() != null?
                currentResult.getShortName() : currentResult.description;

        holder.setPlaceName(placeName);
        holder.setPlaceAddress(currentResult.getDescription());
    }

    @Override
    public int getItemCount() {
        return placePredictions.isEmpty() ? 0 : placePredictions.size();

    }

    public void setOnItemClickListener(OnItemClickListener<PlacePrediction> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PlacePrediction getItem(int index){
        return placePredictions.get(index);
    }

    /**
     * Add item in the last index
     *
     * @param placePrediction The item to be inserted
     */
    public void addItem(PlacePrediction placePrediction) {
        if (placePrediction == null)
            throw new NullPointerException("The item cannot be null");

        placePredictions.add(placePrediction);
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * Add item in determined index
     *
     * @param placePrediction    The event to be inserted
     * @param position Index for the new event
     */
    public void addItem(PlacePrediction placePrediction, int position) {
        if (placePrediction == null)
            throw new NullPointerException("The item cannot be null");

        if (position < getItemCount() || position > getItemCount())
            throw new IllegalArgumentException("The position must be between 0 and lastIndex + 1");

        placePredictions.add(position, placePrediction);
        notifyItemInserted(position);
    }

    /**
     * Add a bunch of items
     *
     * @param placePredictions Collection to add
     * */
    public void addAll(List<PlacePrediction> placePredictions) {
        if (placePredictions == null)
            throw new NullPointerException("The items cannot be null");

        this.placePredictions.addAll(placePredictions);
        notifyItemRangeInserted(getItemCount() - 1, placePredictions.size());
    }

    public void replace(List<PlacePrediction> placePredictions){
        this.placePredictions = placePredictions;
        notifyDataSetChanged();
    }

    /**
     * Delete all the items
     * */
    public void clear() {
        if (!placePredictions.isEmpty()) {
            placePredictions.clear();
            notifyDataSetChanged();
        }
    }



    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.text_place_name)
        TextView name;

        @Bind(R.id.text_place_address)
        TextView address;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setPlaceName(String name){
            this.name.setText(name);
        }

        public void setPlaceAddress(String address){
            this.address.setText(address);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClicked(placePredictions.get(getAdapterPosition()));
        }
    }
}
