package eu.lynxit.bakingapp.fragments;

import android.arch.lifecycle.Observer;
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

import java.util.List;

import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.adapters.RecipiesRecyclerAdapter;
import eu.lynxit.bakingapp.activities.MainActivity;
import eu.lynxit.bakingapp.model.Recipe;

/**
 * Created by lynx on 22/02/18.
 */

public class RecipeListFragment extends Fragment {
    private RecipiesRecyclerAdapter adapter = new RecipiesRecyclerAdapter();
    private Observer mRecipesObserver = new Observer<List<Recipe>>() {
        @Override
        public void onChanged(@Nullable List<Recipe> recipes) {
            adapter.replaceItems(recipes);
        }
    };
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
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecipiesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Integer position, Recipe recipe) {
                ((MainActivity) getActivity()).mViewModel.setSelectedRecipe(recipe);
                ((MainActivity) getActivity()).startFragment(new RecipeFragment(), true, false);
            }
        });
        ((MainActivity) getActivity()).mViewModel.mRecipes.observe(this,mRecipesObserver);
        ((MainActivity) getActivity()).mViewModel.initializeRecipes();
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).mViewModel.mRecipes.removeObserver(mRecipesObserver);
    }
}
