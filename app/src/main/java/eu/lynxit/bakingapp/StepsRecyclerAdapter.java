package eu.lynxit.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.lynxit.bakingapp.model.Step;

/**
 * Created by lynx on 15/02/18.
 */

public class StepsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Step> mItems = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private int selectedItem = -1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_step, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((StepHolder) (holder)).bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void replaceItems(List<Step> newList) {
        mItems.clear();
        mItems.addAll(newList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    class StepHolder extends RecyclerView.ViewHolder {
        private TextView mName;

        public StepHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.listview_item_step_name);
        }

        public void bind(final Step stepDTO, final Integer position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClicked(position, stepDTO);
                    }
                }
            });
            /*RequestBuilder builder = Glide.with(thumbnailView).load(BuildConfig.IMG_ENDPOINT+"w185" + stepDTO.getPoster_path());
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_movie);
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            builder.apply(options);
            builder.into(thumbnailView);*/
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Integer position, Step step);
    }
}