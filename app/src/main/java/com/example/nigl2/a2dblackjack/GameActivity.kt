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
    var betTotal = 0                          // credits the player deposited for the game
    var playerCardScore = mutableListOf<Int>()  // list of all the cards in players hand
    var dealerCardScore = mutableListOf<Int>()  // list of all the cards in dealers hand
    var playerScore = 0                         // score of the players current hand
    var dealerScore = 0                         // score of the dealers current hand
    var playerAssHands = 0                      // number of player ass
    var dealerAssHands = 0                  // number of player ass
    var evenMoney = false
    var insurancemoney = 0
    var insurance = false


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
            bettingCredits(credit - betTotal)
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



            if(betTotal < credit) {
                button_playfield_doubled.isClickable = true
                button_playfield_doubled.visibility = View.VISIBLE
            } else {
                button_playfield_doubled.isClickable = false
                button_playfield_doubled.visibility = View.INVISIBLE
            }



            //jetons for bet disappear

            button_playfield_bet1.isClickable = false
            button_playfield_bet1.visibility = View.INVISIBLE

            button_playfield_bet5.isClickable = false
            button_playfield_bet5.visibility = View.INVISIBLE

            button_playfield_bet25.isClickable = false
            button_playfield_bet25.visibility = View.INVISIBLE

            button_playfield_bet100.isClickable = false
            button_playfield_bet100.visibility = View.INVISIBLE

            button_playfield_bet500.isClickable = false
            button_playfield_bet500.visibility = View.INVISIBLE

            button_playfield_bet5000.isClickable = false
            button_playfield_bet5000.visibility = View.INVISIBLE

            button_playfield_betAllIn.isClickable = false
            button_playfield_betAllIn.visibility = View.INVISIBLE



            //first turn

            playerDraws()
            playerDraws()
            dealerDraws()

            //first turn
            //blackjack
            if (dealerScore == 11 && playerScore == 21 && credit >= betTotal/2) {

                val builder = AlertDialog.Builder(this)
                // Set the alert dialog title
                builder.setTitle("Even Money?")
                // Display a message on alert dialog
                builder.setMessage("Dealers open card is an Ace, do you want to take even money on your BlackJack (Winnings would be 2x)" + "\n" + "Cost is Half of your bet: " + betTotal / 2)

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES") { dialog, which ->
                    // Do something when user press the positive button
                    insurancemoney = betTotal / 2
                    credit -= insurancemoney
                    evenMoney = true
                    Toast.makeText(applicationContext, "Even Money taken", Toast.LENGTH_SHORT).show()
                    textView_playfield_betTotal.text = betTotal.toString()
                    textView_playfield_currentBalance.text = "Credits:  " + (credit - betTotal).toString()
                    /*dealerDraws()
                    checkLoseCondition(insurancemoney, evenMoney = true, insurance = false)
                    */

                }
                builder.setNegativeButton("NO") { dialog, which ->
                    Toast.makeText(applicationContext, "Even Money declined", Toast.LENGTH_SHORT).show()
                    textView_playfield_betTotal.text = betTotal.toString()
                    textView_playfield_currentBalance.text = "Credits:  " + (credit - betTotal).toString()
                   /* dealerDraws()
                    checkLoseCondition(insurancemoney, evenMoney = false, insurance = false)
                    */
                }
                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()
                // Display the alert dialog on app interface
                dialog.show()

            } else if (dealerScore == 11 && playerScore < 21 && credit >= betTotal/2) {


                val builder = AlertDialog.Builder(this)
                // Set the alert dialog title
                builder.setTitle("Insurance?")
                // Display a message on alert dialog
                builder.setMessage("Dealers open card is an Ace, do you want to take Insurance?" + "\n" + "If Dealer has an Blackjack, you get your bet back"+ "Cost is Half of your bet: " + betTotal / 2)

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES") { dialog, which ->
                    // Do something when user press the positive button
                    insurancemoney = betTotal / 2
                    credit -= insurancemoney
                    insurance = true
                    Toast.makeText(applicationContext, "Insurance taken", Toast.LENGTH_SHORT).show()
                    textView_playfield_betTotal.text = betTotal.toString()
                    textView_playfield_currentBalance.text = "Credits:  " + (credit - betTotal).toString()
                    /*dealerDraws()
                    if (dealerScore == 21) {
                        checkLoseCondition(insurancemoney, evenMoney = false, insurance = true)
                    }*/

                }
                builder.setNegativeButton("NO") { dialog, which ->
                    Toast.makeText(applicationContext, "Even Money declined", Toast.LENGTH_SHORT).show()

                    textView_playfield_betTotal.text = betTotal.toString()
                    textView_playfield_currentBalance.text = "Credits:  " + (credit - betTotal).toString()
                   /* if(dealerScore == 21) {
                        checkLoseCondition(insurancemoney, evenMoney = false, insurance = false)
                    } */
                }
                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()
                // Display the alert dialog on app interface
                dialog.show()

            }


        //allow split
        // if(playerCardScore[0] == playerCardScore[1]){
        button_playfield_split.isClickable = true
        button_playfield_split.visibility = View.VISIBLE
        //}
    }

        button_playfield_split.setOnClickListener {
            //checks if it is possible with the current credit
            button_playfield_split.isClickable = false
            button_playfield_split.visibility = View.INVISIBLE
        }

        button_playfield_next.setOnClickListener {
            playerDraws()
            button_playfield_split.isClickable = false
            button_playfield_split.visibility = View.INVISIBLE
            button_playfield_doubled.isClickable = false
            button_playfield_doubled.visibility = View.INVISIBLE
            //checks if you busted
            if (playerScore > 21)
                playerLoses(insurance)
        }

        button_playfield_stand.setOnClickListener {
            dealerPlays()
        }

        button_playfield_doubled.setOnClickListener {
            //checks if it is possible with the current credit
            if ((credit - betTotal) < betTotal)
                return@setOnClickListener
            bettingCredits(betTotal)
            playerDraws()
            //checks if you busted
            if (playerScore > 21)
                playerLoses(insurance)
            else {
                dealerPlays()
            }
        }
    }

    fun bettingCredits(bet: Int) {
        if ((credit - betTotal - bet) < 0) {
            Log.i("GameActivity", "Limit Erreicht")
            return
        }
        betTotal += bet
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_currentBalance.text = "Credits:  " + (credit - betTotal).toString()
    }


    fun dealerDraws() {
        dealerCardScore.add(SingletonCards.cardDeckList[0].second)
        dealerScore += dealerCardScore[dealerCardScore.lastIndex]
        // checks if hand gets hard
        if (dealerCardScore[dealerCardScore.lastIndex] == 11)
            dealerAssHands++
        if (dealerScore > 21 && dealerAssHands != 0) {
            dealerScore -= 10
            dealerAssHands--
        }
        textView_playerfield_dealerScore.text = dealerScore.toString()

        linearlayout_playfield_dealer.addView(drawCard())
        scrollview_playfield_dealer.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    fun playerDraws() {

        playerCardScore.add(SingletonCards.cardDeckList[0].second)
        playerScore += playerCardScore[playerCardScore.lastIndex]
        // checks if hand gets hard
        if (playerCardScore[playerCardScore.lastIndex] == 11)
            playerAssHands++
        if (playerScore > 21 && playerAssHands != 0) {
            playerScore -= 10
            playerAssHands--
        }
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



        // jetons reappear
        button_playfield_bet1.isClickable = true
        button_playfield_bet1.visibility = View.VISIBLE

        button_playfield_bet5.isClickable = true
        button_playfield_bet5.visibility = View.VISIBLE

        button_playfield_bet25.isClickable = true
        button_playfield_bet25.visibility = View.VISIBLE

        button_playfield_bet100.isClickable = true
        button_playfield_bet100.visibility = View.VISIBLE

        button_playfield_bet500.isClickable = true
        button_playfield_bet500.visibility = View.VISIBLE

        button_playfield_bet5000.isClickable = true
        button_playfield_bet5000.visibility = View.VISIBLE

        button_playfield_betAllIn.isClickable = true
        button_playfield_betAllIn.visibility = View.VISIBLE


        linearlayout_playfield_dealer.removeAllViews()
        linearlayout_playfield_player.removeAllViews()
        SingletonCards.cardDeckList.clear()
        SingletonCards.createDeck()

        textView_playfield_currentBalance.text = "Credits:  " + credit.toString()
        betTotal = 0
        textView_playfield_betTotal.text = betTotal.toString()
        playerScore = 0
        textView_playerfield_playerScore.text = playerScore.toString()
        dealerScore = 0
        textView_playerfield_dealerScore.text = dealerScore.toString()
        playerAssHands = 0
        dealerAssHands = 0
        playerCardScore.clear()
        dealerCardScore.clear()
        evenMoney = false
        insurancemoney = 0
        insurance = false

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
        checkLoseCondition(insurancemoney, evenMoney, insurance)
    }

    fun checkLoseCondition(insurancemoney: Int, evenMoney: Boolean, insurance: Boolean){
        if (dealerScore > 21) {
            playerWins()
        }
        else if(playerScore == dealerScore && playerScore < 21) {
            playerTie(insurance)
        }
        else if(dealerScore == 21 && playerScore == 21 && playerCardScore.size == 2 && dealerCardScore.size == 2 && evenMoney){
            playerTie(insurance)
        }
        else if(dealerScore == 21 && playerScore == 21  && playerCardScore.size == 2 && dealerCardScore.size == 2 && !evenMoney){
            playerTie(insurance)
        }
        else if (dealerScore == 21 && dealerCardScore.size == 2 && insurance && playerCardScore.size > 2) {
            playerLoses(insurance)
        } else if (dealerScore == 21 && dealerCardScore.size == 2 && !insurance && playerCardScore.size > 2) {
            playerLoses(insurance)
        }
        else if (dealerScore > playerScore && dealerCardScore.size != 2 && playerScore != 2) {
            playerLoses(insurance)
        } else if (dealerScore > playerScore && dealerCardScore.size != 2 && playerScore == 2) {
            playerLoses(insurance)
        } else if (dealerScore > playerScore && dealerCardScore.size ==2 && playerScore != 2) {
            playerLoses(insurance)
        } else if (dealerScore > playerScore && dealerCardScore.size == 2 && playerScore == 2) {
            playerLoses(insurance)
        } else if (dealerScore < playerScore && dealerCardScore.size != 2 && playerScore != 2) {
            playerWins()
        } else if (dealerScore < playerScore && dealerCardScore.size != 2 && playerScore == 2) {
            playerWins()
        } else if (dealerScore < playerScore && dealerCardScore.size ==2 && playerScore != 2) {
            playerWins()
        } else if (dealerScore < playerScore && dealerCardScore.size == 2 && playerScore == 2) {
            playerWins()
        }

        }


    fun playerWins() {
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("Congratulations")
        // Display a message on alert dialog
        builder.setMessage("YOU WON" +"\n" + "Your Score was: " + playerScore + "\n"+"The Dealers Score was: " + dealerScore +"\n")


        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when user press the positive button
            if (playerScore == 21 && playerCardScore.size == 2) {
            var blackjacktotal = betTotal * 1.5
                  credit += blackjacktotal.toInt()
                builder.setMessage("BLACKJACK")
                Toast.makeText(applicationContext,"BLACKJACK! You won " + blackjacktotal,Toast.LENGTH_SHORT).show()
                resetField()
        } else {
                credit += betTotal
                Toast.makeText(applicationContext,"You won " + betTotal * 2,Toast.LENGTH_SHORT).show()
                resetField()
        }
    }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }

    fun playerLoses(insurance: Boolean) {
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("Ohhhh")
        // Display a message on alert dialog
        builder.setMessage("YOU LOST" +"\n" + "Your Score was: "+playerScore+ "\n" + "The Dealers Score was: " + dealerScore)

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK"){dialog, which ->
            // Do something when user press the positive button
            if (!insurance) {
            credit -= betTotal
            Toast.makeText(applicationContext,"You lost $betTotal",Toast.LENGTH_SHORT).show()
            resetField()
            } else {
                Toast.makeText(applicationContext,"You lost but you took insurance, so your balance remains unchanged",Toast.LENGTH_SHORT).show()
                resetField()
            }

        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()

    }

    fun playerTie(evenMoney: Boolean) {
        if (evenMoney) {
            val builder = AlertDialog.Builder(this)
            // Set the alert dialog title
            builder.setTitle("Even Money")
            // Display a message on alert dialog
            builder.setMessage("Congratulations! You tied but you have taken Even Money, you doubled your stake")
            builder.setPositiveButton("OK") {dialog, which ->
                Toast.makeText(applicationContext,"You get your credit + 3x " +insurancemoney + " back",Toast.LENGTH_SHORT).show()
                resetField()
            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()

        } else {
            val builder = AlertDialog.Builder(this)
            // Set the alert dialog title
            builder.setTitle("OKAY")
            // Display a message on alert dialog
            if (playerScore == 21 && dealerCardScore[0] == 11) {
                builder.setMessage("You tied but you didnt take Even Money!")
            } else {
                builder.setMessage("You tied!" + "\n" + "Your Score was: " + playerScore + "\n" + "The Dealers Score was: " + dealerScore)
            }
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("OK") { dialog, which ->
                // Do something when user press the positive button
                Toast.makeText(applicationContext, "You get your credit back", Toast.LENGTH_SHORT).show()
                resetField()
            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()
        }

    }


}

