package Controller_Classes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.example.covidauditchecklists.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class activity_selected_list_daa extends AppCompatActivity {

    ListView admin_pend_list_selected_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_list_daa);
      /*  if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#102F32"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);*/

        //getActionBar().setTitle("Client's Audited Data");
        Intent intent = getIntent();
        String[] selected_Data_Array = intent.getStringArrayExtra("Selected_data");
        admin_pend_list_selected_data = findViewById(R.id.admin_pend_list_selected_data);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,selected_Data_Array);
        admin_pend_list_selected_data.setAdapter(adapter);
    }
}