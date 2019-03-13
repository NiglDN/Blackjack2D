package com.example.nigl2.a2dblackjack

object SingletonCards {
    val cardDeckList : MutableList<Pair<String, Int>> = mutableListOf()
    fun createDeck(){
        cardDeckList.add(Pair("heart_two", 2))

        //add all cards
        //hearts
        cardDeckList.add(Pair("heart_two", 2))
        cardDeckList.add(Pair("heart_three", 3))
        cardDeckList.add(Pair("heart_four", 4))
        cardDeckList.add(Pair("heart_five", 5))
        cardDeckList.add(Pair("heart_six", 6))
        cardDeckList.add(Pair("heart_seven", 7))
        cardDeckList.add(Pair("heart_eight", 8))
        cardDeckList.add(Pair("heart_nine", 9))
        cardDeckList.add(Pair("heart_ten", 10))
        cardDeckList.add(Pair("heart_jack", 10))
        cardDeckList.add(Pair("heart_queen", 10))
        cardDeckList.add(Pair("heart_king", 10))
        cardDeckList.add(Pair("heart_ace", 11))

        //diamond
         cardDeckList.add(Pair("diamond_two", 2))
         cardDeckList.add(Pair("diamond_three", 3))
         cardDeckList.add(Pair("diamond_four", 4))
         cardDeckList.add(Pair("diamond_five", 5))
         cardDeckList.add(Pair("diamond_six", 6))
         cardDeckList.add(Pair("diamond_seven", 7))
         cardDeckList.add(Pair("diamond_eight", 8))
         cardDeckList.add(Pair("diamond_nine", 9))
         cardDeckList.add(Pair("diamond_ten", 10))
         cardDeckList.add(Pair("diamond_jack", 10))
         cardDeckList.add(Pair("diamond_queen", 10))
         cardDeckList.add(Pair("diamond_king", 10))
         cardDeckList.add(Pair("diamond_ace", 11))

        //spades
         cardDeckList.add(Pair("spade_two", 2))
         cardDeckList.add(Pair("spade_three", 3))
         cardDeckList.add(Pair("spade_four", 4))
         cardDeckList.add(Pair("spade_five", 5))
         cardDeckList.add(Pair("spade_six", 6))
         cardDeckList.add(Pair("spade_seven", 7))
         cardDeckList.add(Pair("spade_eight", 8))
         cardDeckList.add(Pair("spade_nine", 9))
         cardDeckList.add(Pair("spade_ten", 10))
         cardDeckList.add(Pair("spade_jack", 10))
         cardDeckList.add(Pair("spade_queen", 10))
         cardDeckList.add(Pair("spade_king", 10))
         cardDeckList.add(Pair("spade_ace", 11))

        //clubs
         cardDeckList.add(Pair("club_two", 2))
         cardDeckList.add(Pair("club_three", 3))
         cardDeckList.add(Pair("club_four", 4))
         cardDeckList.add(Pair("club_five", 5))
         cardDeckList.add(Pair("club_six", 6))
         cardDeckList.add(Pair("club_seven", 7))
         cardDeckList.add(Pair("club_eight", 8))
         cardDeckList.add(Pair("club_nine", 9))
         cardDeckList.add(Pair("club_ten", 10))
         cardDeckList.add(Pair("club_jack", 10))
         cardDeckList.add(Pair("club_queen", 10))
         cardDeckList.add(Pair("club_king", 10))
         cardDeckList.add(Pair("club_ace", 11))

        cardDeckList.shuffle()
    }

}