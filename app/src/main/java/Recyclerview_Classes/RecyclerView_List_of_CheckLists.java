package Recyclerview_Classes;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidauditchecklists.R;
import java.util.List;

import COMMON.COMMON_DATA;
import COMMON.Flags;
import Controller_Classes.Activity_List_Of_CheckList;
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Model.MODEL_List_Of_CheckList;

public class RecyclerView_List_of_CheckLists extends RecyclerView.Adapter<RecyclerView_List_of_CheckLists.MYVIEWHOLDER> {
    public static List<MODEL_List_Of_CheckList> model_list_of_checkLists;
    public RecyclerView_List_of_CheckLists (List<MODEL_List_Of_CheckList> model_list_of_checkListss){
        model_list_of_checkLists = model_list_of_checkListss;
    }
    @NonNull
    @Override
    public MYVIEWHOLDER  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_checklist_view,parent,false);
        return new RecyclerView_List_of_CheckLists.MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {
        final MODEL_List_Of_CheckList smodel = model_list_of_checkLists.get(position);
        holder.name.setText(smodel.getCheckListname());

        if(smodel.getMainList().equalsIgnoreCase("true")){
            holder.setAsMainCheckList.setBackgroundResource(R.drawable.custom_input);
            holder.setAsMainCheckList.setText("Selected as main");
            Activity_List_Of_CheckList.selectedAsMain = position;
        }else{
           // holder.setAsMainCheckList.setBackgroundResource(R.drawable.list_of_checklist_unselected_star);
        }
        holder.view.setBackgroundResource(smodel.isSelected() ?
                R.drawable.custom_click_view_admin_auditor_clicked : R.drawable.custom_input);
        holder.constrantLayout.setOnClickListener(v -> {
            int i =0;
            for(MODEL_List_Of_CheckList object  : model_list_of_checkLists){
                if(i != position){
                    object.setSelected(false);
                    holder.view.setBackgroundResource(R.drawable.custom_input );
                }
                i++;
                notifyDataSetChanged();
            }
            final Animation myAnim = AnimationUtils.loadAnimation(holder.constrantLayout.getContext(), R.anim.anim);
            holder.constrantLayout.startAnimation(myAnim);
            Activity_List_Of_CheckList.selectedMain  = position;
            Flags.model_list_of_checkListFlag.setCheckList(smodel.getCheckList());
            smodel.setIsSelected(!smodel.getIsSelected());
            if(smodel.getIsSelected()){
                holder.constrantLayout.setBackgroundResource( R.drawable.custom_click_view_admin_auditor_clicked);
                Activity_List_Of_CheckList.modificationIndex = position;
                Flags.auditorCheckListSelectedFlag = 1;
            }else{
                holder.constrantLayout.setBackgroundResource(R.drawable.custom_input );
                Activity_List_Of_CheckList.modificationIndex = -1;
                Flags.auditorCheckListSelectedFlag = 0;
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_list_of_checkLists == null ? 0 :model_list_of_checkLists.size();
    }

    public class MYVIEWHOLDER extends RecyclerView.ViewHolder {
        private View view;
        private TextView name,list_of_check_checklist_Edit;
        private TextView setAsMainCheckList;
        private ConstraintLayout constrantLayout;
        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.list_of_check_checklist_text);
            //list_of_check_checklist_Edit = itemView.findViewById(R.id.list_of_check_checklist_Edit);
            constrantLayout = itemView.findViewById(R.id.list_of_check_constraint_layout);
            setAsMainCheckList = itemView.findViewById(R.id.list_of_check_checklist_main);
        }
    }
}
