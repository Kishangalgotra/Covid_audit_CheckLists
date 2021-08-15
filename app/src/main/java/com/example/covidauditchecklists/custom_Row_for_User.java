package com.example.covidauditchecklists;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class custom_Row_for_User extends RecyclerView.Adapter<custom_Row_for_User.ViewHolder> {

    String[] array ;
    Context context;
    onCustomClickListener oncustomClickListener;
    public custom_Row_for_User(Context context,String[] array, onCustomClickListener oncustomClickListener) {
        this.array = array;
        this.oncustomClickListener = oncustomClickListener;
        this.context =context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_check_list_view,parent,false);
        return new custom_Row_for_User.ViewHolder(view,oncustomClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String setdata = array[position];
        holder.text.setText(setdata);
    }

    @Override
    public int getItemCount() {
        return array.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
       // CheckBox checkBox;
        TextView text;

        onCustomClickListener oncustomClickListener;
        public ViewHolder(@NonNull View itemView,onCustomClickListener oncustomClickListener) {
            super(itemView);
          //  checkBox = itemView.findViewById(R.id.user_checklist_checkBox);
            text     = itemView.findViewById(R.id.user_checklist_text);

            this.oncustomClickListener = oncustomClickListener;
            ///checkBox.setOnClickListener(this);
            text.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
        }

    }
    public interface onCustomClickListener{
        void onCustomCheckBoxClickListener(int position,CheckBox checkBox);
        void onCustomViewClickListener(int position,CheckBox checkBox);
    }
}
