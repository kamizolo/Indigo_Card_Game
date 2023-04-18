package indigo

fun main() {
    class score (var playerScore: Int = 0, var computerScore: Int = 0, var playerCards: Int = 0, var computerCards: Int = 0, var lastWinner: String = "") {

        fun print(winner: String) {
            println("$winner wins cards")
            println("Score: Player $playerScore - Computer $computerScore")
            println("Cards: Player $playerCards - Computer $computerCards")
        }
        fun print() {
            println("Score: Player $playerScore - Computer $computerScore")
            println("Cards: Player $playerCards - Computer $computerCards")
        }

        fun final(cards: MutableList<String>) {
            calculate(cards, lastWinner)
            if (playerCards > computerCards) {
                playerScore += 3
            } else if (playerCards < computerCards) {
                computerScore += 3
            }
        }
        
        fun calculate(cards: MutableList<String>, winner: String) {
            lastWinner = winner
            for (card in cards) {
                if (lastWinner == "Player") {
                    if (card[0] == 'A' || card[0] == 'K' || card[0] == 'Q' || card[0] == 'J' || (card[0] == '1' && card[1] == '0')) {
                        playerScore++
                    }
                    playerCards++
                } else {
                    if (card[0] == 'A' || card[0] == 'K' || card[0] == 'Q' || card[0] == 'J' || (card[0] == '1' && card[1] == '0')) {
                        computerScore++
                    }
                    computerCards++
                }
            }
        }
    }

    class player(val hand: MutableList<String>, val id: String){
        var score = 0
        var cards = 0

        fun add(card: String){
            hand.add(card)
        }
        fun remove(ind: Int): String{
            return hand.removeAt(ind)
        }
        fun holding(p: String = "Player"){
            if(p == "Player") {
                for (i in hand.indices) print("${i + 1})${hand[i]} ")
            } else {
                for (i in hand.indices) print("${hand[i]} ")
            }
            println()
        }
        fun choose(): Int {
            var card = ""
            while (true){
                println("Choose a card to play (1-${hand.size}):")
                card = readln()
                if (card == "exit"){
                    return -1
                } else if (card in "1".."6") break
            }
            return card.toInt() - 1
        }
        fun isCandidate(card: String, tableCard: String): Boolean {
            if (card.length == 3 && tableCard.length == 3) { //they 10*
                return true
            } else {
                if (card.length == 3) { // same suit if one of them is 10*
                    if (card[2] == tableCard[1]){
                        return true
                    }
                }else if (tableCard.length == 3) { // same suit if one of them is 10*
                    if (card[1] == tableCard[2]) {
                        return true
                    }
                }else if (card[0] == tableCard[0]) {  // same rank
                    return true
                } else if (card[1] == tableCard[1]) { // same suit
                    return true
                }
            }
            return false
        } //for test 5
        fun sameSuit(c: String, o: String): Boolean {
            if (c.length == 3 && o.length == 3) { //they 10*
                return false
            } else {
                if (c.length == 3) { // same suit if one of them is 10*
                    if (c[2] == o[1]){
                        return true
                    }
                }else if (o.length == 3) { // same suit if one of them is 10*
                    if (c[1] == o[2]) {
                        return true
                    }
                } else if (c[1] == o[1]) { // same suit
                    return true
                }
            }
            return false
        }
        fun sameRank(c: String, o: String): Boolean {
            if (c.length == 3 && o.length == 3) { //they 10*
                return true
            } else {
                if (c.length == 3) { // same suit if one of them is 10*
                    return false
                }else if (o.length == 3) { // same suit if one of them is 10*
                    return false
                }else if (c[0] == o[0]) {  // same rank
                    return true
                }
            }
            return false
        }
        fun cpuChoose(tabelCards: Int, activeCard: String): String {
            if(hand.size == 1) return hand.removeAt(0)  //first condition

            var highestSuit = 0 //highest "score"
            var highestRank = 0
            val tempSuit = mutableListOf<Int>() //stores the "score" of candidates
            val tempRank = mutableListOf<Int>()
                //calculates the score for cards
            for (c in hand) {
                var num = 0
                var num2 = 0
                for (b in hand) {
                    if(sameSuit(c,b)) num++
                    if(sameRank(c,b)) num2++
                }
                if (num > highestSuit) highestSuit = num
                if (num2 > highestRank) highestRank = num2
                tempSuit.add(num)
                tempRank.add(num2)
            }

            //no table card
            if(tabelCards == 0) {
                //1
                if(highestSuit >= 2){
                    for (i in tempSuit.indices) {
                        if(tempSuit[i] >= 2) return hand.removeAt(i)
                    }
                }
                //2
                if(highestRank >= 2){
                    for (i in tempRank.indices) {
                        if(tempRank[i] >= 2) return hand.removeAt(i)
                    }
                }
                //3
                return hand.removeFirst()
            }

            //tabelCards > 0

            //new list with candidate cards
            val candList = mutableListOf<String>()
            for (c in hand) {
                if(isCandidate(c, activeCard)) {
                    candList.add(c)
                }
            }
            //zero candidates
            val size = candList.size
            if(candList.size == 0) {
                if(highestSuit >= 2){
                    for (i in tempSuit.indices) {
                        if(tempSuit[i] >= 2) return hand.removeAt(i)
                    }
                }else if(highestRank >= 2){
                    for (i in tempRank.indices) {
                        if(tempRank[i] >= 2) return hand.removeAt(i)
                    }
                }else return hand.removeFirst()
            }
            //only one candidate
            if(candList.size == 1) {
                hand.remove(candList[0])
                return candList[0]
            }
            //more than 1 candidate
            if(candList.size > 1) {
                highestSuit = 0
                tempSuit.clear()
                highestRank = 0
                tempRank.clear()
                for (c in candList) {
                    var num = 0
                    var num2 = 0
                    for (b in candList) {
                        if(sameSuit(c,b)) num++
                        if(sameRank(c,b)) num2++
                    }
                    if (num > highestSuit) highestSuit = num
                    if (num > highestRank) highestRank = num2
                    tempSuit.add(num)
                    tempRank.add(num2)
                }


                //1
                if(highestSuit >= 2){
                    for (i in tempSuit.indices) {
                        if(tempSuit[i] >= 2) {
                            for(j in hand.indices) {
                                if(candList[i] == hand[j]) {
                                    return hand.removeAt(j)
                                }
                            }
                        }
                    }
                }
                //2
                if(highestRank >= 2){
                    for (i in tempRank.indices) {
                        if(tempRank[i] >= 2) {
                            for(j in hand.indices) {
                                if(candList[i] == hand[j]) {
                                    return hand.removeAt(j)
                                }
                            }
                        }
                    }
                }
                //3
                for (i in hand.indices){
                    if(hand[i] == candList[0]) return hand.removeAt(i)
                }

            }


            return "fail"
        }
        fun handSize(): Int{
            return hand.size
        }
    }
    val rank = listOf("A","2","3","4","5","6","7","8","9","10","J","Q","K")
    val suit = listOf("♦", "♥", "♠", "♣")
    var deck = mutableListOf<String>()
    val tableCards = mutableListOf<String>()
    val scoreBoard = score()
    var playedCards = 0
    var cardsOnTable = 0
    var lastCard = ""
    var playerTurn = false
    var playing = true
    val me = player(mutableListOf<String>(), "Player")
    val computer = player(mutableListOf<String>(), "Computer")

    fun reset() {
        deck = mutableListOf<String>()
        for (type in suit) {
            for (card in rank) deck.add("$card$type")
        }
    }
    fun shuffle() {
        deck.shuffle()
    }
    fun get() {
        var out = ""
        println("Number of cards:")
        val n = readln()
        var num = 0
        if(n[0] in '0'..'9'){
            num = n.toInt()
        }else {
            println("Invalid number of cards.")
            return
        }

        if (num !in 1..52) {
            println("Invalid number of cards.")
            return
        }else if (num > deck.size) {
            println("The remaining cards are insufficient to meet the request.")
            return
        }

        for (i in 1..num){
            out += "${deck.removeLast()} "
        }
        out = out.trimEnd()
        println(out)
    }
    fun init() {
        // init player
        while(true) {
            println("Play first?")
            val a = readln()
            if(a.lowercase() == "yes") {
                playerTurn = true
                scoreBoard.lastWinner = "Player"
                break
            } else if(a.lowercase() == "no") {
                scoreBoard.lastWinner = "Computer"
                break
            }
        }
        reset()
        shuffle()
        //cards to players
        for (i in 1..6){
            var a = deck.removeLast()
            me.add(a)
            a = deck.removeLast()
            computer.add(a)
        }
        //cards for the table
        print("Initial cards on the table: ")
        for (i in 1..4){
            lastCard = deck.removeLast()
            tableCards.add(lastCard)
            print("$lastCard ")
            playedCards++
            cardsOnTable++
        }
        println("\n")
    }

    fun playCard(p: player) {
        print("Cards in hand: ")
        p.holding()
        val pos = p.choose()
        if (pos == -1) {
            playing = false
            return
        }

        val temp = lastCard
        lastCard = p.remove(pos)
        tableCards.add(lastCard)
        playedCards++
        cardsOnTable++
        if(temp != "") {
            if (temp.length == 3 && lastCard.length == 3) {
                scoreBoard.calculate(tableCards, p.id)
                scoreBoard.print(p.id)
                cardsOnTable = 0
                tableCards.clear()
                lastCard = ""

            } else {
                if (temp[0] == lastCard[0]) {
                    scoreBoard.calculate(tableCards, p.id)
                    scoreBoard.print(p.id)
                    cardsOnTable = 0
                    tableCards.clear()
                    lastCard = ""
                } else if (temp[1] == lastCard[1]) {
                    scoreBoard.calculate(tableCards, p.id)
                    scoreBoard.print(p.id)
                    cardsOnTable = 0
                    tableCards.clear()
                    lastCard = ""
                } else if (temp.length == 3) {
                    if (temp[2] == lastCard[1]){
                        scoreBoard.calculate(tableCards, p.id)
                        scoreBoard.print(p.id)
                        cardsOnTable = 0
                        tableCards.clear()
                        lastCard = ""
                    }
                } else if (lastCard.length == 3) {
                    if (temp[1] == lastCard[2]){
                        scoreBoard.calculate(tableCards, p.id)
                        scoreBoard.print(p.id)
                        cardsOnTable = 0
                        tableCards.clear()
                        lastCard = ""
                    }
                }
            }
        }
        if(p.handSize() == 0 && deck.size != 0){
            for(i in 1..6)
                p.add(deck.removeLast())
        }
        println()
        playerTurn = !playerTurn
    }
    fun cpuPlayCard(p: player) {
        val temp = lastCard
        p.holding(p.id)
        lastCard = p.cpuChoose(cardsOnTable, temp)
        tableCards.add(lastCard)
        if(p.handSize()== 0 && deck.size != 0){
            for( i in 1..6)
                p.add(deck.removeLast())
        }
        println("Computer plays $lastCard")
        playedCards++
        cardsOnTable++
        playerTurn = !playerTurn
        if(temp != "") {
            if (temp.length == 3 && lastCard.length == 3) {
                scoreBoard.calculate(tableCards, p.id)
                scoreBoard.print(p.id)
                println()
                cardsOnTable = 0
                tableCards.clear()
                lastCard = ""

            } else {
                if (temp[0] == lastCard[0]) {
                    scoreBoard.calculate(tableCards, p.id)
                    scoreBoard.print(p.id)
                    println()
                    cardsOnTable = 0
                    tableCards.clear()
                    lastCard = ""
                } else if (temp[1] == lastCard[1]) {
                    scoreBoard.calculate(tableCards, p.id)
                    scoreBoard.print(p.id)
                    println()
                    cardsOnTable = 0
                    tableCards.clear()
                    lastCard = ""
                }  else if (temp.length == 3) {
                    if (temp[2] == lastCard[1]){
                        scoreBoard.calculate(tableCards, p.id)
                        scoreBoard.print(p.id)
                        println()
                        cardsOnTable = 0
                        tableCards.clear()
                        lastCard = ""
                    }
                } else if (lastCard.length == 3) {
                    if (temp[1] == lastCard[2]){
                        scoreBoard.calculate(tableCards, p.id)
                        scoreBoard.print(p.id)
                        println()
                        cardsOnTable = 0
                        tableCards.clear()
                        lastCard = ""
                    }
                }
            }
        }
    }
    println("Indigo Card Game")
    init()

    while (playing) {
        if (cardsOnTable != 0) {
            println("$cardsOnTable cards on the table, and the top card is $lastCard")
        } else println("No cards on the table ")
        if(playerTurn) playCard(me) else cpuPlayCard(computer)
        if (playedCards == 52) {
            if (cardsOnTable != 0) {
                println("$cardsOnTable cards on the table, and the top card is $lastCard")
            } else println("No cards on the table ")
            break
        }
    }
    if (playing) {
        scoreBoard.final(tableCards)
        scoreBoard.print()
    }
    println("Game Over")


}

