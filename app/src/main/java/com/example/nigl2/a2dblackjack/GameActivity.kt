package com.example.nigl2.a2dblackjack

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity(), RewardedVideoAdListener {
    override fun onRewardedVideoAdClosed() {
        Log.i("GameActivity","Video closed")
        loadRewardedVideoAd()

    }

    override fun onRewardedVideoAdLeftApplication() {
        Log.i("GameActivity","video left application")

    }

    override fun onRewardedVideoAdLoaded() {
        Log.i("GameActivity","Video loaded")

    }

    override fun onRewardedVideoAdOpened() {
        Log.i("GameActivity","Video opened")

    }

    override fun onRewardedVideoCompleted() {
        val myPreference = MyPreference(this)
        myPreference.setCredits(myPreference.getCredits() + 1000)
        Log.i("GameActivity","reward incoming video completed")
        Toast.makeText(this, "Dir wurden 1000 Credits gutgeschrieben", Toast.LENGTH_SHORT).show()
    }

    override fun onRewarded(p0: RewardItem?) {
        Log.i("GameActivity","reward applied")

    }

    override fun onRewardedVideoStarted() {
        Log.i("GameActivity"," Video started")

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.i("GameActivity","Video failed to load")

    }

    private lateinit var mRewardedVideoAd: RewardedVideoAd




    private var credit = 0                              // credits of player
    private var betTotal = 0                            // credits the player deposited for the game
    private var playerCardScore = mutableListOf<Int>()  // list of all the cards in players hand
    private var dealerCardScore = mutableListOf<Int>()  // list of all the cards in dealers hand
    private var playerScore = 0                         // score of the players current hand
    private var dealerScore = 0                         // score of the dealers current hand
    private var playerAssHands = 0                      // number of player ass
    private var dealerAssHands = 0                      // number of player ass
    private var insurancemoney = 0                      // insurance money
    private var afterBet = false


    override fun onResume() {
        super.onResume()
        if(!afterBet){
            resetField()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val myPreference = MyPreference(this)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadRewardedVideoAd()



        //betButtons
        button_playfield_bet10.setOnClickListener {
            bettingCredits(10)
        }
        button_playfield_bet50.setOnClickListener {
            bettingCredits(50)
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
            bettingCredits(credit)
        }
        button_playfield_reverse.setOnClickListener {
            if (betTotal == 0)
                finish()
             else
                resetField()
        }

        // actionButtons
        button_playfield_deal.setOnClickListener {
            if (betTotal != 0)
                Toast.makeText(this, "Einsatz akzeptiert", Toast.LENGTH_SHORT).show()
            else
                return@setOnClickListener

            button_playfield_stand.isClickable = true
            button_playfield_stand.visibility = View.VISIBLE

            button_playfield_next.isClickable = true
            button_playfield_next.visibility = View.VISIBLE

            button_playfield_deal.isClickable = false
            button_playfield_deal.visibility = View.INVISIBLE

            textView_playerfield_dealerScore.visibility = View.VISIBLE

            textView_playerfield_playerScore.visibility = View.VISIBLE

            if(betTotal < credit) {
                button_playfield_doubled.isClickable = true
                button_playfield_doubled.visibility = View.VISIBLE
            } else {
                button_playfield_doubled.isClickable = false
                button_playfield_doubled.visibility = View.INVISIBLE
            }

            //buttons for bet disappear
            button_playfield_bet10.isClickable = false
            button_playfield_bet10.visibility = View.INVISIBLE

            button_playfield_bet50.isClickable = false
            button_playfield_bet50.visibility = View.INVISIBLE

            button_playfield_bet100.isClickable = false
            button_playfield_bet100.visibility = View.INVISIBLE

            button_playfield_bet500.isClickable = false
            button_playfield_bet500.visibility = View.INVISIBLE

            button_playfield_bet5000.isClickable = false
            button_playfield_bet5000.visibility = View.INVISIBLE

            button_playfield_betAllIn.isClickable = false
            button_playfield_betAllIn.visibility = View.INVISIBLE

            button_playfield_reverse.isClickable = false

            afterBet = true
            //first turn

            playerDraws()
            playerDraws()
            dealerDraws()

            //blackjack
            checkBlackjack()
        }

        button_playfield_next.setOnClickListener {
            playerDraws()
            button_playfield_doubled.isClickable = false
            button_playfield_doubled.visibility = View.INVISIBLE
            //checks if you busted
            if (playerScore > 21) {
                playerLoses()
                myPreference.setBustedCount(myPreference.getBustedCount() + 1)
            }
        }

        button_playfield_stand.setOnClickListener {
            button_playfield_stand.isClickable = false
            button_playfield_next.isClickable = false
            button_playfield_doubled.isClickable = false

            dealerPlays()
        }

        button_playfield_doubled.setOnClickListener {
            //checks if it is possible with the current credit
            if ((credit) < betTotal)
                return@setOnClickListener
            bettingCredits(betTotal)
            playerDraws()
            //checks if you busted
            if (playerScore > 21){
                playerLoses()
            myPreference.setBustedCount(myPreference.getBustedCount() + 1)
            } else {
                button_playfield_stand.isClickable = false

                button_playfield_next.isClickable = false

                button_playfield_doubled.isClickable = false
                dealerPlays()
            }
        }
    }

    // helper function for credits
    // does not change credit value, just shows and accept with click on deal
    private fun bettingCredits(bet: Int) {
        if ((credit - bet) < 0) {
            Toast.makeText(this, "Nicht genügend Credits", Toast.LENGTH_SHORT).show()
            return
        }
        betTotal += bet
        credit -= bet
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_currentBalance.text = "Credits: $credit"
    }


    //ad function
    private fun loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build())
    }

    //call this when the dealer must draw
    private fun dealerDraws() {
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

    //call this when the player must draw
    private fun playerDraws() {
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

    //helper function for drawing cards
    private fun drawCard() : ImageView {
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

    //resets the whole field
    private fun resetField(){
        val myPreference = MyPreference(this)
        //hide buttons
        button_playfield_stand.isClickable = false
        button_playfield_stand.visibility = View.INVISIBLE

        button_playfield_next.isClickable = false
        button_playfield_next.visibility = View.INVISIBLE

        button_playfield_doubled.isClickable = false
        button_playfield_doubled.visibility = View.INVISIBLE

        textView_playerfield_playerScore.visibility = View.INVISIBLE

        textView_playerfield_dealerScore.visibility = View.INVISIBLE

        button_playfield_deal.isClickable = true
        button_playfield_deal.visibility = View.VISIBLE

        // buttons reappear
        button_playfield_bet10.isClickable = true
        button_playfield_bet10.visibility = View.VISIBLE

        button_playfield_bet50.isClickable = true
        button_playfield_bet50.visibility = View.VISIBLE

        button_playfield_bet100.isClickable = true
        button_playfield_bet100.visibility = View.VISIBLE

        button_playfield_bet500.isClickable = true
        button_playfield_bet500.visibility = View.VISIBLE

        button_playfield_bet5000.isClickable = true
        button_playfield_bet5000.visibility = View.VISIBLE

        button_playfield_betAllIn.isClickable = true
        button_playfield_betAllIn.visibility = View.VISIBLE

        button_playfield_reverse.isClickable = true

        linearlayout_playfield_dealer.removeAllViews()
        linearlayout_playfield_player.removeAllViews()
        SingletonCards.cardDeckList.clear()
        SingletonCards.createDeck()

        credit = myPreference.getCredits()
        betTotal = 0
        playerScore = 0
        dealerScore = 0
        playerAssHands = 0
        dealerAssHands = 0
        playerCardScore.clear()
        dealerCardScore.clear()
        insurancemoney = 0
        afterBet = false

        textView_playfield_currentBalance.text = "Credits:  " + credit.toString()
        textView_playfield_betTotal.text = betTotal.toString()

        //checks if you need credits
        if (credit == 0){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pleite")
            builder.setMessage("Du hast keine Credits mehr! \nDu kannst ein kurzes Werbevideo schauen um Credits zu bekommen")
            builder.setPositiveButton("Werbung ansehen: (1000 Credits)"){dialog, which ->
                if (mRewardedVideoAd.isLoaded) {
                    mRewardedVideoAd.show()
                } else {
                    Toast.makeText(this, "Keine Werbung verfügbar", Toast.LENGTH_SHORT).show()
                }

            }
            builder.setNegativeButton("Nein"){dialog, which -> }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }



    //call this when its the dealers turn
    private fun dealerPlays(){
        var drawAgain = true
        if (dealerScore > 16) {
            drawAgain = false
            checkLoseCondition()
        }
        Handler().postDelayed({
            if(drawAgain){
                dealerPlays()
            }
        },1000)
        if (drawAgain){
            dealerDraws()
        }
    }

    //checks if you lost
    private fun checkLoseCondition(){
        if (dealerScore > 21)
            playerWins(2.0)
        else if (dealerScore == 21 && dealerCardScore.size == 2 && playerCardScore.size > 2)
            playerLoses()
        else if (dealerScore == 21 && dealerCardScore.size == 2 && playerCardScore.size == 2 && dealerScore == playerScore)
            playerTie()
        else if(playerScore == dealerScore)
            playerTie()
        else if(playerScore > dealerScore)
            playerWins(2.0)
        else if(playerScore < dealerScore)
            playerLoses()
    }

    // gives money
    // facotr shows how much you win
    private fun playerWins(factor: Double) {
        button_playfield_stand.isClickable = false
        button_playfield_next.isClickable = false
        button_playfield_doubled.isClickable = false
        Handler().postDelayed({
        val myPreference = MyPreference(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Glückwunsch")
        builder.setMessage("Du hast Gewonnen \nSpieler Hand: $playerScore \nDealer Hand: $dealerScore")
        builder.setPositiveButton("OK") { dialog, which ->
            betTotal = (betTotal * factor).toInt()
            credit += betTotal
            myPreference.setCredits(credit)
            myPreference.setWinCount(myPreference.getWinCount() + 1)
            if (myPreference.getMostCredits() < betTotal)
                myPreference.setMostCredits(betTotal)
            myPreference.setCreditsWon(myPreference.getCreditsWon() + betTotal)
            Toast.makeText(applicationContext,"Du gewinnst $betTotal",Toast.LENGTH_SHORT).show()
            resetField()
         }
        builder.setCancelable(false)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        },500)
    }

    private fun playerLoses() {
        button_playfield_stand.isClickable = false
        button_playfield_next.isClickable = false
        button_playfield_doubled.isClickable = false
        Handler().postDelayed({
        val myPreference = MyPreference(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Schade")
        builder.setMessage("Du hast verloren \nSpieler Hand: $playerScore \nDealer Hand: $dealerScore")
        builder.setPositiveButton("OK"){dialog, which ->
            if (dealerScore == 21 && dealerCardScore.size == 2)
                credit += (insurancemoney * 3)
            myPreference.setCredits(credit)
            myPreference.setLoseCount(myPreference.getLoseCount() + 1)
            myPreference.setCreditsLost(myPreference.getCreditsLost() + betTotal)
            Toast.makeText(applicationContext,"Du verlierst " + betTotal,Toast.LENGTH_SHORT).show()
            resetField()
        }
        builder.setCancelable(false)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        },500)
    }

    private fun playerTie() {
        button_playfield_stand.isClickable = false
        button_playfield_next.isClickable = false
        button_playfield_doubled.isClickable = false
        Handler().postDelayed({
        val myPreference = MyPreference(this)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Unentschieden")
            builder.setMessage("Keiner gewinnt \n Spieler Hand: $playerScore \n Dealer Hand: $dealerScore")
            builder.setPositiveButton("OK") { dialog, which ->
                myPreference.setTieCount(myPreference.getTieCount() + 1)
                Toast.makeText(applicationContext, "Du bekommst deinen Einsatz zurück", Toast.LENGTH_SHORT).show()
                resetField()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        },500)
    }

    //checks for Blackjack and Insurrance
    private fun checkBlackjack(){
        val myPreference = MyPreference(this)
        // Blackjack
        if (dealerScore < 11 && playerScore == 21) {
            myPreference.setBlackJackCount(myPreference.getBlackJackCount() + 1)
            playerWins(2.5)
        }
        //checks even money
        else if (dealerScore == 11 && playerScore == 21) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Even Money?")
            builder.setMessage("Dealer hat ein Ass, willst abbrechen und den doppelten Einsatz zurückbekommen (statt um den x2.5 Gewinn zu spielen)")
            builder.setPositiveButton("OK") { dialog, which ->
                credit += (betTotal * 2)
                Toast.makeText(applicationContext, "Du hast " + betTotal * 2 + " gewonnen", Toast.LENGTH_SHORT).show()
                textView_playfield_betTotal.text = betTotal.toString()
                myPreference.setCredits(credit)
                resetField()
            }
            builder.setNegativeButton("Nein") { dialog, which ->
                Toast.makeText(applicationContext, "Even Money abgelehnt", Toast.LENGTH_SHORT).show()
                textView_playfield_betTotal.text = betTotal.toString()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
            myPreference.setBlackJackCount(myPreference.getBlackJackCount() + 1)
        }
        // Insurance
        else if (dealerScore == 11 && playerScore < 21 && credit >= betTotal/2) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Versicherung?")
            builder.setMessage("Dealer hat ein Ass, wollen Sie ihre Hand gegen einen Blackjack des Gegners versichern? \n(Bei Blackjack des Dealers verlieren Sie so nichts) \nKosten: " + betTotal / 2)
            builder.setPositiveButton("OK") { dialog, which ->
                insurancemoney = betTotal / 2
                credit -= insurancemoney
                Toast.makeText(applicationContext, "Versichert um $insurancemoney", Toast.LENGTH_SHORT).show()
                textView_playfield_currentBalance.text = "Credits: $credit"
            }
            builder.setNegativeButton("Nein") { dialog, which ->
                Toast.makeText(applicationContext, "Versicherung abgelehnt", Toast.LENGTH_SHORT).show()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}

