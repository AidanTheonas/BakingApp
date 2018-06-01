package com.example.aidan.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aidan.bakingapp.BakesDetailsActivity;
import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aidan.bakingapp.MainActivity.BAKES_EXTRA;

@SuppressWarnings("StringBufferReplaceableByString")
public class BakesListAdapter extends RecyclerView.Adapter<BakesListAdapter.BakesListViewHolder> implements Parcelable {
    private List<Bakes> bakesList;
    private Context context;

    public BakesListAdapter(List<Bakes> bakesList) {
        this.bakesList = bakesList;
    }

    private BakesListAdapter(Parcel in) {
        bakesList = in.createTypedArrayList(Bakes.CREATOR);
    }

    public static final Creator<BakesListAdapter> CREATOR = new Creator<BakesListAdapter>() {
        @Override
        public BakesListAdapter createFromParcel(Parcel in) {
            return new BakesListAdapter(in);
        }

        @Override
        public BakesListAdapter[] newArray(int size) {
            return new BakesListAdapter[size];
        }
    };

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

        //Set Content Description for the bake
        holder.ivBakeImage.setContentDescription(bakes.getBakeName());

        //Set Bake Name
        holder.tvBakeName.setText(bakes.getBakeName());

        //Set Total Steps for the bake
        String totalSteps = new StringBuilder().append(context.getResources().getString(R.string.total_steps)).append("\t").append(bakes.getTotalSteps()).toString();
        holder.tvTotalSteps.setText(totalSteps);

        //Set  servings value
        String servings = new StringBuilder().append(context.getResources().getString(R.string.servings)).append("\t").append(bakes.getServings()).toString();
        holder.tvServings.setText(servings);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bakesList);
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
        @BindView(R.id.iv_pie_imageview)
        ImageView ivBakeImage;

        BakesListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cl_container)
        public void openMoreDetailsActivity(){
            Bakes bakes = bakesList.get(getAdapterPosition());
            Intent intent = new Intent(context, BakesDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(BAKES_EXTRA, bakes);
            context.startActivity(intent);
        }
    }
}

