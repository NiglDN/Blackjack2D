package com.example.nigl2.a2dblackjack

import android.content.Context

/**
 * Preferemce cards to store certain values for usage in different activites
 */
class MyPreference(context: Context){
    /**
     * variable how the SharedPreferences-Object is called
     */
    val PREFERENCE_NAME = "SharedPreferenceStat"
    /**
     * SharedPreference for WinCount
     */
    val PREFERENCE_WIN_COUNT = "WinCount"
    /**
     * SharedPreference for LoseCount
     */
    val PREFERENCE_LOSE_COUNT = "LoseCount"
    /**
     * SharedPreference for TieCount
     */
    val PREFERENCE_TIE_COUNT = "TieCount"
    /**
     * SharedPreference for counting won credits
     */
    val PREFERENCE_CREDITS_WON = "CreditsPlusCount"
    /**
     * SharedPreference for counting lost credits
     */
    val PREFERENCE_CREDITS_LOST = "CreditsMinusCount"
    /**
     * SharedPreference to count Blackjacks
     */
    val PREFERENCE_BLACKJACKS = "BlackjacksCount"
    /**
     * SharedPreference to count most credits possessed
     */
    val PREFERENCE_MOST_CREDITS_POSSESD = "MostCreditCount"
    /**
     * SharedPreference to count how many Times player busted
     */
    val PREFERENCE_BUSTED_COUNT = "BustedCount"
    /**
     * SharedPreference to display current players credits
     */
    val PREFERENCE_CREDITS = "Credits"
    /**
     * defines Context mode of SharedPreferences
     */
    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    /**
     * Getter method for win count
     * @return current amount of playerwins
     */
    fun getWinCount() : Int {
        return preference.getInt(PREFERENCE_WIN_COUNT, 0)
    }

    /**
     * Setter method for win count
     * @param current amount of playerwins
     */
    fun setWinCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_WIN_COUNT,count)
        editor.apply()
    }

    /**
     * Getter method for loss count
     * @return current amount of playerlosses
     */
    fun getLoseCount() : Int {
        return preference.getInt(PREFERENCE_LOSE_COUNT, 0)
    }

    /**
     * Setter method for loss count
     * @param current amount of playerlosses
     */
    fun setLoseCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_LOSE_COUNT,count)
        editor.apply()
    }

    /**
     * Getter method for Tie count
     * @return current amount of playerties
     */
    fun getTieCount() : Int {
        return preference.getInt(PREFERENCE_TIE_COUNT, 0)
    }

    /**
     * Setter method for Tie count
     * @param current amount of playerties
     */
    fun setTieCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_TIE_COUNT,count)
        editor.apply()
    }

    /**
     * Getter method for credits won
     * @return amount of won credits
     */
    fun getCreditsWon() : Int {
        return preference.getInt(PREFERENCE_CREDITS_WON, 0)
    }

    /**
     * Setter method for credits won
     */
    fun setCreditsWon(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CREDITS_WON,count)
        editor.apply()
    }

    /**
     * Getter method for amount of credits lost by player
     * @return total amount of playercredits loss
     */
    fun getCreditsLost() : Int {
        return preference.getInt(PREFERENCE_CREDITS_LOST, 0)
    }

    /**
     * Setter method for amount of credits lost by player
     * @param new amount of total lost credits
     */
    fun setCreditsLost(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CREDITS_LOST,count)
        editor.apply()
    }

    /**
     * Getter method for total amount of Blackjacks
     * @return current amount of Player Blackjacks
     */
    fun getBlackJackCount() : Int {
        return preference.getInt(PREFERENCE_BLACKJACKS, 0)
    }

    /**
     * Setter method for total amount of Blackjacks
     * @param new amount of player Blackjacks
     */
    fun setBlackJackCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_BLACKJACKS,count)
        editor.apply()
    }

    /**
     * Getter method for most credits possessed
     * @return most credits possesed
     */
    fun getMostCredits() : Int {
        return preference.getInt(PREFERENCE_MOST_CREDITS_POSSESD, 0)
    }

    /**
     * Setter method for most credits possessed
     * @param most credits possessed
     */
    fun setMostCredits(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_MOST_CREDITS_POSSESD,count)
        editor.apply()
    }

    /**
     * Getter method for count of player busts
     * @return count of player busts
     */
    fun getBustedCount() : Int {
        return preference.getInt(PREFERENCE_BUSTED_COUNT, 0)
    }

    /**
     * Setter method for count of player busts
     * @param count of player busts
     */
    fun setBustedCount(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_BUSTED_COUNT,count)
        editor.apply()
    }

    /**
     * Getter method for current amount of credits
     * @return current amount of credits
     */
    fun getCredits() : Int {
        return preference.getInt(PREFERENCE_CREDITS, 5000)
    }

    /**
     * Setter method for current amount of credits
     * @param current amount of credits
     */
    fun setCredits(count:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CREDITS,count)
        editor.apply()
    }


}