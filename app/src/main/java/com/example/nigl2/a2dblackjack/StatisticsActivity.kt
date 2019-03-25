package com.example.nigl2.a2dblackjack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_statistics.*
import java.math.RoundingMode
import java.text.DecimalFormat

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        loadStats()



        button_stats_back.setOnClickListener {
            finish()
        }

        button_stats_reset.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Zurücksetzten")
            builder.setMessage("Willst du alles zurücksetzten? (Du fängst wieder mit 5000 Credits an)")
            builder.setPositiveButton("OK"){dialog, which ->
                resetStats()
                loadStats()
                Toast.makeText(applicationContext,"Alles zurückgesetzt",Toast.LENGTH_SHORT).show()
            }
            builder.setNeutralButton("Abbrechen"){dialog, which ->
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    fun loadStats(){
        val myPreference = MyPreference(this)
        val wins = myPreference.getWinCount().toDouble()
        val loses = myPreference.getLoseCount().toDouble()

        if (wins == 0.0)
            textView_stats_winrate.text = "0%"
        else{
            val df = DecimalFormat("#.###")
            df.roundingMode = RoundingMode.CEILING

            textView_stats_winrate.text = df.format(wins/(wins + loses)*100) + "%"

        }


        textView_stats_credits.text = myPreference.getCredits().toString()
        textView_stats_wins.text = wins.toInt().toString()
        textView_stats_loses.text = loses.toInt().toString()
        textView_stats_ties.text = myPreference.getTieCount().toString()
        textView_stats_credits_won.text = myPreference.getCreditsWon().toString()
        textView_stats_credits_lost.text = myPreference.getCreditsLost().toString()
        textView_stats_biggest_win.text = myPreference.getMostCredits().toString()
        textView_stats_blackjacks.text = myPreference.getBlackJackCount().toString()
        textView_stats_busted_count.text = myPreference.getBustedCount().toString()


    }

    fun resetStats(){
        val myPreference = MyPreference(this)
        myPreference.setCredits(5000)
        myPreference.setWinCount(0)
        myPreference.setLoseCount(0)
        myPreference.setTieCount(0)
        myPreference.setCreditsWon(0)
        myPreference.setCreditsLost(0)
        myPreference.setMostCredits(0)
        myPreference.setBlackJackCount(0)
        myPreference.setBustedCount(0)
    }
}
