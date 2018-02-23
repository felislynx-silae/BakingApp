package eu.lynxit.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.lynxit.bakingapp.model.Ingredient;

/**
 * Created by lynx on 15/02/18.
 */

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Ingredient> mItems = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((IngredientHolder) (holder)).bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void replaceItems(List<Ingredient> newList) {
        mItems.clear();
        mItems.addAll(newList);
        notifyDataSetChanged();
    }

    class IngredientHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mQuantity;
        private TextView mUnit;

        public IngredientHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.listview_item_ingredient_name);
            mQuantity = itemView.findViewById(R.id.listview_item_ingredient_quantity);
            mUnit = itemView.findViewById(R.id.listview_item_ingredient_unit);
        }

        public void bind(final Ingredient ingredientDTO) {
            mName.setText(ingredientDTO.getIngredient());
            mQuantity.setText(String.format("$1%2d", ingredientDTO.getQuantity()));
            mUnit.setText(ingredientDTO.getMeasure());
        }
    }
}