package com.example.covidauditchecklists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Model;

public class RecyclerViewAdapter_Admin extends RecyclerView.Adapter<RecyclerViewAdapter_Admin.MyViewHolder>{
    private List<Model> mModelList;
    public RecyclerViewAdapter_Admin(List<Model> modelList) {
        mModelList = modelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_check_list_view,
        parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Model model = mModelList.get(position);
        holder.MainText.setText(model.getText());
        holder.view.setBackgroundResource(model.isSelected() ? R.drawable.custom_input_red : R.drawable.custom_input );
        holder.MainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                if(model.isSelected()){
                    Admin_Checklist.admin_checked_data_for_delete.add(Admin_Checklist.array.get(position));
                    //Admin_Checklist.array
                    holder.view.setBackgroundResource( R.drawable.custom_input_red);
                    Admin_Checklist.positions_of_deleted_objects.add(Integer.toString(position));
                }else{
                    Admin_Checklist.admin_checked_data_for_delete.remove(Admin_Checklist.array.get(position));
                    holder.view.setBackgroundResource(R.drawable.custom_input );
                    Admin_Checklist.positions_of_deleted_objects.remove(Integer.toString(position));
                }
            }
        });

        holder.textView.setOnClickListener(view -> {
            model.setSelected(!model.isSelected());
            if(model.isSelected()){
                Admin_Checklist.admin_checked_data_for_delete.add(Admin_Checklist.array.get(position));
                holder.view.setBackgroundResource( R.drawable.custom_input_red);
                Admin_Checklist.positions_of_deleted_objects.add(Integer.toString(position));
            }else{
                Admin_Checklist.admin_checked_data_for_delete.remove(Admin_Checklist.array.get(position));
                holder.view.setBackgroundResource(R.drawable.custom_input );
                Admin_Checklist.positions_of_deleted_objects.remove(Integer.toString(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textView;
        private TextView MainText;
        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            MainText = (TextView) itemView.findViewById(R.id.user_checklist_text);
            textView = (TextView) itemView.findViewById(R.id.user_checklist_Click);
        }
    }
}
