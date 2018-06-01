package com.example.aidan.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.aidan.bakingapp.Adapters.IngredientsListAdapter;
import com.example.aidan.bakingapp.Adapters.StepsListAdapter;
import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.Models.Ingredients;
import com.example.aidan.bakingapp.Models.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aidan.bakingapp.MainActivity.BAKES_EXTRA;

public class BakesDetailsActivity extends AppCompatActivity {

    @BindView(R.id.rv_ingredients_list)
    RecyclerView rvIngredientsList;
    @BindView(R.id.rv_steps_list)
    RecyclerView rvStepsList;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;

    IngredientsListAdapter ingredientsListAdapter;
    StepsListAdapter stepsListAdapter;
    LinearLayoutManager lmIngredientsLayout;

    Bakes bakes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakes_details);
        ButterKnife.bind(this);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsListAdapter = new IngredientsListAdapter(ingredientsList);
        rvIngredientsList.setAdapter(ingredientsListAdapter);

        List<Steps> stepsList = new ArrayList<>();
        stepsListAdapter = new StepsListAdapter(stepsList,bakes);
        rvStepsList.setAdapter(stepsListAdapter);

        Intent intent = getIntent();
        if(intent.hasExtra(BAKES_EXTRA)){
            bakes = intent.getParcelableExtra(BAKES_EXTRA);
            generateIngredientsList(bakes.getIngredientsList());
            generateStepsList(bakes.getStepsList());
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bakes.getBakeName());
                getSupportActionBar().setElevation(0);
            }
        }
    }

    @OnClick(R.id.iv_next)
    void nextInRecyclerView(){
        int itemPosition = lmIngredientsLayout.findLastVisibleItemPosition() + 1;
        ivPrevious.setVisibility(View.VISIBLE);
        if(itemPosition >= (ingredientsListAdapter.getItemCount()-1)){
            ivNext.setVisibility(View.INVISIBLE);
        }else{
            ivNext.setVisibility(View.VISIBLE);
        }
        rvIngredientsList.getLayoutManager().scrollToPosition(lmIngredientsLayout.findLastVisibleItemPosition() + 1);
    }

    @OnClick(R.id.iv_previous)
    void previousInRecyclerView(){
        int itemPosition = lmIngredientsLayout.findLastVisibleItemPosition() - 1;
        ivNext.setVisibility(View.VISIBLE);
        if(itemPosition <= 0){
            ivPrevious.setVisibility(View.INVISIBLE);
        }else{
            ivPrevious.setVisibility(View.VISIBLE);
        }
        rvIngredientsList.getLayoutManager().scrollToPosition(lmIngredientsLayout.findLastVisibleItemPosition() - 1);
    }

    void generateIngredientsList(List<Ingredients> ingredientsList){
        lmIngredientsLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ingredientsListAdapter = new IngredientsListAdapter(ingredientsList);
        rvIngredientsList.setLayoutManager(lmIngredientsLayout);
        rvIngredientsList.setAdapter(ingredientsListAdapter);
    }

    void generateStepsList(List<Steps> stepsList){
        LinearLayoutManager lmStepsLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsListAdapter = new StepsListAdapter(stepsList,bakes);
        rvStepsList.setLayoutManager(lmStepsLayout);
        rvStepsList.setAdapter(stepsListAdapter);
        rvStepsList.setHasFixedSize(true);
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
}
