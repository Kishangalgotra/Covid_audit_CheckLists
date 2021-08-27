package com.example.covidauditchecklists

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ScreenSaverKT : AppCompatActivity() {
    var admin:Int = 0
    private lateinit var mAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    var database :FirebaseDatabase= FirebaseDatabase.getInstance();
    var myRef: DatabaseReference = database.getReference("Users_data");
    lateinit var currentUser: FirebaseUser
    lateinit var windows: Window
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_screen__saver)
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

        imageView = findViewById(R.id.imageView)
        imageView.setBackgroundResource(R.drawable.green)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.screen_saver_progressBar2)
        progressBar.visibility = View.VISIBLE

        val counDownTimer  = object :CountDownTimer(1000,3){
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                startActivity( Intent(getApplicationContext(), MainActivityKT::class.java))
                finish()
            }
        }
        counDownTimer.start()
    }
}