package solutions

import print
import readFile

private const val LINES = 10
private const val COLS = 10
private val directions = listOf(
    -1 to -1, -1 to 0, -1 to 1,
    0 to -1, 0 to 1,
    1 to -1, 1 to 0, 1 to 1,
)

fun day11() {
    val input = readFile("src/main/assets/day11.txt")
    val matrix = Array(LINES) { IntArray(COLS) }
    input.forEachIndexed { index, s ->
        s.forEachIndexed { indexC, c ->
            matrix[index][indexC] = c.digitToInt()
        }
    }

    matrix.print()
    var flashes = 0
    repeat(1000) {
        val newFlashes = matrix.flash()
        flashes += newFlashes
        if (newFlashes == 100) println("$newFlashes flashes at step $it")
    }
    println("Total flashes: $flashes")
}

private fun Array<IntArray>.flash(): Int {
    this.forEachIndexed { rowInd, row ->
        row.forEachIndexed { colInd, col ->
            this[rowInd][colInd] += 1
        }
    }
    val flashed = mutableListOf<Pair<Int, Int>>()
    var checkForNewFlashes = true
    while (checkForNewFlashes) {
        checkForNewFlashes = false
        this.forEachIndexed { rowInd, row ->
            row.forEachIndexed { colInd, col ->
                if (this[rowInd][colInd] >= 10 && !(flashed.contains(rowInd to colInd))) {
                    flashed.add(rowInd to colInd)
                    this[rowInd][colInd] = 0
                    directions.forEach {
                        val newRow = rowInd + it.first
                        val newCol = colInd + it.second
                        if (isPosValid(newRow, newCol)) {
                            if (this[newRow][newCol] in 1..9) this[newRow][newCol]++
                            if (this[newRow][newCol] == 10) checkForNewFlashes = true
                        }
                    }
                }
            }
        }
    }
//    this.print()
    return flashed.size
}

private fun isPosValid(x: Int, y: Int): Boolean {
    return (x in 0 until LINES && y in 0 until COLS)
}

