package eu.lynxit.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.lynxit.bakingapp.adapters.IngredientsRecyclerAdapter;
import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.adapters.StepsRecyclerAdapter;
import eu.lynxit.bakingapp.activities.MainActivity;
import eu.lynxit.bakingapp.model.Recipe;
import eu.lynxit.bakingapp.model.Step;

/**
 * Created by lynx on 22/02/18.
 */

public class RecipeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Recipe selectedRecipe = ((MainActivity) getActivity()).mViewModel.getSelectedRecipe();
        RecyclerView ingredients = view.findViewById(R.id.fragment_recipe_steps_ingredients);
        ingredients.setLayoutManager(new LinearLayoutManager(getContext()));
        IngredientsRecyclerAdapter ingredientsAdapter = new IngredientsRecyclerAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                ((LinearLayoutManager) ingredients.getLayoutManager()).getOrientation());
        ingredients.addItemDecoration(dividerItemDecoration);
        ingredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.replaceItems(selectedRecipe.getIngredients());
        RecyclerView steps = view.findViewById(R.id.fragment_recipe_steps);
        steps.setLayoutManager(new LinearLayoutManager(getContext()));
        StepsRecyclerAdapter stepsAdapter = new StepsRecyclerAdapter();
        stepsAdapter.setOnItemClickListener(new StepsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Integer position, Step step) {
                ((MainActivity) getActivity()).mViewModel.setSelectedStep(step);
                StepFragment fragment = new StepFragment();
                ((MainActivity) getActivity()).startFragment(fragment, true, false);
            }
        });
        steps.setAdapter(stepsAdapter);
        stepsAdapter.replaceItems(selectedRecipe.getSteps());
        if (view.findViewById(R.id.fragment_recipe_step_frame) != null) {
            ((MainActivity) getActivity()).mViewModel.setSelectedStep(selectedRecipe.getSteps().get(0));
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_recipe_step_frame, new StepFragment()).commit();
            stepsAdapter.setOnItemClickListener(new StepsRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(Integer position, Step step) {
                    ((MainActivity) getActivity()).mViewModel.setSelectedStep(step);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_recipe_step_frame, new StepFragment()).commit();
                }
            });
        }
        ((MainActivity) getActivity()).setTitle(selectedRecipe.getName());
    }
}
