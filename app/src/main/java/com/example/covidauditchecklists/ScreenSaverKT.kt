package com.example.covidauditchecklists

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
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
        imageView = findViewById(R.id.imageView)
        imageView.setBackgroundResource(R.drawable.green)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.screen_saver_progressBar2)
        progressBar.visibility = View.VISIBLE

        var counDownTimer  = object :CountDownTimer(1000,3){
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