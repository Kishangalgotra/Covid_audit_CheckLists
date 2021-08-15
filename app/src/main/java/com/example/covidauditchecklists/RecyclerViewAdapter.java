package com.example.covidauditchecklists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Model;

public class RecyclerViewAdapter extends RecyclerView.Adapter< RecyclerViewAdapter.MyViewHolder>{

    private List<Model> mModelList;
    private int color;
    public RecyclerViewAdapter(List<Model> modelList) {
        mModelList = modelList;
    }
    private Context context;
    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_check_list_view_2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        color++;
        int drawable;
        final Model model = mModelList.get(position);
        holder.MainText.setText(model.getText());
        if(color%2 ==0){
            drawable  =  R.drawable.item_color_1;
        }else{
            drawable =   R.drawable.item_color_2;
        }
        if(model.isSelected()){
            holder.textView.setBackgroundResource(R.drawable.ic_baseline_check_circle_cyan);
        }else{
            holder.textView.setBackgroundResource(R.drawable.ic_baseline_check_circle_white);
        }
        holder.view.setBackgroundResource(drawable);
        holder.MainText.setOnClickListener(view -> {
            model.setSelected(!model.isSelected());
            if(model.isSelected()){
                user.user_checked_data.add(user.admin_check_list.get(position));
                holder.textView.setBackgroundResource(R.drawable.ic_baseline_check_circle_cyan);
                holder.view.setBackgroundResource( drawable);
            }else{
                user.user_checked_data.remove(user.admin_check_list.get(position));
                holder.view.setBackgroundResource(drawable );
                holder.textView.setBackgroundResource(R.drawable.ic_baseline_check_circle_white);
            }
        });

        holder.textView.setOnClickListener(view -> {
            model.setSelected(!model.isSelected());
            if(model.isSelected()){
                user.user_checked_data.add(user.admin_check_list.get(position));
                holder.textView.setBackgroundResource(R.drawable.ic_baseline_check_circle_cyan);
                holder.view.setBackgroundResource( drawable);
            }else{
                user.user_checked_data.remove(user.admin_check_list.get(position));
                holder.view.setBackgroundResource(drawable );
                holder.textView.setBackgroundResource(R.drawable.ic_baseline_check_circle_white);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private Button textView;
        private TextView MainText;
        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            MainText = (TextView) itemView.findViewById(R.id.user_checklist_text);
            textView = (Button) itemView.findViewById(R.id.item_click_button);
        }
    }
}
