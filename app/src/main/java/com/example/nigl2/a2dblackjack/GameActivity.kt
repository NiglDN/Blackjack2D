package com.example.nigl2.a2dblackjack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_game.*
import android.widget.Toast

class GameActivity : AppCompatActivity() {

    var credit = 5000
    var betTotal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        textView_playfield_currentBalance.text = credit.toString()

        button_playfield_bet1.setOnClickListener {
            bettingCredits(1)
        }

        button_playfield_bet5.setOnClickListener {
            bettingCredits(5)
        }

        button_playfield_bet25.setOnClickListener {
            bettingCredits(25)
        }

        button_playfield_bet100.setOnClickListener {
            bettingCredits(100)
        }

        button_playfield_bet500.setOnClickListener {
            bettingCredits(500)
        }

        button_playfield_bet5000.setOnClickListener {
            bettingCredits(5000)
        }

        button_playfield_betAllIn.setOnClickListener {
            bettingCredits(credit-betTotal)
        }

        button_playfield_reverse.setOnClickListener {
            betTotal = 0
            bettingCredits(0)
        }

        button_playfield_deal.setOnClickListener {
            if (betTotal != 0)
                Toast.makeText(this, "Bet accepted", Toast.LENGTH_SHORT).show()
            credit = credit - betTotal
            betTotal = 0
            textView_playfield_betTotal.text = betTotal.toString()
            textView_playfield_currentBalance.text = (credit - betTotal).toString()
        }
    }

    fun bettingCredits (bet: Int){

        if ((credit - betTotal - bet) < 0){
            Log.i("GameActivity", "Limit Erreicht")
            return
        }
        betTotal = betTotal + bet;
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_currentBalance.text = (credit - betTotal).toString()
    }

}
