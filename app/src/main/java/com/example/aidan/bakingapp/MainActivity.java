package com.example.aidan.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.aidan.bakingapp.Adapters.BakesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String BAKE_ID = "id";
    public static final String BAKE_NAME = "name";
    public static final String BAKE_INGREDIENTS = "ingredients";
    public static final String BAKE_STEPS = "ingredients";

    @BindView(R.id.rv_bakes_list)
    RecyclerView rvBakesList;
    BakesListAdapter bakesListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }
}
