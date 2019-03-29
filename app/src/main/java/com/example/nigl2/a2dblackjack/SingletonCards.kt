package com.example.nigl2.a2dblackjack

import javax.xml.transform.Source

/**
 * singleton object
 */
object SingletonCards {
    /**
     * cardDecklist as mutable list
     */
    val cardDeckList : MutableList<Pair<Int, Int>> = mutableListOf()

    /**
     * function that fills list with pictures and their corresponding card values
     * afterwards shuffles deck
     */
    fun createDeck(){

        //add all cards
        //hearts
        cardDeckList.add(Pair(R.drawable.heart_two, 2))
        cardDeckList.add(Pair(R.drawable.heart_three, 3))
        cardDeckList.add(Pair(R.drawable.heart_four, 4))
        cardDeckList.add(Pair(R.drawable.heart_five, 5))
        cardDeckList.add(Pair(R.drawable.heart_six, 6))
        cardDeckList.add(Pair(R.drawable.heart_seven, 7))
        cardDeckList.add(Pair(R.drawable.heart_eight, 8))
        cardDeckList.add(Pair(R.drawable.heart_nine, 9))
        cardDeckList.add(Pair(R.drawable.heart_ten, 10))
        cardDeckList.add(Pair(R.drawable.heart_jack, 10))
        cardDeckList.add(Pair(R.drawable.heart_queen, 10))
        cardDeckList.add(Pair(R.drawable.heart_king, 10))
        cardDeckList.add(Pair(R.drawable.heart_ace, 11))

        //diamond
         cardDeckList.add(Pair(R.drawable.diamond_two, 2))
         cardDeckList.add(Pair(R.drawable.diamond_three, 3))
         cardDeckList.add(Pair(R.drawable.diamond_four, 4))
         cardDeckList.add(Pair(R.drawable.diamond_five, 5))
         cardDeckList.add(Pair(R.drawable.diamond_six, 6))
         cardDeckList.add(Pair(R.drawable.diamond_seven, 7))
         cardDeckList.add(Pair(R.drawable.diamond_eight, 8))
         cardDeckList.add(Pair(R.drawable.diamond_nine, 9))
         cardDeckList.add(Pair(R.drawable.diamond_ten, 10))
         cardDeckList.add(Pair(R.drawable.diamond_jack, 10))
         cardDeckList.add(Pair(R.drawable.diamond_queen, 10))
         cardDeckList.add(Pair(R.drawable.diamond_king, 10))
         cardDeckList.add(Pair(R.drawable.diamond_ace, 11))

        //spades
         cardDeckList.add(Pair(R.drawable.spade_two, 2))
         cardDeckList.add(Pair(R.drawable.spade_three, 3))
         cardDeckList.add(Pair(R.drawable.spade_four, 4))
         cardDeckList.add(Pair(R.drawable.spade_five, 5))
         cardDeckList.add(Pair(R.drawable.spade_six, 6))
         cardDeckList.add(Pair(R.drawable.spade_seven, 7))
         cardDeckList.add(Pair(R.drawable.spade_eight, 8))
         cardDeckList.add(Pair(R.drawable.spade_nine, 9))
         cardDeckList.add(Pair(R.drawable.spade_ten, 10))
         cardDeckList.add(Pair(R.drawable.spade_jack, 10))
         cardDeckList.add(Pair(R.drawable.spade_queen, 10))
         cardDeckList.add(Pair(R.drawable.spade_king, 10))
         cardDeckList.add(Pair(R.drawable.spade_ace, 11))

        //clubs
         cardDeckList.add(Pair(R.drawable.club_two, 2))
         cardDeckList.add(Pair(R.drawable.club_three, 3))
         cardDeckList.add(Pair(R.drawable.club_four, 4))
         cardDeckList.add(Pair(R.drawable.club_five, 5))
         cardDeckList.add(Pair(R.drawable.club_six, 6))
         cardDeckList.add(Pair(R.drawable.club_seven, 7))
         cardDeckList.add(Pair(R.drawable.club_eight, 8))
         cardDeckList.add(Pair(R.drawable.club_nine, 9))
         cardDeckList.add(Pair(R.drawable.club_ten, 10))
         cardDeckList.add(Pair(R.drawable.club_jack, 10))
         cardDeckList.add(Pair(R.drawable.club_queen, 10))
         cardDeckList.add(Pair(R.drawable.club_king, 10))
         cardDeckList.add(Pair(R.drawable.club_ace, 11))

        cardDeckList.shuffle()
    }

}