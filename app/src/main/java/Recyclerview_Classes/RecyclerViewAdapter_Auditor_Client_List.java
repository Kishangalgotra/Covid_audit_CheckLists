package Recyclerview_Classes;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.covidauditchecklists.R;
import java.util.List;

import Controller_Classes.auditor_client_list;
import Model.Model_admin_auditor;

public class RecyclerViewAdapter_Auditor_Client_List extends
        RecyclerView.Adapter<RecyclerViewAdapter_Auditor_Client_List.MyViewholder> {
    public static List<Model_admin_auditor> model_admin_auditors;

    public RecyclerViewAdapter_Auditor_Client_List(List<Model_admin_auditor> model_admin_auditors){
        this.model_admin_auditors = model_admin_auditors;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_admin_auditor_list_view,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final Model_admin_auditor smodel = model_admin_auditors.get(position);
        holder.name.setText(smodel.getUser_Name());
        holder.view.setBackgroundResource(smodel.isAdmin_auditor_isSelected() ?
                R.drawable.custom_click_view_admin_auditor_clicked : R.drawable.custom_input);
        holder.view.setOnClickListener(view -> {
            int i =0;
            for(Model_admin_auditor object  : model_admin_auditors){
                if(i != position){
                    object.setAdmin_auditor_isSelected(false);
                    holder.view.setBackgroundResource(R.drawable.custom_input );
                }
                i++;
                notifyDataSetChanged();
            }
            auditor_client_list.Clicked_user_ID = smodel.getUser_ID();
            auditor_client_list.Clicked_user_Name = smodel.getUser_Name();

            smodel.setAdmin_auditor_isSelected(!smodel.isAdmin_auditor_isSelected());
            if(smodel.isAdmin_auditor_isSelected()){
                auditor_client_list.Clicked_user_Number = position;
                holder.view.setBackgroundResource( R.drawable.custom_click_view_admin_auditor_clicked);
            }else{
                auditor_client_list.Clicked_user_Number =-1;
                holder.view.setBackgroundResource(R.drawable.custom_input );
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_admin_auditors == null ? 0 : model_admin_auditors.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        private View view;
        private TextView name;
        private MyViewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.common_audit_admin);
        }
    }
}
