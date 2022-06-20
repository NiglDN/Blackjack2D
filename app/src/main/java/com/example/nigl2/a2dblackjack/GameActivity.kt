package com.example.nigl2.a2dblackjack

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_game.*

/**
 *
 * GameActivity: Holds logic and View for the actual Game
 */
class GameActivity : AppCompatActivity(), RewardedVideoAdListener {

    /**
     * overwrites default implementation to immediately reload ad again so its cached while user plays
     */
    override fun onRewardedVideoAdClosed() {
        Log.i("GameActivity","Video closed")
        loadRewardedVideoAd()

    }

    /**
     * overwrites default implementation
     *
     */
    override fun onRewardedVideoAdLeftApplication() {
        Log.i("GameActivity","video left application")
    }

    /**
     * overwrites default implementation
     */
    override fun onRewardedVideoAdLoaded() {
        Log.i("GameActivity","Video loaded")

    }

    override fun onRewardedVideoAdOpened() {
        Log.i("GameActivity","Video opened")

    }

    /**
     *
     * overwrites default implementation to reward user once ad video is completed
     */
    override fun onRewardedVideoCompleted() {
        val myPreference = MyPreference(this)
        myPreference.setCreditsWon(myPreference.getCreditsLost() + 1000)
        myPreference.setCredits(myPreference.getCredits() + 1000)
        Log.i("GameActivity","reward incoming video completed")
        Toast.makeText(this, "1000 Credits gutgeschrieben", Toast.LENGTH_SHORT).show()
    }

    /**
     * overwrites default implementation
     */
    override fun onRewarded(p0: RewardItem?) {
        Log.i("GameActivity","reward applied")

    }

