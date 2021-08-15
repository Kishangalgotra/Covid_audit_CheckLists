package Recyclerview_Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.covidauditchecklists.R;
import java.util.List;

import Controller_Classes.admin_auditor_list;
import Model.Model_admin_auditor;

public class RecyclerViewAdapter_admin_auditor extends RecyclerView.Adapter<RecyclerViewAdapter_admin_auditor.MyViewholder> {
    private List<Model_admin_auditor> model_admin_auditors;
    public RecyclerViewAdapter_admin_auditor(List<Model_admin_auditor> model_admin_auditors){
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
        holder.user_linear_layout.setOnClickListener(view -> {
            int i =0;
            for(Model_admin_auditor object  : model_admin_auditors){
                if(i != position){
                    object.setAdmin_auditor_isSelected(false);
                    holder.view.setBackgroundResource(R.drawable.custom_input );
                }
                i++;
                notifyDataSetChanged();
            }
            smodel.setAdmin_auditor_isSelected(!smodel.isAdmin_auditor_isSelected());
            if(smodel.isAdmin_auditor_isSelected()){
                admin_auditor_list.AUDITOR_ID = smodel.getUser_ID();
                //admin_auditor_list.FILL_AUDITOR_CLIENT_LIST();
                holder.view.setBackgroundResource( R.drawable.custom_click_view_admin_auditor_clicked);
            }else{
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
        private LinearLayout user_linear_layout;
        private MyViewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.common_audit_admin);
            user_linear_layout =  itemView.findViewById(R.id.user_linear_layout);
        }
    }
}
