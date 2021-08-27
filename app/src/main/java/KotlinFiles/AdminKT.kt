package KotlinFiles

import COMMON.Flags
import Controller_Classes.*
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.covidauditchecklists.Admin_user_checklist_details
import com.example.covidauditchecklists.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.covidauditchecklists.*
import com.example.covidauditchecklists.R

class AdminKT : AppCompatActivity() {
    private lateinit var  mAuth: FirebaseAuth
    private lateinit var edit_checklist:Button
    private lateinit var user_details: Button
    private lateinit var fetch_list:Button
    private lateinit var client_requests_list_button:Button
    private lateinit var admin_pending_auditor_list:Button
    private lateinit var admin_rejected_aud_list:Button
    private lateinit var admin_approved_auditor_list:Button
    var database :FirebaseDatabase= FirebaseDatabase.getInstance()
    var myRef : DatabaseReference = database.getReference("CheckList")
    lateinit var imageView: ImageView
    var data : Array<String> = emptyArray()
    //String[] data=null;
    lateinit var progressBar2: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        MainActivity.WRONG_CREDENTIALS.visibility =View.INVISIBLE
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (Build.VERSION.SDK_INT >= 21) {
            var window :Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
        // Define ActionBar object
        var actionBar: ActionBar = getSupportActionBar()!!
        var colorDrawable = ColorDrawable(Color.parseColor("#102F32"))
        actionBar.setBackgroundDrawable(colorDrawable)
        mAuth = FirebaseAuth.getInstance()
        admin_approved_auditor_list = findViewById(R.id.admin_approved_auditor_list)
        edit_checklist = findViewById(R.id.admin_modify_checklist)
        edit_checklist.isEnabled = false
        user_details   = findViewById(R.id.admin_user_performance)
        user_details.isEnabled = false
        client_requests_list_button   = findViewById(R.id.admin_client_requests)
        client_requests_list_button.setEnabled(false)
        progressBar2 = findViewById(R.id.progressBar2)
        progressBar2.visibility = View.GONE
        fetch_data()
        //MODIFY CHECKLIST
        edit_checklist.setOnClickListener { v ->
          //  final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
           // edit_checklist.startAnimation(myAnim);
            Flags.auditorClientChecklist = 0
            var intent :Intent =  Intent(getApplicationContext(), Activity_List_Of_CheckList::class.java)
            startActivity(intent)
            finish()
        }
        //USER DETAIL AFTER SEARCHING
        user_details.setOnClickListener{ v ->
         //  var myAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim)
            //user_details.startAnimation(myAnim)
            var intent =  Intent(applicationContext, Admin_user_checklist_details::class.java)
            startActivity(intent)
            finish()
        }
        //CLIENT REQUESTS FOR AUDIT
        client_requests_list_button.setOnClickListener{ v ->
           // final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            //client_requests_list_button.startAnimation(myAnim);
            var intent =  Intent(applicationContext, admin_client_list::class.java)
            startActivity(intent)
            finish()
        }
        //admin_pending_auditor_list
        admin_pending_auditor_list =  findViewById(R.id.admin_pending_auditor_list)
        admin_pending_auditor_list.setOnClickListener{ v ->
           // final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim)
          //  admin_pending_auditor_list.startAnimation(myAnim);
            var intent =  Intent(applicationContext, ADMIN_AUD_PEND_LIST::class.java)
            startActivity(intent)
            finish()
        }
        //admin_pending_auditor_list
        admin_rejected_aud_list =  findViewById(R.id.admin_rejected_auditor_list);
        admin_rejected_aud_list.setOnClickListener{ v ->
           // final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
           // admin_rejected_aud_list.startAnimation(myAnim);
            var intent = Intent(applicationContext, Admin_Rej_Aud_List::class.java)
            startActivity(intent)
            finish()
        }
        //admin approved audit list
        admin_approved_auditor_list.setOnClickListener { v ->
            //final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
           // admin_approved_auditor_list.startAnimation(myAnim);
            var intent = Intent(getApplicationContext(), Admin_Approved_Audits::class.java)
            startActivity(intent)
            finish()
        }
    }

    //FETCH CHECKLIST FROM FIREBASE
    fun fetch_data(){
        progressBar2.visibility  = View.VISIBLE
        myRef.addValueEventListener(object : ValueEventListener {
            @Override
            public override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fetcheddata :String = dataSnapshot.getValue() as String
                data = fetcheddata.split(",").toTypedArray()
                progressBar2.setVisibility(View.INVISIBLE)
                user_details.setEnabled(true)
                edit_checklist.setEnabled(true)
                client_requests_list_button.setEnabled(true)
            }
            override fun onCancelled(error: DatabaseError) {
                progressBar2.setVisibility(View.INVISIBLE)
               // Log.w("TAG", "Failed to read value.", kotlin.error.toException())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menu1 = menuInflater
        menu1.inflate(R.menu.menu_file, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.sign_out) {
            AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are You Sure").setMessage("Sign Out from Application")
                    .setPositiveButton("Sign Out") { dialog: DialogInterface?, _: Int ->
                        mAuth.signOut()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int -> }.show()
        }
        return true
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setIcon(com.example.covidauditchecklists.R.drawable.ic_baseline_exit)
                .setTitle("Are you Sure you want to Log out ?").setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                    super.onBackPressed()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
         }.setNegativeButton("No", { dialog: DialogInterface?, which: Int -> }).show()
    }

}