    /**
     * overwrites default implementation
     */
    override fun onRewardedVideoStarted() {
        Log.i("GameActivity"," Video started")

    }
    /**
     *  overwrites default implementation
     */
    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.i("GameActivity","Video failed to load")

    }

    /**
     * instance of rewardVideoad
     */
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    /**
     * credits of player
     */
    private var credit = 0                              // credits of player
    /**
     * credits which are currently deposited in current game
     */
    private var betTotal = 0                            // credits the player deposited for the game
    /**
     * list of all cards in players hand
     */
    private var playerCardScore = mutableListOf<Int>()  // list of all the cards in players hand
    /**
     * list of all cards in dealers hand
     */
    private var dealerCardScore = mutableListOf<Int>()  // list of all the cards in dealers hand
    /**
     * score of players current hand
     */
    private var playerScore = 0                         // score of the players current hand
    /**
     * score of dealers current hand
     */
    private var dealerScore = 0                         // score of the dealers current hand
    /**
     * number of aces in players hand
     */
    private var playerAssHands = 0                      // number of player ass
    /**
     * number of aces in dealers hand
     */
    private var dealerAssHands = 0                      // number of player ass
    /**
     * variable for insurancemoney when insurance is taken (int)
     */
    private var insurancemoney = 0                      // insurance money
    /**
     * boolean which is set after bet is valid
     */
    private var afterBet = false

    //split cards

    /**
     * list of all players left hand cards when split
     */
    private var splitLeft = mutableListOf<Int>()
    /**
     * list of all players right hand cards when split
     */
    private var splitRight = mutableListOf<Int>()
    /**
     * score of players split left hand
     */
    private var splitScoreLeft = 0
    /**
     * score of players split right hand
     */
    private var splitScoreRight = 0
    /**
     * value of first card in players original hand
     */
    private var firstCardsInfo = 0
    /**
     * value of second card in players original hand
     */
    private var secondCardsInfo = 0
    /**
     * boolean which is set if player decides to split his hand
     *
     */
    private var splitMode = false
    /**
     * boolean which defines which hand is currently being handled
     */
    private var leftMode = true

    /**
     * overwrites activities default implementation
     */
    override fun onPause() {
        super.onPause()
        mRewardedVideoAd.pause(this)
    }

    /**
     * overwrites activities default implementation
     */
    override fun onResume() {
        super.onResume()
        if(!afterBet){
            resetField()
        }
        mRewardedVideoAd.resume(this)
    }

    /**
     * overwrites activities default implementation
     */
    override fun onDestroy() {
        super.onDestroy()
        mRewardedVideoAd.destroy(this)
    }

    /**
     * overwrites activities default implementation
     * sets listeners, activates function of floating buttons
     * handles betting and the first 3 dealt cards (2 player / 1 dealer)
     */
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

        floatingActionButton_StrategyTable.setOnClickListener {
            val image = ImageView(this)

            image.setImageResource(R.drawable.basis2)
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            val dialog: AlertDialog = builder.create()
            image.setOnClickListener(){
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_basicstrat)
            dialog.setView(image)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }

        floatingActionButton_Help.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://de.blackjack.org/blackjack-regeln/")))
        }

        button_playfield_deal.setOnClickListener {
            //checks if you need credits
            if (credit == 0 && betTotal == 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("out of money")
                builder.setMessage("You ran out of Credits! \nWatch a short advertisment clip to recieve 1000 Credits")
                builder.setPositiveButton("Watch ad") { dialog, which ->
                    if (mRewardedVideoAd.isLoaded) {
                        mRewardedVideoAd.show()
                    } else {
                        Toast.makeText(this, "No ad available", Toast.LENGTH_SHORT).show()
                    }

                }
                builder.setNegativeButton("No") { dialog, which -> }
                builder.setCancelable(false)
                val dialog: AlertDialog = builder.create()
                dialog.setOnShowListener {
                }
                dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
                dialog.show()
            }
            if (betTotal == 0)
                return@setOnClickListener

            button_playfield_stand.isClickable = true
            button_playfield_stand.visibility = View.VISIBLE

            button_playfield_next.isClickable = true
            button_playfield_next.visibility = View.VISIBLE

            button_playfield_deal.isClickable = false
            button_playfield_deal.visibility = View.INVISIBLE

            textView_playerfield_dealerScore.visibility = View.VISIBLE

            textView_playerfield_playerScore.visibility = View.VISIBLE

            if(betTotal <= credit) {
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


            firstCardsInfo = SingletonCards.cardDeckList[0].first
            playerDraws()
            secondCardsInfo = SingletonCards.cardDeckList[0].first
            playerDraws()
            dealerDraws()

            if (playerCardScore[0] == playerCardScore[1] && betTotal <= credit){
                button_playfield_split.isClickable = true
                button_playfield_split.visibility = View.VISIBLE
            }

            //blackjack
            checkBlackjack()


        }

        button_playfield_next.setOnClickListener {
            if (splitMode){
                if (leftMode){
                    playerDrawsSplitLeft()
                    //checks if you busted
                    if (splitScoreLeft > 21) {
                        playerAssHands = 0
                        if (splitScoreRight == 11)
                            playerAssHands = 1
                        textView_playerfield_loseWinCondition.text = "Play with right hand"
                        textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                        Handler().postDelayed({
                            textView_playerfield_loseWinCondition.text = ""
                        }, 2000)
                        Toast.makeText(this, "Left hand busted!", Toast.LENGTH_SHORT).show()
                        myPreference.setBustedCount(myPreference.getBustedCount() + 1)
                        leftMode = false
                    }
                } else {
                    playerDrawsSplitRight()
                    //checks if you busted
                    if (splitScoreRight > 21) {
                        Toast.makeText(this, "Right hand busted!", Toast.LENGTH_SHORT).show()
                        myPreference.setBustedCount(myPreference.getBustedCount() + 1)
                        leftMode = true
                        dealerPlays(splitScoreLeft,splitLeft)
                    }
                }
            } else {
                playerDraws()
                button_playfield_doubled.isClickable = false
                button_playfield_doubled.visibility = View.INVISIBLE

                button_playfield_split.isClickable = false
                button_playfield_split.visibility = View.INVISIBLE

                //checks if you busted
                if (playerScore > 21) {
                    playerLoses()
                    myPreference.setBustedCount(myPreference.getBustedCount() + 1)
                }
            }
        }

        button_playfield_stand.setOnClickListener {
            if (splitMode){
                if (leftMode){
                    playerAssHands = 0
                    if (splitScoreRight == 11)
                        playerAssHands = 1
                    textView_playerfield_loseWinCondition.text = "Play with right hand"
                    textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                    Handler().postDelayed({
                        textView_playerfield_loseWinCondition.text = ""
                    }, 2000)
                    leftMode = false
                } else {
                    leftMode = true
                    dealerPlays(splitScoreLeft, splitLeft)
                }
            } else {
                button_playfield_stand.isClickable = false
                button_playfield_next.isClickable = false
                button_playfield_doubled.isClickable = false
                dealerPlays(playerScore,playerCardScore)
            }
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
                dealerPlays(playerScore,playerCardScore)
            }
        }

        button_playfield_split.setOnClickListener {
            button_playfield_split.isClickable = false
            button_playfield_split.visibility = View.INVISIBLE
            button_playfield_doubled.isClickable = false
            button_playfield_doubled.visibility = View.INVISIBLE
            textView_playfield_betTotal.text = (betTotal * 2).toString()
            myPreference.setCredits(myPreference.getCredits() - betTotal)
            credit -= betTotal
            textView_playfield_currentBalance.text = (myPreference.getCredits() - betTotal).toString() + " $"
            Toast.makeText(this, "Split for $betTotal Credits", Toast.LENGTH_SHORT).show()
            split()
        }
    }

    /**
     * sets view accordingly when player decides to split
     */
    private fun split() {
        textView_playerfield_loseWinCondition.text = "Play with left hand"
        textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
        Handler().postDelayed({
            textView_playerfield_loseWinCondition.text = ""
        }, 2000)
        //disable normal field and enable split field
        scrollview_playfield_player.visibility = View.INVISIBLE
        scrollview_playfield_player.isClickable = false

        horizontalScrollView_playerfield_splitRight.visibility = View.VISIBLE
        horizontalScrollView_playerfield_splitRight.isClickable = true

        horizontalScrollView_playerfield_splitLeft.visibility = View.VISIBLE
        horizontalScrollView_playerfield_splitLeft.isClickable = true

        textView_playerfield_splitLeftScore.visibility = View.VISIBLE

        //start spliting cards
        SingletonCards.cardDeckList[0] = Pair(firstCardsInfo, playerCardScore[0])
        SingletonCards.cardDeckList[1] = Pair(secondCardsInfo, playerCardScore[1])

        playerScore = 0
        splitMode = true
        leftMode = true

        playerDrawsSplitLeft()
        playerDrawsSplitRight()

        playerAssHands = 0
        if (splitScoreLeft == 11)
            playerAssHands = 1
    }

    /**
     * handles the different betting amounts assigned to the betting buttons
     */
    private fun bettingCredits(bet: Int) {
        if ((credit - bet) < 0) {
            Toast.makeText(this, "not enough Credits", Toast.LENGTH_SHORT).show()
            return
        }
        betTotal += bet
        credit -= bet
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_betTotal.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom))
        textView_playfield_currentBalance.text = "$credit $"
    }


    /**
     * loads an ad with default ad-mob id
     */
    private fun loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build())
    }

    /**
     * is called when the dealer draws and handles drawing and displaying new dealers hand
     */
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
        var draw = drawCard()
        textView_playerfield_dealerScore.text = dealerScore.toString()
        linearlayout_playfield_dealer.addView(draw)
        draw.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom))
        scrollview_playfield_dealer.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    /**
     * is called when player draws, handles players drawing and displaying players new hand
     */
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
        var draw = drawCard()
        linearlayout_playfield_player.addView(draw)
        draw.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom))

        scrollview_playfield_player.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    /**
     * is called when player draws when he decided to split (left hand)
     * sets view accordingly
     */
    private fun playerDrawsSplitLeft() {
        splitLeft.add(SingletonCards.cardDeckList[0].second)
        splitScoreLeft += splitLeft[splitLeft.lastIndex]
        // checks if hand gets hard
        if (splitLeft[splitLeft.lastIndex] == 11)
            playerAssHands++
        if (splitScoreLeft > 21 && playerAssHands != 0) {
            splitScoreLeft -= 10
            playerAssHands--
        }
        textView_playerfield_splitLeftScore.text = splitScoreLeft.toString()
        var draw = drawCard()
        linearlayout_playfield_splitLeft.addView(draw)
        draw.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom))
        horizontalScrollView_playerfield_splitLeft.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    /**
     * is called when player draws when he decided to split (right hand)
     * sets view accordingly
     */
    private fun playerDrawsSplitRight() {
        splitRight.add(SingletonCards.cardDeckList[0].second)
        splitScoreRight += splitRight[splitRight.lastIndex]
        // checks if hand gets hard
        if (splitRight[splitRight.lastIndex] == 11)
            playerAssHands++
        if (splitScoreRight > 21 && playerAssHands != 0) {
            splitScoreRight -= 10
            playerAssHands--
        }
        textView_playerfield_playerScore.text = splitScoreRight.toString()
        var draw = drawCard()
        linearlayout_playfield_splitRight.addView(draw)
        draw.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom))

        horizontalScrollView_playerfield_splitRight.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    /**
     * helper function for drawing the card, sets card and its attributes accordingly in layout
     */
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

    /**
     * resets whole playing field and underlying variables
     */
    private fun resetField(){
        val myPreference = MyPreference(this)
        //hide buttons
        button_playfield_stand.isClickable = false
        button_playfield_stand.visibility = View.INVISIBLE

        button_playfield_next.isClickable = false
        button_playfield_next.visibility = View.INVISIBLE

        button_playfield_doubled.isClickable = false
        button_playfield_doubled.visibility = View.INVISIBLE

        button_playfield_split.isClickable = false
        button_playfield_split.visibility = View.INVISIBLE

        scrollview_playfield_player.isClickable = true
        scrollview_playfield_player.visibility = View.VISIBLE

        horizontalScrollView_playerfield_splitLeft.isClickable = false
        horizontalScrollView_playerfield_splitLeft.visibility = View.INVISIBLE

        horizontalScrollView_playerfield_splitRight.isClickable = false
        horizontalScrollView_playerfield_splitRight.visibility = View.INVISIBLE

        textView_playerfield_splitLeftScore.visibility = View.INVISIBLE

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
        linearlayout_playfield_splitLeft.removeAllViews()
        linearlayout_playfield_splitRight.removeAllViews()
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
        splitLeft.clear()
        splitRight.clear()
        splitScoreLeft = 0
        splitScoreRight = 0
        firstCardsInfo = 0
        secondCardsInfo = 0
        splitMode = false
        leftMode = true
        textView_playfield_currentBalance.text = "$credit $"
        textView_playfield_currentBalance.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
        textView_playfield_betTotal.text = betTotal.toString()
        textView_playfield_betTotal.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
        textView_playerfield_loseWinCondition.text = ""

        //checks if you need credits
        if (credit == 0){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("out of money")
            builder.setMessage("You ran out of Credits! \nWatch a short advertisment clip to recieve 1000 Credits")
            builder.setPositiveButton("Watch ad"){dialog, which ->
                if (mRewardedVideoAd.isLoaded) {
                    mRewardedVideoAd.show()
                } else {


                    Toast.makeText(this, "No ad available", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No"){dialog, which -> }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
            dialog.show()
        }
    }


    /**
     * is called when its dealers turn to draw cards, handles dealers "must draw to 16, must stand on 17"
     */
    private fun dealerPlays(score : Int, list : MutableList<Int>){
        var drawAgain = true
        if (dealerScore > 16) {
            drawAgain = false
            checkLoseCondition(score,list)
        }
        if (drawAgain){
            Handler().postDelayed({
                dealerPlays(score,list)
            },750)
            dealerDraws()
        }
    }

    /**
     * checks if player has lost his current game and invokes the fitting method according to the outcome of the game
     */
    private fun checkLoseCondition(score : Int, list : MutableList<Int>){
        if (score > 21){
            playerLoses()
        }
        else if (dealerScore > 21)
            playerWins(2.0)
        else if (dealerScore == 21 && dealerCardScore.size == 2 && list.size > 2)
            playerLoses()
        else if (dealerScore == 21 && dealerCardScore.size == 2 && list.size == 2 && dealerScore == score)
            playerTie()
        else if (dealerScore == 21 && dealerCardScore.size > 2 && playerCardScore.size > 2 && dealerScore == score)
            playerWins(2.0)
        else if(score == dealerScore)
            playerTie()
        else if(score > dealerScore)
            playerWins(2.0)
        else if(score < dealerScore)
            playerLoses()
    }

    /**
     * handles player-winning event
     * @param factor how much players bet gets multiplied (difference between blackjack and "normal" win)
     */
    private fun playerWins(factor: Double) {
        var multiply = factor
        button_playfield_stand.isClickable = false
        button_playfield_next.isClickable = false
        button_playfield_doubled.isClickable = false


        val myPreference = MyPreference(this)

        if (splitMode){
            if (leftMode){
                textView_playerfield_loseWinCondition.text = "Left Hand wins"
                textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                Handler().postDelayed({
                    myPreference.setCreditsWon(myPreference.getCreditsWon() + betTotal-insurancemoney)
                    credit += ((betTotal-insurancemoney) * multiply).toInt()
                    myPreference.setCredits(credit)
                    myPreference.setWinCount(myPreference.getWinCount() + 1)
                    if (myPreference.getMostCredits() > betTotal*2)
                        myPreference.setMostCredits(betTotal*2)

                    Toast.makeText(applicationContext,"(Left Hand) You win " + (betTotal* factor).toInt() + " $",Toast.LENGTH_SHORT).show()
                    leftMode = false
                    checkLoseCondition(splitScoreRight,splitRight)
                },2000)
            } else {
                textView_playerfield_loseWinCondition.text = "Right hand wins"
                textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                Handler().postDelayed({

                    myPreference.setCreditsWon(myPreference.getCreditsWon() + betTotal-insurancemoney)
                    credit += ((betTotal-insurancemoney)  * multiply).toInt()
                    myPreference.setCredits(credit)
                    myPreference.setWinCount(myPreference.getWinCount() + 1)
                    if (myPreference.getMostCredits() < betTotal*2)
                        myPreference.setMostCredits(betTotal*2)

                    Toast.makeText(applicationContext,"(Right Hand) You win " + (betTotal * factor).toInt() + " $",Toast.LENGTH_SHORT).show()
                    resetField()
                },2000)
            }
        } else {
            Handler().postDelayed({
                textView_playerfield_loseWinCondition.text = "You won"

            if(playerScore == 21 && playerCardScore.size == 2) {
                textView_playerfield_loseWinCondition.text = "BLACKJACK!"
                myPreference.setBlackJackCount(myPreference.getBlackJackCount() + 1)
                multiply = 2.5
            }}, 500)
            textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
            Handler().postDelayed({
                var difwin = betTotal-insurancemoney

                betTotal = ((betTotal-insurancemoney) * multiply).toInt()
                credit += betTotal
                credit -=insurancemoney
                myPreference.setCredits(credit)
                myPreference.setCreditsWon(myPreference.getCreditsWon() + betTotal - difwin)
                myPreference.setWinCount(myPreference.getWinCount() + 1)
                if (myPreference.getMostCredits() < (betTotal-difwin))
                    myPreference.setMostCredits((betTotal-difwin))

                Toast.makeText(applicationContext,"You win " +(betTotal-difwin) +" $",Toast.LENGTH_SHORT).show()
                resetField()
            },2000)
        }
    }

    /**
     * handles player-losing event
     */
    private fun playerLoses() {
        button_playfield_stand.isClickable = false
        button_playfield_next.isClickable = false
        button_playfield_doubled.isClickable = false
        val myPreference = MyPreference(this)

        if (splitMode){
            if (leftMode){
                textView_playerfield_loseWinCondition.text = "Left hand loses"
                textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                Handler().postDelayed({
                    myPreference.setCredits(credit)
                    myPreference.setLoseCount(myPreference.getLoseCount() + 1)
                    myPreference.setCreditsLost(myPreference.getCreditsLost() + betTotal)
                    if (!(dealerScore == 21 && dealerCardScore.size == 2 && dealerCardScore[0] == 11)) {
                        myPreference.setCreditsLost(myPreference.getCreditsLost() + insurancemoney)
                    }

                    Toast.makeText(applicationContext,"You lose (left hand) $betTotal $",Toast.LENGTH_SHORT).show()
                    leftMode = false
                    checkLoseCondition(splitScoreRight,splitRight)
                },2000)
            } else {
                textView_playerfield_loseWinCondition.text = "Right hand loses"
                textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                Handler().postDelayed({
                    myPreference.setCredits(credit)
                    myPreference.setLoseCount(myPreference.getLoseCount() + 1)
                    myPreference.setCreditsLost(myPreference.getCreditsLost() + betTotal)


                    Toast.makeText(applicationContext,"You lose (right hand) $betTotal $",Toast.LENGTH_SHORT).show()
                    resetField()
                },2000)
            }
        } else{
            textView_playerfield_loseWinCondition.text = "You lost"
            textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
            Handler().postDelayed({

                myPreference.setLoseCount(myPreference.getLoseCount() + 1)
                myPreference.setCreditsLost(myPreference.getCreditsLost() + betTotal + insurancemoney)


                if (dealerScore == 21 && dealerCardScore.size == 2 && dealerCardScore[0] == 11 && insurancemoney > 0){
                    credit += (insurancemoney * 3)

                    myPreference.setCreditsLost(myPreference.getCreditsLost() - (insurancemoney*3))
                    Toast.makeText(applicationContext,"Loss is 0 because of insurance",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext,"You lose "+(insurancemoney+betTotal) +" $",Toast.LENGTH_SHORT).show()

                }
                myPreference.setCredits(credit)
                resetField()
            },2000)
        }
    }

    /**
     * handles player-tieing event
     */
    private fun playerTie() {
        button_playfield_stand.isClickable = false
        button_playfield_next.isClickable = false
        button_playfield_doubled.isClickable = false
        val myPreference = MyPreference(this)
        myPreference.setCreditsLost(myPreference.getCreditsLost() + insurancemoney)
        if (splitMode){
            if(leftMode){
                textView_playerfield_loseWinCondition.text = "Left Hand ties"
                textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                Handler().postDelayed({

                    myPreference.setTieCount(myPreference.getTieCount() + 1)
                    credit+=betTotal

                    myPreference.setCredits(credit)
                    Toast.makeText(applicationContext, "(Left Hand) You get your betting stake back", Toast.LENGTH_SHORT).show()
                    leftMode = false
                    checkLoseCondition(splitScoreRight,splitRight)
                },2000)
            } else {
                textView_playerfield_loseWinCondition.text = "Right Hand ties"
                textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
                Handler().postDelayed({

                    myPreference.setTieCount(myPreference.getTieCount() + 1)
                    credit+=betTotal

                    myPreference.setCredits(credit)
                    Toast.makeText(applicationContext, "(Right Hand) You get your betting stake back", Toast.LENGTH_SHORT).show()
                    resetField()
                },2000)
            }
        } else {

            textView_playerfield_loseWinCondition.text = "Tie"
            textView_playerfield_loseWinCondition.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))

            Handler().postDelayed({


                myPreference.setCredits(credit+betTotal)
                myPreference.setTieCount(myPreference.getTieCount() + 1)
                Toast.makeText(applicationContext, "you get your betting stake back", Toast.LENGTH_SHORT).show()
                resetField()
            },2000)
        }
    }

    /**
     * is called to check for blackjack and insurance situation
     */
    private fun checkBlackjack(){
        val myPreference = MyPreference(this)
        // Blackjack
        if (dealerScore < 10 && playerScore == 21) {
            playerWins(2.5)
        }
        //checks even money
        else if (dealerScore == 11 && playerScore == 21) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Even Money?")
            builder.setMessage("Dealer has an Ace and you have a Blackjack, do you want to recieve 2x your betting stake instantly without risk (instead of playing for an Blackjack or Tie)?")
            builder.setPositiveButton("OK") { dialog, which ->
                credit += (betTotal * 2)
                Toast.makeText(applicationContext, "You have won " + betTotal * 2 + "  $", Toast.LENGTH_SHORT).show()
                textView_playfield_betTotal.text = betTotal.toString()
                myPreference.setCredits(credit)
                resetField()
            }
            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(applicationContext, "Declined Even Money", Toast.LENGTH_SHORT).show()
                textView_playfield_betTotal.text = betTotal.toString()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
            dialog.show()
            myPreference.setBlackJackCount(myPreference.getBlackJackCount() + 1)
        }
        // Insurance
        else if (dealerScore == 11 && playerScore < 21 && credit >= betTotal/2) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Insurance?")
            builder.setMessage("Dealer has an Ace, Do you want to insure your hand against an Blackjack of the Dealer? \n(If Dealer has an Blackjack, your total loss is 0!) \nCost: " + betTotal / 2)
            builder.setPositiveButton("OK") { dialog, which ->
                insurancemoney = betTotal / 2
                credit -= insurancemoney
                Toast.makeText(applicationContext, "Insured for $insurancemoney", Toast.LENGTH_SHORT).show()
                textView_playfield_currentBalance.text = "$credit $"
            }
            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(applicationContext, "Declined Insurance", Toast.LENGTH_SHORT).show()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
            dialog.show()
        }
    }
}
