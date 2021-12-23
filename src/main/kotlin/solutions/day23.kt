package solutions

import print
import readFile
import kotlin.math.abs
import kotlin.system.measureNanoTime

typealias Matrix = Array<CharArray>


//failed miserably, ended up doing it on paper. Just minimize the most costly operations.
fun day23() {
    val input = readFile("src/main/assets/day23.txt")
    println(input)
    val t1 = measureNanoTime {
        part1(input)
    }
    println("Part 1 took $t1")

    val t2 = measureNanoTime {
        part2(input)
    }
    println("Part 2 took $t2")
}

private fun part1(input: List<String>) {
    val matrix = Array(5) { CharArray(13) { '#' } }
    (1..11).forEach {
        matrix[1][it] = '0'
        matrix[2][it] = input[2][it]
        if (it <= 9) matrix[3][it] = input[3][it]
    }
    matrix.print()
    println("Part1: ${solveFor(matrix)}")
}

private fun solveFor(matrix: Matrix): Int? {
    val moves = getAllPossibleMoves(matrix)
    val scores = moves.map {
        calculateEnergyFor(it.first, it.second)
    }
    println("Calculcated scores: $scores")
    return scores.minOrNull()
}

fun calculateEnergyFor(matrix: Matrix, currentEnergy: Int): Int {
    if (matrix.isSolved()) return 0
    val newMoves = getAllPossibleMoves(matrix)
    if (newMoves.isEmpty()) {
        return 1_000_000
    } else {
        newMoves.forEach { (newMove, energy) ->
            return currentEnergy + calculateEnergyFor(newMove, energy)
        }
    }
    return 1_000_000
}

private fun Array<CharArray>.isSolved(): Boolean {
    return this[2][3] == 'A' && this[3][3] == 'A' &&
            this[2][5] == 'B' && this[3][5] == 'B' &&
            this[2][7] == 'C' && this[3][7] == 'C' &&
            this[2][9] == 'D' && this[3][9] == 'D'
}

fun getAllPossibleMoves(matrix: Matrix): List<Pair<Array<CharArray>, Int>> {
    val result = mutableListOf<Pair<Matrix, Int>>()
    matrix.forEachIndexed { rowInd, row ->
        row.forEachIndexed { colInd, col ->
            when (col) {
                'A' -> {
                    if (matrix[2][3] != 'A' || matrix[3][3] != 'A') {
                        result.addAll(positionsFor('A', matrix, rowInd, colInd))
                    }
                }
                'B' -> {
                    if (matrix[2][5] != 'B' || matrix[3][5] != 'B') {
                        result.addAll(positionsFor('B', matrix, rowInd, colInd))
                    }
                }
                'C' -> {
                    if (matrix[2][7] != 'C' || matrix[3][7] != 'C') {
                        result.addAll(positionsFor('C', matrix, rowInd, colInd))
                    }
                }
                'D' -> {
                    if (matrix[2][9] != 'D' || matrix[3][9] != 'D') {
                        result.addAll(positionsFor('D', matrix, rowInd, colInd))
                    }
                }
            }
        }
    }
    return result
}

fun positionsFor(c: Char, matrix: Array<CharArray>, rowInd: Int, colInd: Int): List<Pair<Array<CharArray>, Int>> {
    fun calcEnergy(newRow: Int, newCol: Int): Int {
        return abs(newRow - rowInd) + abs(newCol - colInd)
    }

    fun isPosFree(newRow: Int, newCol: Int): Boolean = matrix[newRow][newCol] == '0'
    val validPositionsForAll = listOf(1 to 1, 1 to 2, 1 to 4, 1 to 6, 1 to 8, 1 to 10, 1 to 11)
    val validPosForA = validPositionsForAll + (2 to 3) + (3 to 3) + (1 to 3)
    val validPosForB = validPositionsForAll + (2 to 5) + (3 to 5) + (1 to 5)
    val validPosForC = validPositionsForAll + (2 to 7) + (3 to 7) + (1 to 7)
    val validPosForD = validPositionsForAll + (2 to 9) + (3 to 9) + (1 to 9)
    val result = mutableListOf<Pair<Array<CharArray>, Int>>()
    when (c) {
        'A' -> {
            validPosForA.forEach {
                if (isPosFree(it.first, it.second)) {
                    val newMatrix = copyMatrix(matrix)
                    newMatrix[rowInd][colInd] = '0'
                    newMatrix[it.first][it.second] = 'A'
                    result.add(newMatrix to calcEnergy(it.first, it.second))
                }
            }
        }
        'B' -> {
            validPosForB.forEach {
                if (isPosFree(it.first, it.second)) {
                    val newMatrix = copyMatrix(matrix)
                    newMatrix[rowInd][colInd] = '0'
                    newMatrix[it.first][it.second] = 'B'
                    result.add(newMatrix to calcEnergy(it.first, it.second) * 10)
                }
            }
        }
        'C' -> {
            validPosForC.forEach {
                if (isPosFree(it.first, it.second)) {
                    val newMatrix = copyMatrix(matrix)
                    newMatrix[rowInd][colInd] = '0'
                    newMatrix[it.first][it.second] = 'C'
                    result.add(newMatrix to calcEnergy(it.first, it.second) * 100)
                }
            }
        }
        'D' -> {
            validPosForD.forEach {
                if (isPosFree(it.first, it.second)) {
                    val newMatrix = copyMatrix(matrix)
                    newMatrix[rowInd][colInd] = '0'
                    newMatrix[it.first][it.second] = 'D'
                    result.add(newMatrix to calcEnergy(it.first, it.second) * 1000)
                }
            }
        }
    }
    return result
}

private fun copyMatrix(input: Matrix): Matrix {
    val newMatrix = Array(5) { CharArray(13) { '#' } }
    input.forEachIndexed { index, chars ->
        chars.forEachIndexed { ind2, c ->
            newMatrix[index][ind2] = c
        }
    }
    return newMatrix
}

private fun part2(input: List<String>) {

}