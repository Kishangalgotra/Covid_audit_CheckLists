package COMMON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.admin;
import com.example.covidauditchecklists.image_proof;
import com.example.covidauditchecklists.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import COMMON.*;
import Controller_Classes.ADMIN_AUD_PEND_LIST;
import Controller_Classes.Activity_List_Of_CheckList;
import Controller_Classes.Admin_Rej_Aud_List;
import Controller_Classes.admin_client_list;
import Controller_Classes.auditor_client_list;
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Model.MODEL_ADMI_REJECTED_LIST;
import Model.MODEL_AVAILABLE_AUDITORS;
import Model.MODEL_List_Of_CheckList;
import Model.Users_data;

import java.net.URL;
import java.util.Date;

public class FIREBASE_COMMON extends COMMON_DATA {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef_fetch_checklist;
    public static  ValueEventListener mSendEventListner;
    public static DatabaseReference myRef_fetch_Rejected;
    public static  ValueEventListener mSendEventListnerRejected;
    //SET CHECKLIST AS MAIN
    public static void SET_MAIN_CHECKIST(String commaseparatedlist){
        DatabaseReference myRef = database.getReference("CheckList");
        myRef.setValue(commaseparatedlist);
    }

    //DELETE CHECKLIST FROM LIST OF CHECKLIST
    public static void delete_checklist(String checklistName){
        DatabaseReference myRef = database.getReference("List_Of_CheckList");
        myRef.child(checklistName).removeValue();
        Activity_List_Of_CheckList.list_of_checklist_list.removeAllViews();
        Activity_List_Of_CheckList.mAdapter_list_of_checklist_list.notifyDataSetChanged();
        removeListener();
    }

