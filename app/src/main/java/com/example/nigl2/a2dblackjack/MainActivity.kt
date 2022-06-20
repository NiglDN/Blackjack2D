package com.example.nigl2.a2dblackjack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * class which represents starting screen of app, has 3 buttons and its according listeners to start other activites
 * also creates the card-deck
 */
class MainActivity : AppCompatActivity() {
    /**
     * overwrites default onCreate implementation
     * sets listeners and creates carddeck
     */
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
