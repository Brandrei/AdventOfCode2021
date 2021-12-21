package solutions

import kotlin.math.max
import kotlin.random.Random

const val WINNING_SCORE = 21

fun day21() {
//    testPlayerAdvance()
//    return

    val player1 = Player("p1", 8, 0)
    val player2 = Player("p2", 6, 0)
    val dice = Dice()
    var rolls = 0
    while (true) {
        val p1Adv = dice.roll() + dice.roll() + dice.roll()
        rolls += 3
        player1.advance(p1Adv)
        if (player1.won()) {
            println("P1 won. P2 score: ${player2.score}, Rolls $rolls -> Result: ${player2.score * rolls} ")
            break
        }

        val p2Adv = dice.roll() + dice.roll() + dice.roll()
        rolls += 3
        player2.advance(p2Adv)
        if (player2.won()) {
            println("P2 won. P1 score: ${player1.score}, Rolls $rolls -> Result: ${player1.score * rolls} ")
            break
        }
    }
    part2()
}

class Dice(var value: Int = 0) {
    fun roll(): Int {
        value = value++ % 10 + 1
        return value
    }
}

data class Player(val name: String, var position: Int = 1, var score: Int = 0) {
    fun won() = score >= WINNING_SCORE
    fun advance(toAdvance: Int) {
        var newPos = position + toAdvance
        newPos--
        newPos %= 10
        newPos++
        score += newPos
        println("${name} advanced from $position with $toAdvance to $newPos. Score: $score")
        position = newPos
    }
}

fun testPlayerAdvance() {
    val testPlayer = Player("test")
    repeat(10) {
        val toAdv = Random(100).nextInt(10)
        testPlayer.advance(toAdv)
        println(testPlayer)
    }
}

data class State(var p1Pos: Int, var p2Pos: Int, var p1Score: Int, var p2Score: Int)

fun part2() {
    var p1Wins: Long = 0
    var p2Wins: Long = 0
    val states = mutableMapOf(State(8, 6, 0, 0) to 1L)
    while (states.isNotEmpty()) {
        val (state, count) = states.entries.first()
        states.remove(state)
        println("States: ${states.size}")
        for (p1d1 in 1..3) {
            for (p1d2 in 1..3) {
                for (p1d3 in 1..3) {
                    val posP1 = advance(state.p1Pos, p1d1+p1d2+p1d3)
                    val scoreP1 = state.p1Score + posP1
                    if (scoreP1 >= WINNING_SCORE) {
                        p1Wins += count
                    } else {
                        for (p2d1 in 1..3) {
                            for (p2d2 in 1..3) {
                                for (p2d3 in 1..3) {
                                    val posP2 = advance(state.p2Pos, p2d1 + p2d2 + p2d3)
                                    val scoreP2 = state.p2Score + posP2
                                    if (scoreP2 >= WINNING_SCORE) {
                                        p2Wins += count
                                    } else {
                                        val newState = State(posP1, posP2, scoreP1, scoreP2)
                                        if (newState in states) {
                                            states[newState] = states[newState]!! + count
                                        } else {
                                            states[newState] = count
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    println("States ${states.size}")
    println("Wins: $p1Wins / $p2Wins -> ${max(p1Wins, p2Wins)}")

}

fun advance(from: Int, toAdvance: Int): Int {
    var newPos = from + toAdvance
    newPos--
    newPos %= 10
    newPos++
    return newPos
}
