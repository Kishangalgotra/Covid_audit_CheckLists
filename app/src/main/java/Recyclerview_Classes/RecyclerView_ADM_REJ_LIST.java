package Recyclerview_Classes;

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
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Model.MODEL_ADMI_REJECTED_LIST;

public class RecyclerView_ADM_REJ_LIST extends RecyclerView.Adapter<RecyclerView_ADM_REJ_LIST.MYVIEWHOLDER> {
    public static List<MODEL_ADMI_REJECTED_LIST> model_admi_rejected_lists;

    public RecyclerView_ADM_REJ_LIST (List<MODEL_ADMI_REJECTED_LIST> model_admi_rejected_lists){
        this.model_admi_rejected_lists = model_admi_rejected_lists;
    }
    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pend_aud_list,parent,false);
        return new RecyclerView_ADM_REJ_LIST.MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {
        final MODEL_ADMI_REJECTED_LIST smodel = model_admi_rejected_lists.get(position);
        String sName = smodel.getAuditor_NAME().toUpperCase();
        holder.name.setText(sName);
        holder.client_name.setText(smodel.getClient_name().toUpperCase());

        holder.audited_data.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(holder.audited_data.getContext(), R.anim.anim);
            holder.audited_data.startAnimation(myAnim);

            if( smodel.getClient_selected_items().isEmpty()){
                String[] selectedataArray = {"Empty "};
                COMMON_DATA.Admin_aud_pend_list_selected_data(selectedataArray,holder.name.getContext());
            }else{
                String[] selectedataArray = smodel.getClient_selected_items().split(",");
                COMMON_DATA.Admin_aud_pend_list_selected_data(selectedataArray,holder.name.getContext());
            }
        });
        holder.view.setBackgroundResource(smodel.isSelected() ?
                R.drawable.custom_click_view_admin_auditor_clicked : R.drawable.custom_input);
    }

    @Override
    public int getItemCount() {
        return model_admi_rejected_lists == null ? 0 : model_admi_rejected_lists.size();
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
