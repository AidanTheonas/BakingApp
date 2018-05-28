package com.example.aidan.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("StringBufferReplaceableByString")
public class BakesListAdapter extends RecyclerView.Adapter<BakesListAdapter.BakesListViewHolder> {
    private List<Bakes> bakesList;
    private Context context;

    public BakesListAdapter(List<Bakes> bakesList) {
        this.bakesList = bakesList;
    }

    @NonNull
    @Override
    public BakesListAdapter.BakesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bakes_list_layout, parent, false);
        context = parent.getContext();
        return new BakesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BakesListViewHolder holder, int position) {
        Bakes bakes = bakesList.get(position);

        holder.tvBakeName.setText(bakes.getBakeName());

        String totalIngredients = new StringBuilder().append(context.getResources().getString(R.string.servings)).append("\t").append(bakes.getServings()).toString();
        holder.tvServings.setText(totalIngredients);

        String totalSteps = new StringBuilder().append(context.getResources().getString(R.string.total_steps)).append("\t").append(bakes.getTotalSteps()).toString();
        holder.tvTotalSteps.setText(totalSteps);
    }

    @Override
    public int getItemCount() {
        return bakesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class BakesListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cl_container)
        ConstraintLayout clContainer;
        @BindView(R.id.tv_bake_name)
        TextView tvBakeName;
        @BindView(R.id.tv_total_steps)
        TextView tvTotalSteps;
        @BindView(R.id.tv_servings)
        TextView tvServings;

        BakesListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cl_container)
        public void openMoreDetailsActivity(){
            Bakes bakes = bakesList.get(getAdapterPosition());
            Toast.makeText(context,bakes.getIngredientsList().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}

