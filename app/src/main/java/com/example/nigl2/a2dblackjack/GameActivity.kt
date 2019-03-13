package com.example.nigl2.a2dblackjack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_game.*
import android.widget.Toast

class GameActivity : AppCompatActivity() {

    var credit = 5000
    var betTotal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //create Deck and set Credits
        SingletonCards.createDeck()
        dealerDraws()
        dealerDraws()
        playerDraws()
        playerDraws()
        textView_playfield_currentBalance.text = credit.toString()

        //betButtons
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

        // actionButtons
        button_playfield_deal.setOnClickListener {
            if (betTotal != 0)
                Toast.makeText(this, "Bet accepted", Toast.LENGTH_SHORT).show()
            credit = credit - betTotal
            betTotal = 0
            textView_playfield_betTotal.text = betTotal.toString()
            textView_playfield_currentBalance.text = (credit - betTotal).toString()
            clearField()
        }

        button_playfield_next.setOnClickListener {
            playerDraws()
            checkLoseCondition()
        }

        button_playfield_stand.setOnClickListener{
            dealerPlays()
        }
    }

    fun bettingCredits (bet: Int){
        if ((credit - betTotal - bet) < 0){
            Log.i("GameActivity", "Limit Erreicht")
            return
        }
        betTotal = betTotal + bet
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_currentBalance.text = (credit - betTotal).toString()
    }

    fun dealerDraws (){
        val btn = Button(this)
        btn.text = SingletonCards.cardDeckList[0].first
        SingletonCards.cardDeckList.removeAt(0)
        linearlayout_playfield_dealer.addView(btn)
    }

    fun playerDraws () {
        val btn = Button(this)
        btn.text = SingletonCards.cardDeckList[0].first
        SingletonCards.cardDeckList.removeAt(0)
        linearlayout_playfield_player.addView(btn)
    }

    fun clearField(){
        linearlayout_playfield_dealer.removeAllViews()
        linearlayout_playfield_player.removeAllViews()
        SingletonCards.cardDeckList.clear()
        SingletonCards.createDeck()
    }

    fun checkLoseCondition(){

    }

    fun dealerPlays(){
        clearField()
    }
}
