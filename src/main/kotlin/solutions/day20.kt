package solutions

import print
import readFile

private val SIZE = 100

fun day20() {
    val input = readFile("src/main/assets/day20.txt")
    println(input)
    val mapping = IntArray(512)
    input[0].forEachIndexed { index, c ->
        mapping[index] = if (c == '.') 0 else 1
    }
    println("Mapping: $mapping")
    val image = Array(SIZE) { IntArray(SIZE) { 0 } }
    for (i in 2 until input.size) {
        val newLine = input[i]
        newLine.forEachIndexed { colInd, col ->
            image[i-2][colInd] = if (col == '.') 0 else 1
        }
    }
//    println("Initial image:")
    image.print()
    var filtered = filterImage(image, mapping)
    filtered.print()
//    println("new image size: ${filtered.size}")
    repeat(1) {
        filtered = filterImage(filtered, mapping)
    }
    filtered.print()
//    println("new image size: ${filtered.size}")

    var litPixels = 0
    filtered.forEach { row ->
        litPixels += row.count { it == 1 }
    }
    println("Lit pixels: $litPixels")
}

fun filterImage(image: Array<IntArray>, mapping: IntArray): Array<IntArray> {
    val result = Array(image.size + 2) { IntArray(image.size + 2) { 0 } }
    fun inBounds(x: Int, y: Int) = (x in 0 until image.size) && (y in 0 until image.size)
    for (rowInd in -1..image.size) {
        for (colInd in -1..image.size) {
            val sb = StringBuilder()
            for (i in -1..1) {
                for (j in -1..1) {
                    sb.append(
                        if (inBounds(rowInd + i, colInd + j)) image[rowInd + i][colInd + j] else 0
                    )
                }
            }
            val mapInd = Integer.valueOf(sb.toString(), 2)
//            println("MapString for $rowInd | $colInd is $mapString = $mapInd which maps to ${mapping[mapInd]}")
            result[rowInd + 1][colInd + 1] = mapping[mapInd]
        }
    }

    return result
}
