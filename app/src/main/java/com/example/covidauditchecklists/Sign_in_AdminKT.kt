package com.example.covidauditchecklists

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Sign_in_AdminKT :AppCompatActivity() {
    lateinit var sign_in: Button
    lateinit var wrong_cred: TextView
    lateinit var passcode: EditText
    lateinit var admin_wrong_credentials: TextView
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("admin")
    private val mAuth: FirebaseAuth? = null
    lateinit var sign_in_admin_progress_bar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in__admin)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#102F32"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        sign_in = findViewById(R.id.sign_in_button_admin)
        admin_wrong_credentials = findViewById(R.id.admin_wrong_credentials)
        sign_in_admin_progress_bar = findViewById(R.id.sign_in_admin_progress_bar)
        sign_in_admin_progress_bar.visibility = View.INVISIBLE
        admin_wrong_credentials.visibility = View.GONE
        passcode = findViewById(R.id.passcode_admin)
        passcode.setText("")
    }
}