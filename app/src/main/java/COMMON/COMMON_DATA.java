package COMMON;

import android.content.Context;
import android.content.Intent;

import Controller_Classes.activity_selected_list_daa;

public class COMMON_DATA {
   static public String sNAME_common ="USER_NAME";
   static public String sID_common = "";
   static public String sEmail_common = "";
   static public String sCheckList_common = "";
   static public String sCurrentUser_CheckList_common = "";
   static public String sCurrentUserStar_common = "";


   public static void Admin_aud_pend_list_selected_data(String[] selectedList, Context context) {
      Intent intent = new Intent(context.getApplicationContext(), activity_selected_list_daa.class);
      intent.putExtra("Selected_data",selectedList);
      context.startActivity(intent);
   }
}
