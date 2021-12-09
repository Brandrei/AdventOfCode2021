package solutions

import readFile

const val LINES = 100
const val COLS = 100

fun day9() {
    val input = readFile("src/main/assets/day9.txt")
    println(input)

    val matrix = Array(LINES) { IntArray(COLS) }
    input.forEachIndexed{ index, value ->
        value.forEachIndexed { colIndex, colValue ->
            matrix[index][colIndex] = colValue.digitToInt()
        }
    }

    val basinSizes = mutableListOf<Int>()
    matrix.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, col ->
            var min = true
            if (isPosValid(rowIndex, colIndex -1 ) && col >= matrix [rowIndex][colIndex -1]) min = false
            if (isPosValid(rowIndex - 1, colIndex) && col >= matrix [rowIndex - 1][colIndex]) min = false
            if (isPosValid(rowIndex + 1, colIndex) && col >= matrix [rowIndex + 1][colIndex]) min = false
            if (isPosValid(rowIndex, colIndex +1 ) && col >= matrix [rowIndex][colIndex +1]) min = false
            if (min) {
                println("Local min found at [$rowIndex, $colIndex] = $col")
                basinSizes.add(calculateSize(matrix.clone(), rowIndex, colIndex))
            }
        }
    }
    basinSizes.sortDescending()
    println(basinSizes)
    val multiple = basinSizes[0] * basinSizes[1] * basinSizes[2]
    println(multiple)
}

fun calculateSize(matrix: Array<IntArray>, rowIndex: Int, colIndex: Int): Int {
    if (!isPosValid(rowIndex, colIndex)) return 0
    if (matrix[rowIndex][colIndex] == 9 || matrix[rowIndex][colIndex] == -1) return 0
    matrix[rowIndex][colIndex] = -1
    return 1 + calculateSize(matrix, rowIndex, colIndex - 1) +
            calculateSize(matrix, rowIndex, colIndex + 1) +
            calculateSize(matrix, rowIndex - 1, colIndex) +
            calculateSize(matrix, rowIndex + 1, colIndex)

}

fun isPosValid(x: Int, y: Int): Boolean {
    return (x in 0 until LINES && y in 0 until COLS )
}

