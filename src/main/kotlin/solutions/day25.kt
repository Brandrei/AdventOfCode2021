package solutions

import print
import readFile
import kotlin.system.measureTimeMillis

private const val ROWS = 137
private const val COLS = 139

fun day25() {
    val input = readFile("src/main/assets/day25.txt")
    println(input)
    val matrix = Array(ROWS) { CharArray(COLS) { '.' } }
    input.forEachIndexed { lineNo, line ->
        line.forEachIndexed { charIndex, c ->
            matrix[lineNo][charIndex] = c
        }
    }
    matrix.print()
    val t = measureTimeMillis {
        println("Part1 result: ${part1(matrix)}")
    }
    println("Part 1 done in $t")
}

private fun part1(matrix: Array<CharArray>): Int {
    var newMatrix = copyMatrix(matrix)
    var steps = 1
    while (true) {
        //move east
        val east = moveEast(newMatrix)
        println("Step $steps")
//        east.first.print()
        //move south
        val south = moveSouth(east.first)
//        println("South step $steps")
//        south.first.print()
        if (!east.second && !south.second) {
            south.first.print()
            return steps
        }
        newMatrix = south.first
        steps++
    }
}

fun moveSouth(input: Array<CharArray>): Pair<Matrix, Boolean> {
    fun getNextSouth(oldX: Int, oldY: Int): Pair<Int, Int> {
        var newX = oldX + 1
        if (newX == ROWS) newX = 0
        return newX to oldY
    }

    val result = Array(ROWS) { CharArray(COLS) { '.' } }
    var moved = false
    input.forEachIndexed { rowInd, rows ->
        rows.forEachIndexed { colInd, c ->
            if (c == 'v') {
                val (newX, newY) = getNextSouth(rowInd, colInd)
                if (input[newX][newY] == '.') {
                    moved = true
                    result[newX][newY] = 'v'
                } else {
                    result[rowInd][colInd] = input[rowInd][colInd]
                }
            } else {
                if (result[rowInd][colInd] != 'v') {
                    result[rowInd][colInd] = input[rowInd][colInd]
                }
            }
        }
    }
    return result to moved
}

private fun moveEast(input: Array<CharArray>): Pair<Matrix, Boolean> {
    fun getNextEast(oldX: Int, oldY: Int): Pair<Int, Int> {
        var newY = oldY + 1
        if (newY == COLS) newY = 0
        return oldX to newY
    }

    val result = Array(ROWS) { CharArray(COLS) { '.' } }
    var moved = false
    input.forEachIndexed { rowInd, rows ->
        rows.forEachIndexed { colInd, c ->
            if (c == '>') {
                val (newX, newY) = getNextEast(rowInd, colInd)
                if (input[newX][newY] == '.') {
                    moved = true
                    result[newX][newY] = '>'
                } else {
                    result[rowInd][colInd] = input[rowInd][colInd]
                }
            } else {
                if (result[rowInd][colInd] != '>') {
                    result[rowInd][colInd] = input[rowInd][colInd]
                }
            }
        }
    }
    return result to moved
}

private fun copyMatrix(input: Matrix): Matrix {
    val newMatrix = Array(ROWS) { CharArray(COLS) }
    input.forEachIndexed { index, chars ->
        chars.forEachIndexed { ind2, c ->
            newMatrix[index][ind2] = c
        }
    }
    return newMatrix
}
