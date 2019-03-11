package com.example.nigl2.a2dblackjack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    var credit = 5000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        textView_playfield_currentBalance.text = credit.toString()

        button_playfield_bet500.setOnClickListener {
            credit = credit - 500;
            var bet = 0;
            if (textView_playfield_betTotal.text != null) {
                textView_playfield_betTotal.text = (textView_playfield_betTotal.text.toString().toInt() + 500).toString()
            } else {
                textView_playfield_currentBalance.text = "500"
            }
            textView_playfield_currentBalance.text = credit.toString()
        }
    }


}
