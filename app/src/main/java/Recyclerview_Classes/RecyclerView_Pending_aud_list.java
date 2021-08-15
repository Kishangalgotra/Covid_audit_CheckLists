package Recyclerview_Classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.covidauditchecklists.R;
import java.util.List;

import COMMON.COMMON_DATA;
import Controller_Classes.ADMIN_AUD_PEND_LIST;
import Controller_Classes.auditor_client_list;
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Model.Model_admin_auditor;

public class RecyclerView_Pending_aud_list extends RecyclerView.Adapter<RecyclerView_Pending_aud_list.MYVIEWHOLDER> {
    public static List<MODEL_ADMIN_PEND_AUDIT_REQ> model_admin_pend_audit_reqs;

    public RecyclerView_Pending_aud_list (List<MODEL_ADMIN_PEND_AUDIT_REQ> model_admin_pend_audit_reqs2){
        this.model_admin_pend_audit_reqs = model_admin_pend_audit_reqs2;
    }
    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pend_aud_list,parent,false);
        return new RecyclerView_Pending_aud_list.MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {
        final MODEL_ADMIN_PEND_AUDIT_REQ smodel = model_admin_pend_audit_reqs.get(position);
        String sName = smodel.getAuditor_NAME().toUpperCase();
        holder.name.setText(sName);
        holder.client_name.setText(smodel.getClient_name().toUpperCase());
        holder.view.setBackgroundResource(smodel.is_pending_auditor_isSelected() ?
                R.drawable.custom_click_view_admin_auditor_clicked : R.drawable.custom_input);
        holder.linearLayout.setOnClickListener(view -> {
            ADMIN_AUD_PEND_LIST.BTN_APPROVE.setEnabled(true);
            ADMIN_AUD_PEND_LIST.BTN_REJECT.setEnabled(true);
            int i =0;
            for(MODEL_ADMIN_PEND_AUDIT_REQ object  : model_admin_pend_audit_reqs){
                if(i != position){
                    object.setSelected(false);
                    holder.view.setBackgroundResource(R.drawable.custom_input );
                }
                i++;
                notifyDataSetChanged();
            }
            //----------------SELECTED DATA FOR APPROVAL OR REJECTION
            ADMIN_AUD_PEND_LIST.Approved_Rejected_data   =   model_admin_pend_audit_reqs.get(position);
            ADMIN_AUD_PEND_LIST.Clicked_DATA_position = position;
            smodel.setSelected(!smodel.isSelected());
            if(smodel.isSelected()){
                holder.view.setBackgroundResource( R.drawable.custom_click_view_admin_auditor_clicked);
            }else{
                holder.view.setBackgroundResource(R.drawable.custom_input );
            }
        });

        holder.audited_data.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(holder.audited_data.getContext(), R.anim.anim);
            holder.audited_data.startAnimation(myAnim);
            if(smodel.getClient_selected_items().isEmpty()){
                String[] selectedataArray = {"Empty "};
                COMMON_DATA.Admin_aud_pend_list_selected_data(selectedataArray,holder.name.getContext());
            }else{
                String[] selectedataArray = smodel.getClient_selected_items().split(",");
                COMMON_DATA.Admin_aud_pend_list_selected_data(selectedataArray,holder.name.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_admin_pend_audit_reqs == null ? 0 : model_admin_pend_audit_reqs.size();
    }

    public class MYVIEWHOLDER extends RecyclerView.ViewHolder {
        private View view;
        private TextView name;
        private TextView client_name;
        private Button audited_data;
        private LinearLayout linearLayout;
        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.admin_pend_aud_name);
            client_name = itemView.findViewById(R.id.admin_pend_client_name);
            audited_data = itemView.findViewById(R.id.admin_pen_aud_selected_data);
            linearLayout = itemView.findViewById(R.id.admin_pend_aud_list_linear);
        }
    }
}
