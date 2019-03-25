package com.example.nigl2.a2dblackjack

import android.content.Context

class MyPreference(context: Context){
    val PREFERENCE_NAME = "SharedPreferenceStat"
    val PREFERENCE_WIN_COUNT = "WinCount"
    val PREFERENCE_LOSE_COUNT = "LoseCount"
    val PREFERENCE_TIE_COUNT = "TieCount"
    val PREFERENCE_CREDITS_WON = "CreditsPlusCount"
    val PREFERENCE_CREDITS_LOST = "CreditsMinusCount"
    val PREFERENCE_BLACKJACKS = "BlackjacksCount"
    val PREFERENCE_MOST_CREDITS_POSSESD = "MostCreditCount"
    val PREFERENCE_BUSTED_COUNT = "BustedCount"
    val PREFERENCE_CREDITS = "Credits"

    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getWinCount() : Int {
        return preference.getInt(PREFERENCE_WIN_COUNT, 0)
    }

    fun setWinCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_WIN_COUNT,count)
        editor.apply()
    }

    fun getLoseCount() : Int {
        return preference.getInt(PREFERENCE_LOSE_COUNT, 0)
    }

    fun setLoseCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_LOSE_COUNT,count)
        editor.apply()
    }

    fun getTieCount() : Int {
        return preference.getInt(PREFERENCE_TIE_COUNT, 0)
    }

    fun setTieCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_TIE_COUNT,count)
        editor.apply()
    }

    fun getCreditsWon() : Int {
        return preference.getInt(PREFERENCE_CREDITS_WON, 0)
    }

    fun setCreditsWon(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CREDITS_WON,count)
        editor.apply()
    }

    fun getCreditsLost() : Int {
        return preference.getInt(PREFERENCE_CREDITS_LOST, 0)
    }

    fun setCreditsLost(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CREDITS_LOST,count)
        editor.apply()
    }

    fun getBlackJackCount() : Int {
        return preference.getInt(PREFERENCE_BLACKJACKS, 0)
    }

    fun setBlackJackCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_BLACKJACKS,count)
        editor.apply()
    }


    fun getMostCredits() : Int {
        return preference.getInt(PREFERENCE_MOST_CREDITS_POSSESD, 0)
    }

    fun setMostCredits(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_MOST_CREDITS_POSSESD,count)
        editor.apply()
    }

    fun getBustedCount() : Int {
        return preference.getInt(PREFERENCE_BUSTED_COUNT, 0)
    }

    fun setBustedCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_BUSTED_COUNT,count)
        editor.apply()
    }

    fun getCredits() : Int {
        return preference.getInt(PREFERENCE_CREDITS, 5000)
    }

    fun setCredits(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CREDITS,count)
        editor.apply()
    }


}