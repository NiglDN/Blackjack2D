package com.example.nigl2.a2dblackjack

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    var credit = 5000                           // credits of player
    var betTotal = 0                            // credits the player deposited for the game
    var playerCardScore = mutableListOf<Int>()  // list of all the cards in players hand
    var dealerCardScore = mutableListOf<Int>()  // list of all the cards in dealers hand
    var playerScore = 0                         // score of the players current hand
    var dealerScore = 0                         // score of the dealers current hand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //build the field and reset everything
        resetField()

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
            else
                return@setOnClickListener

            button_playfield_stand.isClickable = true
            button_playfield_stand.visibility = View.VISIBLE

            button_playfield_next.isClickable = true
            button_playfield_next.visibility = View.VISIBLE

            button_playfield_deal.isClickable = false
            button_playfield_deal.visibility = View.INVISIBLE

            //first turn
            dealerDraws()
            playerDraws()
            playerDraws()
            //first turn
            //blackjack
            if (playerCardScore.size == 2 && playerScore == 21){
                if (dealerScore < 10)
                    playerWins()
                else {
                    dealerDraws()
                    if (dealerScore == 21)
                        playerTie()
                    else
                        playerWins()
                }
            }
        }

        button_playfield_next.setOnClickListener {
            playerDraws()
            //checks if you busted
            if (playerScore > 21)
                playerLoses()
        }

        button_playfield_stand.setOnClickListener{
            dealerPlays()
        }
    }

    fun bettingCredits (bet: Int){
        if ((credit - betTotal - bet) < 0) {
            Log.i("GameActivity", "Limit Erreicht")
            return
        }
        betTotal = betTotal + bet
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_currentBalance.text = (credit - betTotal).toString()
    }

    fun dealerDraws (){
        dealerCardScore.add(SingletonCards.cardDeckList[0].second)
        dealerScore += dealerCardScore[dealerCardScore.lastIndex]
        textView_playerfield_dealerScore.text = dealerScore.toString()

        linearlayout_playfield_dealer.addView(drawCard())
        scrollview_playfield_dealer.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    fun playerDraws () {
        playerCardScore.add(SingletonCards.cardDeckList[0].second)
        playerScore += playerCardScore[playerCardScore.lastIndex]
        textView_playerfield_playerScore.text = playerScore.toString()


        linearlayout_playfield_player.addView(drawCard())
        scrollview_playfield_player.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    fun drawCard() : ImageView {
        var layoutParam = ViewGroup.MarginLayoutParams(222, 323)
        layoutParam.setMargins(20, 0, 20, 0)
        var imgview = ImageView(this)
        imgview.setImageResource(SingletonCards.cardDeckList[0].first)
        imgview.layoutParams = layoutParam
        imgview.requestLayout()
        imgview.setBackgroundColor(Color.WHITE)
        SingletonCards.cardDeckList.removeAt(0)
        return imgview
    }

    fun resetField(){
        //hide buttons
        button_playfield_stand.isClickable = false
        button_playfield_stand.visibility = View.INVISIBLE

        button_playfield_next.isClickable = false
        button_playfield_next.visibility = View.INVISIBLE

        button_playfield_doubled.isClickable = false
        button_playfield_doubled.visibility = View.INVISIBLE

        button_playfield_split.isClickable = false
        button_playfield_split.visibility = View.INVISIBLE

        button_playfield_deal.isClickable = true
        button_playfield_deal.visibility = View.VISIBLE

        linearlayout_playfield_dealer.removeAllViews()
        linearlayout_playfield_player.removeAllViews()
        SingletonCards.cardDeckList.clear()
        SingletonCards.createDeck()

        textView_playfield_currentBalance.text = credit.toString()
        betTotal = 0
        textView_playfield_betTotal.text = betTotal.toString()
        playerScore = 0
        textView_playerfield_playerScore.text = playerScore.toString()
        dealerScore = 0
        textView_playerfield_dealerScore.text = dealerScore.toString()
    }

    fun dealerPlays(){
        //checks if dealer should draw
        var drawAgain = true
        // must draw till at least 16
        while (drawAgain){

            dealerDraws()
            if (dealerScore > 16) {
                drawAgain = false
            }
        }
        checkLoseCondition()
    }

    fun checkLoseCondition(){
        if(dealerScore > 21)
            playerWins()
        else if(dealerScore > playerScore){
            playerLoses()
        }
        else if(dealerScore < playerScore){
            playerWins()
        }
        else if(dealerScore == playerScore){
            playerTie()
        }
    }

    fun playerWins() {
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("Congratulations")
        // Display a message on alert dialog
        builder.setMessage("YOU WON")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK"){dialog, which ->
            // Do something when user press the positive button
            credit += betTotal
            Toast.makeText(applicationContext,"You won " + betTotal * 2,Toast.LENGTH_SHORT).show()
            resetField()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }

    fun playerLoses() {
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("Ohhhh")
        // Display a message on alert dialog
        builder.setMessage("YOU LOST")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK"){dialog, which ->
            // Do something when user press the positive button
            credit -= betTotal
            Toast.makeText(applicationContext,"You lost $betTotal",Toast.LENGTH_SHORT).show()
            resetField()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()

    }

    fun playerTie() {
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("OKAY")
        // Display a message on alert dialog
        builder.setMessage("You tied")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK"){dialog, which ->
            // Do something when user press the positive button
            Toast.makeText(applicationContext,"You get your credit back",Toast.LENGTH_SHORT).show()
            resetField()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()


    }


}
