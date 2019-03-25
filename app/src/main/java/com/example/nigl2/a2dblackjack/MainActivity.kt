package com.example.nigl2.a2dblackjack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        SingletonCards.createDeck()

        button_game_start.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        button_statistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        button_exit.setOnClickListener {
            finish()
        }
    }
}
