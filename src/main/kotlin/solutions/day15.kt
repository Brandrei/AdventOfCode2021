package solutions

import print
import readFile

private const val LINES = 500
private const val COLS = 500

private val directions = listOf(
    -1 to 0, 1 to 0, 0 to -1, 0 to 1
)

data class Tile(val x: Int, val y: Int, val weight: Int)

fun day15() {
    val input = readFile("src/main/assets/day15.txt")

    val matrix = Array(100) { IntArray(100) }

    input.forEachIndexed { rowIndex, line ->
        line.forEachIndexed { colIndex, col ->
            matrix[rowIndex][colIndex] = col.digitToInt()
        }
    }
//    matrix.print()
    val matrixEnlarged = enlarge(matrix)
    matrixEnlarged.print()

    val riskMap = Array(LINES) { IntArray(COLS) { Int.MAX_VALUE } }
    riskMap[0][0] = 0
    val queue = mutableListOf(Tile(0,0,matrix[0][0]))
    var iter = 1
    while (queue.size > 0) {
        val currentTile = queue.removeFirst()
        directions.forEach {
            val neighbourX = currentTile.x + it.first
            val neighbourY = currentTile.y + it.second
            if (isPosValid(neighbourX, neighbourY)) {
                val neighbourCost = riskMap[neighbourX][neighbourY]
                val costToNeighbour = riskMap[currentTile.x][currentTile.y] + matrixEnlarged[neighbourX][neighbourY]
                if (neighbourCost > costToNeighbour) {
                    riskMap[neighbourX][neighbourY] = costToNeighbour
                    queue.add(Tile(neighbourX, neighbourY, matrixEnlarged[neighbourX][neighbourY]))
                }
            }
        }
//        println("calculating at $iter, queue size is: ${queue.size}")
        iter++
    }
    riskMap.print()



}

fun enlarge(matrix: Array<IntArray>): Array<IntArray> {
    val result = Array(LINES) { IntArray(COLS) }
    repeat(5) { times ->
        println(times)
        matrix.forEachIndexed { rowIndex, rows ->
            rows.forEachIndexed { colIndex, col ->
                var toAdd = matrix[rowIndex][colIndex] + times
                if (toAdd > 9) toAdd -= 9
                result[rowIndex + (times * 100)][colIndex] = toAdd
            }
        }
    }
    result.forEachIndexed { rowIndex, rows ->
        repeat(5) { times ->
            for (i in 0 until 100) {
                var toAdd = result[rowIndex][i] + times
                if (toAdd > 9) toAdd -= 9
                result[rowIndex][i +  + (times * 100)] = toAdd
            }
        }
    }

//    println("After rows:")
    result.print()
    return result
}


private fun isPosValid(x: Int, y: Int): Boolean {
    return (x in 0 until LINES && y in 0 until COLS)
}
