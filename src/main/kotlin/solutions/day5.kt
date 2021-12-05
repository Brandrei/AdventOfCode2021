package solutions

import readFile
import kotlin.math.max
import kotlin.math.min
import kotlin.ranges.IntProgression.Companion.fromClosedRange

fun day5() {
    val input = readFile("src/main/assets/day5.txt")
    val lines = buildInput(input)
    println(lines)
    val matrixSize = 10
    val matrix = Array(matrixSize) { IntArray(matrixSize) }
    lines.forEach { line ->
        if (line.p1.x == line.p2.x) {
            println("row")
            val start = min(line.p1.y, line.p2.y)
            val end = max(line.p1.y, line.p2.y)
            for (i in start..end) {
                matrix[i][line.p1.x]++
            }
        } else if (line.p1.y == line.p2.y) {
            println("col")
            val start = min(line.p1.x, line.p2.x)
            val end = max(line.p1.x, line.p2.x)
            for (i in start..end) {
                matrix[line.p1.y][i]++
            }
        } else {
            println("diag")
            val xStep = line.p2.x.compareTo(line.p1.x)
            val xProgression = fromClosedRange(line.p1.x, line.p2.x, xStep)
            println("XStep: $xStep, xProgression: $xProgression")
            val yStep = line.p2.y.compareTo(line.p1.y)
            val yProgression = fromClosedRange(line.p1.y, line.p2.y, yStep)
            println("YStep: $yStep, YProgression: $yProgression")

            xProgression.zip(yProgression).forEach { (x, y) ->
                matrix[y][x]++
            }
        }
    }

    println("\n\n")
    matrix.print()
    println("Count = ${matrix.countScore()}")
}

fun buildInput(input: List<String>): List<Line> = input.map { inputLine ->
    val (x1, y1) = inputLine.split(" -> ").first().split(",")
    val (x2, y2) = inputLine.split(" -> ").last().split(",")
    Line(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
}

fun Array<IntArray>.print() {
    this.forEach { rows ->
        rows.forEach { print("$it ") }
        println()
    }
}

fun Array<IntArray>.countScore(): Int {
    var count = 0
    this.forEach { rows ->
        rows.forEach {
            if (it > 1) count++
        }
    }
    return count
}

data class Point(val x: Int, val y: Int)

data class Line(val p1: Point, val p2: Point)
