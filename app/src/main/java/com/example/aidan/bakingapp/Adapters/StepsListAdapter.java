package com.example.aidan.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aidan.bakingapp.Fragments.StepItemClicked;
import com.example.aidan.bakingapp.Helpers.GlideApp;
import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.Models.Steps;
import com.example.aidan.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aidan.bakingapp.Helpers.Helpers.getStepIcon;

@SuppressWarnings("StringBufferReplaceableByString")
public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListViewHolder> {
    public static final String STEPS_EXTRA = "bakes_steps_extra";
    public static final String SELECTED_POSITION_EXTRA = "selected_bake_step_extra";
    private List<Steps> stepsList;
    private Bakes bakes;
    private StepItemClicked stepItemClicked;
    private Context context;

    public StepsListAdapter(List<Steps> stepsList, Bakes bakes, StepItemClicked stepItemClicked) {
        this.stepsList = stepsList;
        this.bakes = bakes;
        this.stepItemClicked = stepItemClicked;
    }

    @NonNull
    @Override
    public StepsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_layout, parent, false);
        this.context = parent.getContext();
        return new StepsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListViewHolder holder, int position) {
        Steps steps = stepsList.get(position);

        if(!steps.getThumbnailUrl().trim().equals("")) {
           GlideApp.with(context)
                    .asBitmap()
                    .load(steps.getThumbnailUrl())
                    .override(1024,852)
                    .placeholder(getStepIcon(steps.getVideoUrl()))
                    .error(getStepIcon(steps.getVideoUrl()))
                    .into(holder.ivThumbnail);
        }else {
            holder.ivThumbnail.setImageDrawable(context.getResources().getDrawable(getStepIcon(steps.getVideoUrl())));
        }

        holder.tvRightDescTitle.setText(steps.getShortDescription());
        holder.tvStepDesc.setText(steps.getFullDescription());

        holder.cvSteps.setTag(position);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class StepsListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnail_icon)
        ImageView ivThumbnail;
        @BindView(R.id.tv_right_desc_title)
        TextView tvRightDescTitle;
        @BindView(R.id.tv_step_desc)
        TextView tvStepDesc;
        @BindView(R.id.cv_steps)
        CardView cvSteps;

        StepsListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cv_steps)
        void openVideo() {
            if (bakes != null) {
                int position = Integer.parseInt(cvSteps.getTag().toString());
                stepItemClicked.getClickedStepPosition(position);
            }
        }
    }
}
