package com.example.aidan.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.aidan.bakingapp.Fragments.IngredientsStepsFragment;
import com.example.aidan.bakingapp.Fragments.VideoPlayerFragment;
import com.example.aidan.bakingapp.Models.Bakes;

import butterknife.ButterKnife;

import static com.example.aidan.bakingapp.Adapters.StepsListAdapter.SELECTED_POSITION_EXTRA;
import static com.example.aidan.bakingapp.Adapters.StepsListAdapter.STEPS_EXTRA;
import static com.example.aidan.bakingapp.Fragments.IngredientsStepsFragment.CLICKED_STATE;
import static com.example.aidan.bakingapp.MainActivity.BAKES_EXTRA;

public class BakesDetailsActivity extends AppCompatActivity implements IngredientsStepsFragment.OnStepClickListener {

    Bakes bakes = null;
    IngredientsStepsFragment ingredientsStepsFragment;
    VideoPlayerFragment videoPlayerFragment;
    FragmentManager fragmentManager;
    private boolean isTab;
    int clickedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakes_details);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();

        isTab = getResources().getBoolean(R.bool.isTablet);
        if (intent.hasExtra(BAKES_EXTRA)) {
            bakes = intent.getParcelableExtra(BAKES_EXTRA);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bakes.getBakeName());
                getSupportActionBar().setElevation(0);
            }
            initIngredientsStepsFragment();

            if(isTab) {
                if (savedInstanceState != null) {
                    clickedPosition = savedInstanceState.getInt(CLICKED_STATE);
                    updateVideoPlayerFragment(clickedPosition, bakes);
                } else {
                    updateVideoPlayerFragment(0, bakes);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CLICKED_STATE, clickedPosition);
    }

    private void initIngredientsStepsFragment() {
        ingredientsStepsFragment = new IngredientsStepsFragment();
        ingredientsStepsFragment.setIngredientsList(bakes.getIngredientsList());
        ingredientsStepsFragment.setStepsList(bakes.getStepsList());
        ingredientsStepsFragment.setBakes(bakes);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_ingredients_steps_layout, ingredientsStepsFragment)
                .commit();
    }

    private void updateVideoPlayerFragment(int position, Bakes bakes) {
        videoPlayerFragment = new VideoPlayerFragment();
        videoPlayerFragment.setStepPosition(position);
        videoPlayerFragment.setBake(bakes);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_video_layout, videoPlayerFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onStepClicked(int position) {
        if (isTab) {
            clickedPosition = position;
            updateVideoPlayerFragment(position, bakes);
        } else {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(STEPS_EXTRA, bakes);
            intent.putExtra(SELECTED_POSITION_EXTRA, position);
            startActivity(intent);
        }
    }
}
