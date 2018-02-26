package eu.lynxit.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.adapters.RecipiesRecyclerAdapter;
import eu.lynxit.bakingapp.activities.MainActivity;
import eu.lynxit.bakingapp.model.Recipe;

/**
 * Created by lynx on 22/02/18.
 */

public class RecipeListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_recipe_list_recycler_view);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if(dpWidth>=600){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        RecipiesRecyclerAdapter adapter = new RecipiesRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecipiesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Integer position, Recipe recipe) {
                ((MainActivity) getActivity()).mViewModel.setSelectedRecipe(recipe);
                ((MainActivity) getActivity()).startFragment(new RecipeFragment(), true, false);
            }
        });
        adapter.replaceItems(((MainActivity) getActivity()).mViewModel.getRecipes());
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));

    }
}
