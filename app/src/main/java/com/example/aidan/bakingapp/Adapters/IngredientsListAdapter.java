package com.example.aidan.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aidan.bakingapp.Models.Ingredients;
import com.example.aidan.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aidan.bakingapp.Helpers.Helpers.getFullMeasurementString;
import static com.example.aidan.bakingapp.Helpers.Helpers.getMeasurementIcon;

@SuppressWarnings("StringBufferReplaceableByString")
public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsListViewHolder> {
    private List<Ingredients> ingredientsList;
    private Context context;

    public IngredientsListAdapter(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public IngredientsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_layout, parent, false);
        context = parent.getContext();
        return new IngredientsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsListViewHolder holder, int position) {
        Ingredients ingredients = ingredientsList.get(position);

        holder.tvIngredient.setText(ingredients.getIngredientName());

        int drawable = getMeasurementIcon(ingredients.getMeasure());
        int quantity = 0;
        if(TextUtils.isDigitsOnly(ingredients.getQuantity())){
            quantity = Integer.parseInt(ingredients.getQuantity());
        }
        String measure = getFullMeasurementString(ingredients.getMeasure(),context, quantity);
        String measureString = new StringBuilder().append(ingredients.getQuantity()).append("\n").append(measure).toString();
        holder.tvMeasureQuantity.setText(measureString);
        holder.tvMeasureQuantity.setCompoundDrawablesWithIntrinsicBounds(0,drawable,0,0);
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class IngredientsListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;
        @BindView(R.id.tv_measure_quantity)
        TextView tvMeasureQuantity;

        IngredientsListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