    //FETCH LIST OF CHECKLIST FROM FIREBASE
    public static void FETCH_LIST_OF_CHECKLISTS(Context context){
        myRef_fetch_checklist = database.getReference("List_Of_CheckList");
        ValueEventListener valueEventListener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String checkListName = ds.child("checkListname").getValue(String.class);
                        String CheckList = ds.child("checkList").getValue(String.class);
                        String MainList = ds.child("mainList").getValue(String.class);
                        Activity_List_Of_CheckList.list_of_checklist_list.removeAllViews();
                        MODEL_List_Of_CheckList model_list_of_checkList =
                                new MODEL_List_Of_CheckList(CheckList, false, checkListName, MainList);
                        Activity_List_Of_CheckList.model_list_of_checkLists.add(model_list_of_checkList);
                        Activity_List_Of_CheckList.mAdapter_list_of_checklist_list.notifyDataSetChanged(); }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef_fetch_checklist.addValueEventListener(valueEventListener);
        mSendEventListner = valueEventListener;
        Activity_List_Of_CheckList.fetch_list_checklist = 0;
    }

    public static void removeListener(){
        if (mSendEventListner != null) {
            myRef_fetch_checklist.removeEventListener(mSendEventListner);
        }
    }

    //ADD LIST OF CHECKLIST TO FIREBASE
    public static void ADD_CHECKLIST_IN_LIST_OF_CHECKLISTS(View view,MODEL_List_Of_CheckList model_list_of_checkList) {
        DatabaseReference myRef = database.getReference("List_Of_CheckList");
        myRef.child(model_list_of_checkList.getCheckListname()).setValue(model_list_of_checkList).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                removeListener();
                Activity_List_Of_CheckList.list_of_checklist_list.removeAllViews();
                Activity_List_Of_CheckList.mAdapter_list_of_checklist_list.notifyDataSetChanged();
            } else if(task.isCanceled()) {
                //progressBar_admin_auditor_list.setVisibility(View.GONE);
            } else{
                //progressBar_admin_auditor_list.setVisibility(View.GONE);
                Log.i("Failed","user");
            }
        });
    }

    //REJECT AUDITOR AUDITED DATA
    public static void REJECT_AUDITOR_AUDITED_DATA(MODEL_ADMIN_PEND_AUDIT_REQ model_admin_pend_audit_req){
        DatabaseReference databaseReference = database.getReference("REJECTED_AUDITOR_AUDIT");
        String Client_name =  model_admin_pend_audit_req.getClient_name();
        String Client_rating =  model_admin_pend_audit_req.getClient_rating();
        String Client_selected_items =  model_admin_pend_audit_req.getClient_selected_items();
        String Auditor_UID = model_admin_pend_audit_req.getAuditor_UID();
        String Auditor_Name = model_admin_pend_audit_req.getAuditor_NAME();
        String Client_UID  = model_admin_pend_audit_req.getClient_UID();
        MODEL_ADMI_REJECTED_LIST model_admi_rejected_list
                = new MODEL_ADMI_REJECTED_LIST(Auditor_UID,Auditor_Name,Client_UID, Client_name,
                Client_rating, Client_selected_items,false);
        String Unique_ID =model_admin_pend_audit_req.getRandomUID();
        databaseReference.child(Long.toString(new Date().getTime())).setValue(model_admi_rejected_list)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ADMIN_AUD_PEND_LIST.removeListenerPend();
                        ADMIN_AUD_PEND_LIST.model_admin_pend_audit.remove(ADMIN_AUD_PEND_LIST.Clicked_DATA_position);
                        FirebaseDatabase.getInstance().getReference("PEND_APPROVAL").child(Unique_ID).removeValue();
                        ADMIN_AUD_PEND_LIST.recyclerView_PEND_AUD_LIST.removeAllViews();
                        ADMIN_AUD_PEND_LIST.mAdapter_recycler_PEND_AUD_LIST.notifyDataSetChanged();
                        Log.i("SUCCESS", "user");
                    } else if (task.isCanceled()) {
                        //Toast.makeText(context.getApplicationContext(),"Uploading",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("Failed", "user");
                    }
        });

    }

    //APPROVE AUDITOR AUDITED DATA
    public static void APPROVE_AUDITOR_AUDITED_DATA(MODEL_ADMIN_PEND_AUDIT_REQ model_admin_pend_audit_req,Context context){
        DatabaseReference databaseReference = database.getReference("Users_data");
        String Client_name =  model_admin_pend_audit_req.getClient_name();
        String Client_rating =  model_admin_pend_audit_req.getClient_rating();
        String Client_selected_items =  model_admin_pend_audit_req.getClient_selected_items();
        Users_data users_data =  new Users_data(Client_name,Client_rating,Client_selected_items);
        Toast.makeText(context.getApplicationContext(),databaseReference.getParent().toString(),Toast.LENGTH_SHORT).show();
        databaseReference.child(model_admin_pend_audit_req.getClient_UID()).removeValue();
        String Unique_ID =model_admin_pend_audit_req.getRandomUID();
        databaseReference.child(model_admin_pend_audit_req.getClient_UID()).setValue(users_data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ADMIN_AUD_PEND_LIST.removeListenerPend();
                        ADD_IN_APPROVED_AUDITS(model_admin_pend_audit_req);
                        ADMIN_AUD_PEND_LIST.model_admin_pend_audit.remove(ADMIN_AUD_PEND_LIST.Clicked_DATA_position);
                        ADMIN_AUD_PEND_LIST.recyclerView_PEND_AUD_LIST.removeAllViews();
                        ADMIN_AUD_PEND_LIST.mAdapter_recycler_PEND_AUD_LIST.notifyDataSetChanged();
                        FirebaseDatabase.getInstance().getReference("PEND_APPROVAL").child(Unique_ID).removeValue();
                        Log.i("SUCCESS", "user");
                    } else if (task.isCanceled()) {
                        //Toast.makeText(context.getApplicationContext(),"Uploading",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("Failed", "user");
                    }
                });
    }

    public static void ADD_IN_APPROVED_AUDITS(MODEL_ADMIN_PEND_AUDIT_REQ model_admin_pend_audit_req){
        DatabaseReference databaseReference = database.getReference("APPROVED_AUDITOR_AUDIT");
        databaseReference.child(Long.toString(new Date().getTime())).setValue(model_admin_pend_audit_req)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("SUCCESS", "user");
                    } else if (task.isCanceled()) {
                        Log.i("Cancelled", "user");
                    } else {
                        Log.i("Failed", "user");
                    }
        });
    }

    //FETCH REJECTED AUDITOR LIST NAME FORM FIREBASE
    public static void FETCH_REJECTED_AUD_LIST(Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef_fetch_Rejected = database.getReference("REJECTED_AUDITOR_AUDIT");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String Auditor_name = ds.child("auditor_NAME").getValue(String.class);
                        String Auditor_UID = ds.child("auditor_UID").getValue(String.class);
                        String Client_UID = ds.child("client_UID").getValue(String.class);
                        String Client_name = ds.child("client_name").getValue(String.class);
                        String Client_rating = ds.child("client_rating").getValue(String.class);
                        String Client_selected_items = ds.child("client_selected_items").getValue(String.class);
                        //Toast.makeText(context.getApplicationContext(),Auditor_UID+ " :: " + Auditor_name, Toast.LENGTH_SHORT).show();
                        MODEL_ADMI_REJECTED_LIST model_admi_rejected_list
                                = new MODEL_ADMI_REJECTED_LIST(Auditor_UID, Auditor_name, Client_UID, Client_name,
                                Client_rating, Client_selected_items, false);
                        Admin_Rej_Aud_List.mModelList_ADM_REJ_AUD_LIST.add(model_admi_rejected_list);
                        // auditor_client_list.recyclerView_auditor_client.removeAllViews();
                        Admin_Rej_Aud_List.mAdapter_ADM_REJ_AUD_LIST.notifyDataSetChanged();
                    }
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef_fetch_Rejected.addValueEventListener(valueEventListener);
        mSendEventListnerRejected = valueEventListener;
    }

    public static void removeListenerRejected(){
        if(myRef_fetch_Rejected != null){
            myRef_fetch_Rejected.removeEventListener(mSendEventListnerRejected);
        }
    }

    //FOR PENDING AUDITOR REQUEST IN ADMIN SECTION
    public static void ADD_ENTRY_IN_PEND_AUDIT_REQ(String ID,MODEL_ADMIN_PEND_AUDIT_REQ model_admin_pend_audit_req){
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("PEND_APPROVAL").child(model_admin_pend_audit_req.getRandomUID()).setValue(model_admin_pend_audit_req)
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("SUCCESS", "user");
            } else if (task.isCanceled()) {
                //Toast.makeText(context.getApplicationContext(),"Uploading",Toast.LENGTH_LONG).show();
            } else {
                Log.i("Failed", "user");
            }
        });
    }

    //FOR ADDING ENTRY IN AVAILABLE AUDITOR
    public static void ADD_ENTRY_IN_AVAILABLE_AUDITORS(String ID){
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Auditors").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                COMMON_DATA.sNAME_common = snapshot.child("fullname").getValue(String.class);
                //Toast.makeText(user.this,USER_NAME,Toast.LENGTH_SHORT);
                if(!COMMON_DATA.sNAME_common.equalsIgnoreCase("")){
                    if(!COMMON_DATA.sNAME_common.equalsIgnoreCase("USER_NAME")) {
                        getUserName(ID);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void getUserName(String ID){
        //OBJECT CREATION
        MODEL_AVAILABLE_AUDITORS auditing_request_model = new MODEL_AVAILABLE_AUDITORS(COMMON_DATA.sNAME_common,ID);
        FirebaseDatabase.getInstance().getReference("AVAILABLE_AUDITORS").child(ID)
                .setValue(auditing_request_model).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.i("Success","user");
            } else if(task.isCanceled()){
            } else{
                Log.i("Failed","user");
            }
        });
    }

    //CODE TO GET USER NAME FROM DATABASE
    public static void GET_AUDITOR_NAME(){
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Auditors").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sNAME_common = snapshot.child("fullname").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void GET_CLIENT_NAME(){
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Users").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sNAME_common = snapshot.child("fullname").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //CODE TO REMOVE CLIENT NAME FROM AUDITOR LIST
    public static void REMOVE_CLIENT_FROM_AUDITOR_LIST(){
        String Client_id = auditor_client_list.Clicked_user_ID;
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.getReference("AUDITOR_CLIENT_LIST").child(ID).child(Client_id).removeValue().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        //progressBar_admin_auditor_list.setVisibility(View.GONE);
                    } else if(task.isCanceled()){
                        //progressBar_admin_auditor_list.setVisibility(View.GONE);
                    } else{
                        //progressBar_admin_auditor_list.setVisibility(View.GONE);
                        Log.i("Failed","user");
                    }
          });
    }

}
