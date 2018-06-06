package com.example.aidan.bakingapp.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aidan.bakingapp.Adapters.IngredientsListAdapter;
import com.example.aidan.bakingapp.Adapters.StepsListAdapter;
import com.example.aidan.bakingapp.Helpers.Helpers;
import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.Models.Ingredients;
import com.example.aidan.bakingapp.Models.IngredientsColumns;
import com.example.aidan.bakingapp.Models.Providers.IngredientsProvider;
import com.example.aidan.bakingapp.Models.Steps;
import com.example.aidan.bakingapp.R;
import com.example.aidan.bakingapp.Widget.WidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsStepsFragment extends Fragment implements StepItemClicked, Parcelable {
    public static final String BAKES_STATE = "bakes_state";
    public static final String CLICKED_STATE = "clicked_state";
    public static final Creator<IngredientsStepsFragment> CREATOR = new Creator<IngredientsStepsFragment>() {
        @Override
        public IngredientsStepsFragment createFromParcel(Parcel in) {
            return new IngredientsStepsFragment(in);
        }

        @Override
        public IngredientsStepsFragment[] newArray(int size) {
            return new IngredientsStepsFragment[size];
        }
    };
    @BindView(R.id.rv_ingredients_list)
    RecyclerView rvIngredientsList;
    @BindView(R.id.rv_steps_list)
    RecyclerView rvStepsList;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;
    @BindView(R.id.fab_add_to_widget)
    FloatingActionButton fabAddToWidget;
    IngredientsListAdapter ingredientsListAdapter;
    StepsListAdapter stepsListAdapter;
    LinearLayoutManager lmIngredientsLayout;
    Bakes bakes = null;
    List<Ingredients> ingredientsList = new ArrayList<>();
    List<Steps> stepsList = new ArrayList<>();
    OnStepClickListener onStepClickListener;

    public IngredientsStepsFragment() {
    }

    @SuppressLint("ValidFragment")
    protected IngredientsStepsFragment(Parcel in) {
        bakes = in.readParcelable(Bakes.class.getClassLoader());
        ingredientsList = in.createTypedArrayList(Ingredients.CREATOR);
        stepsList = in.createTypedArrayList(Steps.CREATOR);
    }

    @Override
    public void getClickedStepPosition(int position) {
        onStepClickListener.onStepClicked(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bakes, flags);
        dest.writeTypedList(ingredientsList);
        dest.writeTypedList(stepsList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_steps, container, false);
        ButterKnife.bind(this, view);

        if(getContext() != null)

        if (savedInstanceState != null) {
            bakes = savedInstanceState.getParcelable(BAKES_STATE);
        }

        generateIngredientsList(getIngredientsList());
        generateStepsList(getStepsList());

        if (isAddedToWidget() > 0) {
            updateFloatingActionButton(true);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BAKES_STATE, bakes);
    }

    public void setBakes(Bakes bakes) {
        this.bakes = bakes;
    }

    private List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    private List<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void updateFloatingActionButton(boolean hasBeenAdded) {
        if (hasBeenAdded) {
            fabAddToWidget.setImageResource(R.drawable.ic_delete_white_24dp);
            fabAddToWidget.setTag(1);
        } else {
            fabAddToWidget.setImageResource(R.drawable.ic_add_white_24dp);
            fabAddToWidget.setTag(0);
        }
    }

    public int isAddedToWidget() {
        int count = 0;
        if (getActivity() != null) {
            Cursor cursor = getActivity().getContentResolver().query(IngredientsProvider.Ingredients.CONTENT_URI,
                    null,
                    IngredientsColumns.COLUMN_BAKE_NAME + " LIKE ?",
                    new String[]{
                            bakes.getBakeName() + "%"
                    },
                    null);
            if (cursor != null) {
                count = cursor.getCount();
                cursor.close();
            }
        }
        return count;
    }

    @OnClick(R.id.iv_next)
    void nextInRecyclerView() {
        int itemPosition = lmIngredientsLayout.findLastVisibleItemPosition() + 1;
        ivPrevious.setVisibility(View.VISIBLE);
        if (itemPosition >= (ingredientsListAdapter.getItemCount() - 1)) {
            ivNext.setVisibility(View.INVISIBLE);
        } else {
            ivNext.setVisibility(View.VISIBLE);
        }
        rvIngredientsList.getLayoutManager().scrollToPosition(lmIngredientsLayout.findLastVisibleItemPosition() + 1);
    }

    @OnClick(R.id.iv_previous)
    void previousInRecyclerView() {
        int itemPosition = lmIngredientsLayout.findLastVisibleItemPosition() - 1;
        ivNext.setVisibility(View.VISIBLE);
        if (itemPosition <= 0) {
            ivPrevious.setVisibility(View.INVISIBLE);
        } else {
            ivPrevious.setVisibility(View.VISIBLE);
        }
        rvIngredientsList.getLayoutManager().scrollToPosition(lmIngredientsLayout.findLastVisibleItemPosition() - 1);
    }

    void generateIngredientsList(List<Ingredients> ingredientsList) {
        lmIngredientsLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        ingredientsListAdapter = new IngredientsListAdapter(ingredientsList);
        rvIngredientsList.setLayoutManager(lmIngredientsLayout);
        rvIngredientsList.setAdapter(ingredientsListAdapter);
    }

    void generateStepsList(List<Steps> stepsList) {
        LinearLayoutManager lmStepsLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        stepsListAdapter = new StepsListAdapter(stepsList, bakes, this);
        rvStepsList.setLayoutManager(lmStepsLayout);
        rvStepsList.setAdapter(stepsListAdapter);
        rvStepsList.setHasFixedSize(true);
    }

    @OnClick(R.id.fab_add_to_widget)
    public void addToWidget() {
        int tag = Integer.parseInt(fabAddToWidget.getTag().toString());
        if (tag == 0) {
            performAdd();
        } else {
            performRemove(bakes.getBakeName());
        }
    }

    public void performAdd() {
        if (getActivity() == null) return;
        final Uri[] returnedURI = new Uri[1];
        ContentValues values = new ContentValues();
        values.put(IngredientsColumns.COLUMN_BAKE_NAME, bakes.getBakeName());
        values.put(IngredientsColumns.COLUMN_INGREDIENTS, getIngredients());
        values.put(IngredientsColumns.COLUMN_TIMESTAMP, Helpers.getCurrentTimeAsInteger());
        returnedURI[0] = getActivity().getContentResolver().insert(IngredientsProvider.Ingredients.CONTENT_URI, values);
        if (returnedURI[0] == null) {
            Toast.makeText(getContext(), R.string.error_adding_to_widget, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.ingredients_added_to_widget, Toast.LENGTH_LONG).show();
            WidgetService.actionUpdateWidget(getContext());
            updateFloatingActionButton(true);
        }
    }

    public void performRemove(String bakeName) {
        if (getActivity() == null) return;
        int data = getActivity().getContentResolver().delete(IngredientsProvider.Ingredients.CONTENT_URI,
                IngredientsColumns.COLUMN_BAKE_NAME + " LIKE ?",
                new String[]{
                        bakeName + "%"
                });
        if (data > 0) {
            Toast.makeText(getContext(), R.string.ingredients_removed, Toast.LENGTH_LONG).show();
            updateFloatingActionButton(false);
            WidgetService.actionUpdateWidget(getContext());
        } else {
            Toast.makeText(getContext(), R.string.error_removing_ingredients, Toast.LENGTH_LONG).show();
        }
    }

    String getIngredients() {
        StringBuilder ingredientsBuilder = new StringBuilder();
        List<Ingredients> ingredientsList = bakes.getIngredientsList();
        int totalIngredients = ingredientsList.size();
        for (int i = 0; i < totalIngredients; i++) {
            String quantity = ingredientsList.get(i).getQuantity();
            ingredientsBuilder
                    .append("\u25A0 ")
                    .append(quantity)
                    .append(" ")
                    .append(ingredientsList.get(i).getMeasure())
                    .append(" ")
                    .append(getString(R.string.string_of))
                    .append(" ")
                    .append(ingredientsList.get(i).getIngredientName())
                    .append("\n");
        }
        return ingredientsBuilder.toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ingredients_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_ingredients:
                StringBuilder stringBuilder = new StringBuilder();
                String ingredients = stringBuilder.append("Ingredients required to make ")
                        .append(bakes.getBakeName()).append("::\n").append(getIngredients()).toString();
                shareIngredients(ingredients);
                return true;

            default:
                break;
        }

        return false;
    }

    private void shareIngredients(String ingredients) {
        if (getActivity() != null)
            ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setChooserTitle(R.string.share_ingredients)
                    .setText(ingredients)
                    .startChooser();
    }

    public interface OnStepClickListener {
        void onStepClicked(int position);
    }
}
