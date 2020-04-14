package com.askmesmth.designtraining;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CategoryImageAdapter extends RecyclerView.Adapter<CategoryImageAdapter.ViewHolder> {
    private final List<String> categoryNames;
    private final List<Integer> imageIds;
    private Listener listener;


    void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public CategoryImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_category_image, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.category_image);
        Drawable drawable = cardView.getResources().getDrawable(imageIds.get(position));
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(categoryNames.get(position));
        TextView textView = cardView.findViewById(R.id.category_name);
        textView.setText(categoryNames.get(position));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    public interface Listener {
        void onClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}

