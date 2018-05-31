package com.example.aidan.bakingapp;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aidan.bakingapp.Adapters.BakesListAdapter;
import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.Models.Ingredients;
import com.example.aidan.bakingapp.Models.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String dataSourceURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static final String BAKE_ID = "id";
    public static final String BAKE_NAME = "name";
    public static final String BAKE_SERVINGS = "servings";
    public static final String BAKE_INGREDIENTS = "ingredients";
    public static final String BAKE_STEPS = "steps";
    public static final String BAKE_IMAGE = "image";

    public static final String INGREDIENT_QTY = "quantity";
    public static final String INGREDIENT_MEASURE = "measure";
    public static final String INGREDIENT_DESC = "ingredient";

    public static final String STEP_ID = "id";
    public static final String STEP_SHORT_DESC = "shortDescription";
    public static final String STEP_DESC = "description";
    public static final String STEP_VIDEO_URL = "videoURL";
    public static final String STEP_THUMB_URL = "thumbnailURL";

    private static final String BAKES_LIST_STATE = "bakes_list_state";

    public static final String BAKES_EXTRA = "bakes_extra";

    @BindView(R.id.rv_bakes_list)
    RecyclerView rvBakesList;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoadingProgress;

    BakesListAdapter bakesListAdapter;
    List<Bakes> bakesList;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.bakes));
        }
        pbLoadingProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        requestQueue = Volley.newRequestQueue(this);
        screenSize();
        if(savedInstanceState != null) {
            pbLoadingProgress.setVisibility(View.GONE);
            bakesListAdapter = savedInstanceState.getParcelable(BAKES_LIST_STATE);
            rvBakesList.setAdapter(bakesListAdapter);
        }else {
            bakesList = new ArrayList<>();
            bakesListAdapter = new BakesListAdapter(bakesList);
            rvBakesList.setAdapter(bakesListAdapter);
            loadBakeList();
        }
    }

    public void screenSize(){
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        GridLayoutManager gridLayoutManager;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                gridLayoutManager = new GridLayoutManager(this, 1);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                gridLayoutManager = new GridLayoutManager(this, 1);
                break;
            default:
                gridLayoutManager = new GridLayoutManager(this, 2);
                break;
        }
        rvBakesList.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BAKES_LIST_STATE, bakesListAdapter);
}

    private void loadBakeList() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dataSourceURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbLoadingProgress.setVisibility(View.GONE);
                try {
                    int totalBakes = response.length();
                    for (int i = 0; i < totalBakes; i++) {
                        JSONObject bakesJSONObject = response.getJSONObject(i);
                        int bakeId = bakesJSONObject.optInt(BAKE_ID);
                        String bakeName = bakesJSONObject.optString(BAKE_NAME);
                        String bakeImage = bakesJSONObject.optString(BAKE_IMAGE);
                        int servings = bakesJSONObject.optInt(BAKE_SERVINGS);
                        int totalSteps = 0;
                        List<Ingredients> ingredientsList = new ArrayList<>();
                        List<Steps> stepsList = new ArrayList<>();
                        if (bakesJSONObject.has(BAKE_STEPS)) {
                            JSONArray stepsArray = new JSONArray(bakesJSONObject.optString(BAKE_STEPS));
                            totalSteps = stepsArray.length();
                            for (int j = 0; j < totalSteps; j++) {
                                JSONObject stepsJson = stepsArray.getJSONObject(j);
                                stepsList.add(new Steps(
                                        stepsJson.optInt(STEP_ID),
                                        stepsJson.optString(STEP_SHORT_DESC),
                                        stepsJson.optString(STEP_DESC),
                                        stepsJson.optString(STEP_VIDEO_URL),
                                        stepsJson.optString(STEP_THUMB_URL)
                                ));
                            }
                        }

                        if (bakesJSONObject.has(BAKE_INGREDIENTS)) {
                            JSONArray ingredientsArray = new JSONArray(bakesJSONObject.optString(BAKE_INGREDIENTS));
                            for (int j = 0; j < ingredientsArray.length(); j++) {
                                JSONObject ingredientsJson = ingredientsArray.getJSONObject(j);
                                ingredientsList.add(new Ingredients(
                                        ingredientsJson.optString(INGREDIENT_QTY),
                                        ingredientsJson.optString(INGREDIENT_MEASURE),
                                        ingredientsJson.optString(INGREDIENT_DESC)
                                ));
                            }
                        }
                        bakesList.add(new Bakes(
                                bakeId,
                                bakeName,
                                bakeImage,
                                servings,
                                totalSteps,
                                stepsList,
                                ingredientsList
                        ));
                    }
                    bakesListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Timber.e(e);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.e(error);
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
