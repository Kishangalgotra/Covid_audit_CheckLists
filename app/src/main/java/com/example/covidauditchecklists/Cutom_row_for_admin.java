package com.example.covidauditchecklists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class Cutom_row_for_admin  extends RecyclerView.Adapter<com.example.covidauditchecklists.Cutom_row_for_admin.ViewHolder>{
    ArrayList<String> array ;
    Context context;
    Cutom_row_for_admin.onCustomClickListener onCustomClickListener;
    public Cutom_row_for_admin(Context context,ArrayList<String> array, Cutom_row_for_admin.onCustomClickListener onCustomClickListener) {
        this.array = array;
        this.onCustomClickListener = onCustomClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public Cutom_row_for_admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_check_list_view,parent,false);
        return new Cutom_row_for_admin.ViewHolder(view,onCustomClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Cutom_row_for_admin.ViewHolder holder, int position) {
        String setdata = array.get(position);
        holder.text.setText(setdata);

        /*if(holder.checkBox.isChecked()){
           holder.checkBox.setChecked(false);
            holder.constraint_layout_id.setBackgroundResource(R.drawable.custom_input);
            }
        else{
            holder.constraint_layout_id.setBackgroundResource(R.drawable.custom_input_red);
            holder.checkBox.setChecked(true);}*/
    }

    void bind (final CheckBox checkBox){
        checkBox.setSelected(checkBox.isChecked() ? false  : true);

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener   {
        CheckBox checkBox;
        TextView text;
        //ConstraintLayout constraint_layout_id;
        Cutom_row_for_admin.onCustomClickListener oncustomClickListener;
        public ViewHolder(@NonNull View itemView, Cutom_row_for_admin.onCustomClickListener oncustomClickListener) {
            super(itemView);

            text     = itemView.findViewById(R.id.user_checklist_text);
            //constraint_layout_id = itemView.findViewById(R.id.constraint_layout_id);
            //constraint_layout_id.setOnClickListener(this);
            this.oncustomClickListener = oncustomClickListener;
           // checkBox.setOnClickListener(this);
            //text.setOnClickListener(this);

        }
       /* @Override
    /    public void onClick(View v) {


            if(v.getId() == R.id.user_checklist_text) {

                oncustomClickListener.onCustomViewClickListener(getAdapterPosition(), checkBox, constraint_layout_id);
            }

            if(v.getId() ==R.id.user_checklist_checkBox) {

                oncustomClickListener.onCustomCheckBoxClickListener(getAdapterPosition(), checkBox, constraint_layout_id);
            }
        }*/

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(checkBox.isChecked()){
               // constraint_layout_id.setBackgroundResource(R.drawable.custom_input);
                //Admin_Checklist.admin_checked_data_for_delete.remove(Integer.toString(position));
                // admin_checked_data.remove(fetched_checked_data.get(position));
                checkBox.setChecked(false);
            }else {
                // admin_checked_data.add(fetched_checked_data.get(position));
               // constraint_layout_id.setBackgroundResource(R.drawable.custom_input_red);
                ///admin_checked_data_for_delete.add(Integer.toString(position));
               // Toast.makeText(this, Arrays.toString(new ArrayList[]{admin_checked_data_for_delete}),Toast.LENGTH_SHORT).show();
                checkBox.setChecked(true);
            }
            v.setOnClickListener(v1 -> {
                oncustomClickListener.onCustomViewClickListener(getAdapterPosition(), checkBox);
            });

            return true;
        }
    }
    public interface onCustomClickListener{
        void onCustomCheckBoxClickListener(int position,CheckBox checkBox);
        void onCustomViewClickListener(int position,CheckBox checkBox);
    }
}
