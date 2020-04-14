package com.askmesmth.designtraining;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CategoriesFragment extends Fragment {
    private final static int CATEGORIES_COLUMNS_AMOUNT = 2;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView categoriesRecyclerView = (RecyclerView) inflater
                .inflate(R.layout.fragment_categories, container, false);

        List<String> categoryNames = new ArrayList<>();
        List<Integer> categoryImagesIds = new ArrayList<>();
        for (QuestionCategory category : QuestionCategory.categories) {
            categoryNames.add(category.getCategoryName());
            categoryImagesIds.add(category.getImageResourceId());
        }
        CategoryImageAdapter categoryImageAdapter =
                new CategoryImageAdapter(categoryNames, categoryImagesIds);

        categoriesRecyclerView.setAdapter(categoryImageAdapter);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), CATEGORIES_COLUMNS_AMOUNT);
        categoriesRecyclerView.setLayoutManager(layoutManager);
        categoryImageAdapter.setListener(new CategoryImageAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                intent.putExtra(QuestionsActivity.QUESTION_NUMB, position);
                Objects.requireNonNull(getActivity()).startActivity(intent);
            }
        });
        return categoriesRecyclerView;
    }

}
