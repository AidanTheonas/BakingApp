package com.example.aidan.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aidan.bakingapp.Models.Steps;
import com.example.aidan.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aidan.bakingapp.Helpers.Helpers.getStepIcon;

@SuppressWarnings("StringBufferReplaceableByString")
public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListViewHolder> {
    private List<Steps> stepsList;
    private Context context;

    public StepsListAdapter(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    @NonNull
    @Override
    public StepsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_layout, parent, false);
        context = parent.getContext();
        return new StepsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListViewHolder holder, int position) {
        Steps steps = stepsList.get(position);

        holder.tvStepIcon.setCompoundDrawablesWithIntrinsicBounds(0, getStepIcon(steps.getVideoUrl()), 0, 0);

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
        @BindView(R.id.tv_step_icon)
        TextView tvStepIcon;
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
        void openVideo(){
            int position = Integer.parseInt(cvSteps.getTag().toString());
            Steps clickedStep = stepsList.get(position);
            if(clickedStep.getVideoUrl().trim().equals("")){
                Toast.makeText(context, R.string.video_not_available,Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, clickedStep.getVideoUrl(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
