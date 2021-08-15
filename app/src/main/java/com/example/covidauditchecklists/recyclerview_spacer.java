package com.example.covidauditchecklists;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recyclerview_spacer extends RecyclerView.ItemDecoration {

    private final int VectorSpaceHeight;

    public recyclerview_spacer(int vectorSpaceHeight) {
        VectorSpaceHeight = vectorSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
      outRect.bottom = VectorSpaceHeight;
    }
}
