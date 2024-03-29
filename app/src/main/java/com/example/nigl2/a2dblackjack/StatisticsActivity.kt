package com.example.nigl2.a2dblackjack

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.activity_statistics.*

/**
 * Class which displays piechart and detailed statistics of players current session
 * contains loadStats and resetStats methods
 */
class StatisticsActivity : AppCompatActivity() {
    /**
     * overwrites default implementation of onCreate
     * sets listeners
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        loadStats()
        button_stats_back.setOnClickListener {
            finish()
        }

        button_stats_reset.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Reset stats?")
            builder.setMessage("Would you like to reset all statistics? (You start with 5000 Credits again)")
            builder.setPositiveButton("OK"){dialog, which ->
                resetStats()
                loadStats()
                Toast.makeText(applicationContext,"Reset everything successfully!",Toast.LENGTH_SHORT).show()
            }
            builder.setNeutralButton("Abort"){dialog, which ->
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
            dialog.show()
        }
    }

    /**
     * loads the players current stats and displays them in the chart and list of stats
     */
    fun loadStats(){
        val myPreference = MyPreference(this)

        val wins = myPreference.getWinCount()
        val loses = myPreference.getLoseCount()
        val ties = myPreference.getTieCount()
        // PieChart
            val entries = ArrayList<PieEntry>()
            val colors = ArrayList<Int>()
            if (wins != 0){
                colors.add(Color.parseColor("#3ADF00"))
                entries.add(PieEntry(wins.toFloat(), "Wins"))
            }
            if (loses != 0){
                colors.add(Color.parseColor("#FF0000"))
                entries.add(PieEntry(loses.toFloat(), "Losses"))
            }
            if (ties != 0){
                colors.add(Color.parseColor("#6E6E6E"))
                entries.add(PieEntry(ties.toFloat(), "Ties"))
            }
            var pieData = PieDataSet(entries, "")
            pieData.setColors(colors)
            pieData.valueTextSize = 12f
            pieData.valueTextColor = Color.WHITE
            pieData.valueFormatter = PercentFormatter(chart)
            chart.data = PieData(pieData)
            chart.setDrawHoleEnabled(true);
            chart.getDescription().setEnabled(false);
            chart.setDragDecelerationFrictionCoef(0.95f);
            chart.setRotationEnabled(true);
            chart.setUsePercentValues(true)
            chart.setHighlightPerTapEnabled(true);
            chart.setEntryLabelColor(Color.WHITE);
            chart.setEntryLabelTextSize(15f);
            chart.setHoleColor(Color.TRANSPARENT)
            chart.legend.isEnabled = false
            chart.setDragDecelerationFrictionCoef(0.95f);

        if ((wins + loses + ties) == 0){
            chart.centerText = "No games played!"
            chart.setTransparentCircleAlpha(0)
            chart.setCenterTextColor(Color.WHITE)
            chart.setCenterTextSize(16f)
        }
        pieData.notifyDataSetChanged()
        chart.notifyDataSetChanged()
        chart.invalidate()
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

    /**
     * resets all stats to default
     */
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
