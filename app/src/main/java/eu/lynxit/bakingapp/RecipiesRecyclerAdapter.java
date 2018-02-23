package eu.lynxit.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.lynxit.bakingapp.model.Recipe;

/**
 * Created by lynx on 15/02/18.
 */

public class RecipiesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Recipe> mItems = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecipieHolder) (holder)).bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void replaceItems(List<Recipe> newList) {
        mItems.clear();
        mItems.addAll(newList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    class RecipieHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        public RecipieHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.listview_item_recipe_name);
        }

        public void bind(final Recipe recipeDTO, final Integer position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClicked(position, recipeDTO);
                    }
                }
            });
            mName.setText(recipeDTO.getName());
            /*RequestBuilder builder = Glide.with(thumbnailView).load(BuildConfig.IMG_ENDPOINT+"w185" + recipeDTO.getPoster_path());
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_movie);
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            builder.apply(options);
            builder.into(thumbnailView);*/
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Integer position, Recipe recipe);
    }
}