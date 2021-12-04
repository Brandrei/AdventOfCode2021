package solutions

import readFile

fun day4() {
    val lineList = readFile("src/main/assets/day4.txt")
    val numbers = lineList[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Board>()
    println(lineList[1].isBlank())

    var i = 2
    while ( i < lineList.size) {
        if (lineList[i].isNotBlank()) {
            val map = mutableMapOf<Int, MutableList<Pair<Int, Boolean>>>()
            for (j in 0..4) {
                map[j] = lineList[i+j].trim().split("\\s+".toRegex()).map { it.trim().toInt() to false }.toMutableList()
            }
            boards.add(Board(map))
        }
        i+=6
    }

    run breakStatement@ {
        numbers.forEach { inputNumber ->
            boards.forEach { board ->
                if (!board.reachedBingo) {
                    board.addNumber(inputNumber)
                    if (board.checkForBingo()) {
                        val score = board.calculateScore()
                        println(
                            "Board $board \n won with score: ${score} and inputNumber $inputNumber" +
                                    "\n Total = ${score * inputNumber}"
                        )
                        board.reachedBingo = true
                    }
                }
            }
        }
    }
}

class Board(private val rows: Map<Int, MutableList<Pair<Int, Boolean>>>, var reachedBingo: Boolean = false) {

    override fun toString(): String = buildString {
        rows.forEach {
            append("$it\n")
        }
    }

    fun addNumber(inputNumber: Int) {
        run brk@ {
            rows.values.forEach { cols ->
                cols.forEachIndexed { index, value ->
                    if (value.first == inputNumber) {
                        cols[index] = inputNumber to true
                        return@brk
                    }
                }
            }
        }
    }

    fun checkForBingo(): Boolean {
        //rows
        rows.values.forEach fe1@ {
            it.forEach {
                if (!it.second) return@fe1
            }
            return true
        }
        //cols
        for (i in 0..4) {
            var result = true
            rows.values.forEach { rows ->
                if (!rows[i].second) { result = false }
            }
            if (result) return true
        }
        return false
    }

    fun calculateScore(): Int {
        var score = 0
        rows.values.forEach { cols ->
            cols.forEach { value ->
                if (!value.second) {
                    score += value.first
                }
            }
        }
        return score
    }
}
