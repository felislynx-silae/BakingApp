package eu.lynxit.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.model.Step;

/**
 * Created by lynx on 15/02/18.
 */

public class StepsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Step> mItems = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

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
        private TextView mId;

        public StepHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.listview_item_step_name);
            mId = itemView.findViewById(R.id.listview_item_step_id);
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
            mId.setText(position.toString());
            mName.setText(stepDTO.getShortDescription());
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Integer position, Step step);
    }
}