package com.example.covidauditchecklists

import COMMON.COMMON_DATA
import Controller_Classes.auditor_client_list
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivityKT : AppCompatActivity() {
    var MAUTH : FirebaseAuth  =FirebaseAuth.getInstance()
    lateinit var EMAIL_SIGNING : EditText
    lateinit var PASSWORD_SIGNING : EditText
    lateinit var SIGN_IN :Button
    lateinit var SIGN_UP : Button
    lateinit var PROGRESSBAR :ProgressBar
    lateinit var ADMIN_SIGNING : TextView
    lateinit var USER_SEARCH_PAGE : TextView
    lateinit var AUDITOR_PANEL : TextView

    lateinit var WRONG_CREDENTIALS : TextView
    lateinit var NO_INTERNET :TextView
    var  myRef2 : DatabaseReference = FirebaseDatabase.getInstance().getReference("CheckList")
    var admin :Int = 0 
    lateinit var spinner : Spinner
    var DATABASE :FirebaseDatabase = FirebaseDatabase.getInstance() 
    private lateinit var mDatabase : DatabaseReference
    var DATA_FOR_UER_CHECKLIST :Array<String> = emptyArray()
    var run :Boolean= true 
    var POOR_NETWORK :Int= 0 
    var TO_USER_PROFILE :Int = 2 
    var h :Int = 0 
    //GLOBAL DECLARATION
    var myRef_admin_auditor :DatabaseReference= DATABASE.getReference("AUDITOR_FOR_SIGN_IN") 
    var myRef_admin_users :DatabaseReference = DATABASE.getReference("CLIENT_FOR_SIGN_IN") 
    var authorized_in_firebase :Int = 2 
    lateinit var checklistListener : ValueEventListener
    //var  myAnim  = AnimationUtils.loadAnimation(this, R.anim.anim) 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 21) {
            var window :Window= this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(this.getResources().getColor(R.color.black))
        }
        var actionBar: ActionBar
        actionBar = getSupportActionBar()!!
        var colorDrawable =  ColorDrawable(Color.parseColor("#102F32"))
        actionBar.setBackgroundDrawable(colorDrawable)

        var intent :Intent= getIntent()
        TO_USER_PROFILE =2
        SIGN_IN = findViewById(R.id.sign_in_button)
        NO_INTERNET = findViewById(R.id.no_inernet)
        NO_INTERNET.setVisibility(View.INVISIBLE)
        WRONG_CREDENTIALS = findViewById(R.id.wrong_credential)
        WRONG_CREDENTIALS.setVisibility(View.INVISIBLE)
        ADMIN_SIGNING = findViewById(R.id.admin_login)
        SIGN_UP = findViewById(R.id.sign_up_at_sign_in_screen)
        PROGRESSBAR = findViewById(R.id.progressBar)
        PROGRESSBAR.setVisibility(View.GONE)
        EMAIL_SIGNING =  findViewById(R.id.editTextTextEmailAddress)
        PASSWORD_SIGNING =  findViewById(R.id.editTextTextPassword)
       // AUDITOR_PANEL = findViewById(R.id.auditor_Panel)
        //temporary dropdown checking
        spinner =  findViewById(R.id.spinner1)
        // Spinner Drop down elements
        var categories = ArrayList<String>()
        categories.add("Auditor")
        categories.add("Client")
        // Creating adapter for spinner
        var dataAdapter =  ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter)
        spinner.setOnItemSelectedListener( CustomOnItemSelectedListener())
        //temporary dropdown checking
        USER_SEARCH_PAGE = findViewById(R.id.user_search_page)
        USER_SEARCH_PAGE.visibility = View.GONE
        //used for auditor common password 6789
        if (TO_USER_PROFILE == 5) {
            var myRef: DatabaseReference = DATABASE.getReference("AuditorPass") 
            var edittext = EditText(getApplicationContext())
            val builder = AlertDialog.Builder(this)
            builder.setView(edittext)
            builder.setTitle("Authenticate First").setMessage("Kindly Enter Passcode")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                PROGRESSBAR.setVisibility(View.VISIBLE)
                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var value: String = dataSnapshot.getValue().toString()

                        Log.d("TAG", "Value is: " + value)
                        if (value.equals(edittext.getText().toString())) {
                            PROGRESSBAR.setVisibility(View.INVISIBLE)
                            h = 2
                        } else {
                            startActivity(Intent(getApplicationContext(), MainActivity::class.java))
                            finish()
                            PROGRESSBAR.setVisibility(View.INVISIBLE)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
            builder.setNegativeButton("No") { dialog, which ->
                finish()
            }
            builder.setCancelable(false).show().setCanceledOnTouchOutside(false)
            //ON USER SEARCH CLICK
            USER_SEARCH_PAGE.setOnClickListener {
               // USER_SEARCH_PAGE.startAnimation(myAnim)
                startActivity(Intent(getApplicationContext(), Admin_user_checklist_details::class.java))
            }
            USER_SEARCH_PAGE.setOnClickListener {
              //  USER_SEARCH_PAGE.startAnimation(myAnim)
                startActivity(Intent(getApplicationContext(), Admin_user_checklist_details::class.java))
                //finish() 
            }
            //SIGN IN CLICK
            SIGN_IN.setOnClickListener {
               // SIGN_IN.startAnimation(myAnim)
                if (POOR_NETWORK == 0) {
                    SIGN_IN_FUNCTION()
                }
            }
            //SIGN UP CLICK
            SIGN_UP.setOnClickListener {
              //  SIGN_UP.startAnimation(myAnim)
                startActivity(Intent(getApplicationContext(), Sign_up::class.java))
                // finish() 
            }
            //ADMIN SIGN IN CLICK
            ADMIN_SIGNING.setOnClickListener {
               // ADMIN_SIGNING.startAnimation(myAnim)
                startActivity(Intent(getApplicationContext(), Sign_in_Admin::class.java))
                finish()
            }


        }
    }


    //SIGN IN FUNCTION WITH CHECKS
    public fun SIGN_IN_FUNCTION()  {
        var entity :String =spinner.getSelectedItem().toString()
        if(entity.equals("Client")) {
            TO_USER_PROFILE =1
        }

        //CHECK IF DEVICE HAVE A STABLE CONNECTION
        var connected :Boolean
        val connectivityManager :ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetwork :NetworkInfo? = connectivityManager.activeNetworkInfo
        connected = activenetwork?.isConnectedOrConnecting  == true
        /*if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.getState() ?:  == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true  //we are connected to a network
        } else
            connected = false*/
        //SIGN IN ACTIVITY STARTS HERE
        if (connected) {
            NO_INTERNET.setVisibility(View.INVISIBLE)
            WRONG_CREDENTIALS.setVisibility(View.INVISIBLE)
            val email:String = EMAIL_SIGNING.getText().toString().trim()
            val password:String = PASSWORD_SIGNING.getText().toString().trim()
            if (email.isEmpty()) {
                EMAIL_SIGNING.setError("Please Fill Email")
                EMAIL_SIGNING.requestFocus()
                return 
            }
            COMMON_DATA.sEmail_common = email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                EMAIL_SIGNING.setError("Please fill Email Correctly")
                EMAIL_SIGNING.requestFocus() 
                return 
            }
            if (password.isEmpty()) {
                PASSWORD_SIGNING.setError("Please fill password")
                PASSWORD_SIGNING.requestFocus()
                return 
            }
            EMAIL_SIGNING.setEnabled(false)
            PASSWORD_SIGNING.setEnabled(false)
            PROGRESSBAR.setVisibility(View.VISIBLE)
            Toast.makeText(applicationContext, "Starting Authentication.", Toast.LENGTH_SHORT).show() 
            //Getting Data For User CheckList
             checklistListener = object: ValueEventListener {
                @Override
                 override  fun onDataChange(dataSnapshot:DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DATA_FOR_UER_CHECKLIST = dataSnapshot.getValue(String::class.java) as Array<String>
                    } else {
                        NO_INTERNET.setText("Poor Connection")
                        NO_INTERNET.setVisibility(View.VISIBLE)
                    }
                }
                @Override
                override  fun onCancelled(error:DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }
            } 
            myRef2.addValueEventListener(checklistListener)

           //AUTHENTICATING GIVEN CREDENTIALS
            MAUTH.signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                if (task.isSuccessful()) {
                    PROGRESSBAR.setVisibility(View.INVISIBLE)
                    if (TO_USER_PROFILE == 1) {
                        WRONG_CREDENTIALS.setText("Verifying Credentials")
                        WRONG_CREDENTIALS.setVisibility(View.VISIBLE)
                        CHECK_IF_CLIENT(email)
                    } else {
                        WRONG_CREDENTIALS.setText("Verifying Credentials")
                        WRONG_CREDENTIALS.setVisibility(View.VISIBLE)
                        CHECK_IF_AUDITOR(email)
                    }
                } else {
                    PROGRESSBAR.setVisibility(View.INVISIBLE)
                    Toast.makeText(applicationContext, "Task Is Failed.", Toast.LENGTH_SHORT).show()
                    WRONG_CREDENTIALS.setVisibility(View.VISIBLE)
                }
                if (task.isCanceled()) {
                    PROGRESSBAR.setVisibility(View.INVISIBLE)
                    Toast.makeText(applicationContext, "Task Is Cancelled.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            NO_INTERNET.setText("No Internet")
            NO_INTERNET.setVisibility(View.VISIBLE)
            PROGRESSBAR.setVisibility(View.INVISIBLE)
        }
        //ENABLING DISABLED COMPONENTS
        EMAIL_SIGNING.setEnabled(true)
        PASSWORD_SIGNING.setEnabled(true)
        SIGN_IN.setEnabled(true)
    }

    private fun CHECK_IF_AUDITOR( emailss:String){
        var intent:Intent =  Intent(this, auditor_client_list::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("string-array", DATA_FOR_UER_CHECKLIST)
        CredCheck()
        myRef_admin_auditor.addListenerForSingleValueEvent(object :ValueEventListener {
            @Override
            override fun onDataChange( dataSnapshot :DataSnapshot) {
                for ( ds:DataSnapshot in dataSnapshot.getChildren()){
                    var email:String = ds.getValue().toString()
                    if(emailss.equals(email)){
                        startActivity(intent)
                        finish()
                        authorized_in_firebase =2 
                        break
                    }
                }
            }
            override fun onCancelled( error:DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        }) 
    }

    private fun CHECK_IF_CLIENT( emails:String) {
        var intent1 =  Intent(applicationContext, Searched_user_Data::class.java)
        intent1.putExtra("searched_user_condition", 1)
        CredCheck()
        myRef_admin_users.addListenerForSingleValueEvent(object :ValueEventListener {
            @Override
             override fun onDataChange(dataSnapshot:DataSnapshot) {
                for ( ds :DataSnapshot in  dataSnapshot.getChildren()) {
                    var email = ds.getValue().toString()
                    if (email.equals(emails)) {
                        startActivity(intent1)
                        finish()
                        authorized_in_firebase = 2
                        break
                    }
                }
            }
            @Override
             override  fun onCancelled(error:DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        }) 
    }

     fun CredCheck(){
        WRONG_CREDENTIALS.setText("Verifying Credentials") 
        WRONG_CREDENTIALS.setVisibility(View.VISIBLE) 
        var handler = Handler() 
        handler.postDelayed({
        WRONG_CREDENTIALS.setText("Wrong Credentials") 
        WRONG_CREDENTIALS.setVisibility(View.VISIBLE) 
        },3000)

    }

     override fun onDestroy() {
        super.onDestroy() 
        if(myRef2 != null){
           // myRef2.removeEventListener(checklistListener) 
        }
    }

     override fun onStop() {
        super.onStop() 
        if(myRef2 != null){
            //myRef2.removeEventListener(checklistListener) 
        }
    }
}