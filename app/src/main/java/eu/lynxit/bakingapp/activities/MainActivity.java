package eu.lynxit.bakingapp.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.lynxit.bakingapp.MainViewModel;
import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.fragments.RecipeListFragment;

public class MainActivity extends AppCompatActivity {
    private Fragment mCurrentFragment;
    private ImageView mBackButton;
    public MainViewModel mViewModel;
    private View.OnClickListener mOnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = findViewById(R.id.activity_main_toolbar_title);
        mBackButton = findViewById(R.id.activity_main_toolbar_back);
        mBackButton.setOnClickListener(mOnBackClickListener);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment != null && fragment.isVisible()) {
                        mCurrentFragment = fragment;
                    }
                }

                if(mCurrentFragment instanceof RecipeListFragment){
                    mBackButton.setVisibility(View.GONE);
                } else {
                    mBackButton.setVisibility(View.VISIBLE);
                }
            }
        });
        if (savedInstanceState == null) {
            startFragment(new RecipeListFragment(), false, false);
        } else {
            List<Fragment> tmp = new ArrayList<Fragment>();
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof RecipeListFragment)
                    tmp.add(fragment);
            }
            if (tmp.size() > 0) {
                Fragment fragment = tmp.get(tmp.size() - 1);
                if (fragment instanceof RecipeListFragment) {
                    mCurrentFragment = fragment;
                }
            }
        }
    }

    public void startFragment(Fragment fragment, Boolean addToBackStack, Boolean pop) {
        if (fragment != null) {
            if (pop) {
                getSupportFragmentManager().popBackStack();
            }
            mCurrentFragment = fragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment, null);
            if (addToBackStack) {
                transaction.addToBackStack(fragment.getTag());
            }
            if(mCurrentFragment instanceof RecipeListFragment){
                mBackButton.setVisibility(View.GONE);
            } else {
                mBackButton.setVisibility(View.VISIBLE);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    public void setTitle(final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTitle.setText(title);
            }
        });
    }
}
