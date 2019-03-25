package com.example.nigl2.a2dblackjack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_advertisment.*
import kotlinx.android.synthetic.main.activity_game.*

class AdvertismentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisment)

        button_advert_skip.setOnClickListener {
            val myPreference = MyPreference(this)
            myPreference.setCredits(myPreference.getCredits() + 1000)
            finish()
        }
    }
}
