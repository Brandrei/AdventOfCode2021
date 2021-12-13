package solutions

import print
import readFile

fun day13() {
    val input = readFile("src/main/assets/day13.txt")
    var marked = mutableSetOf<Pair<Int, Int>>()
    val folds = mutableListOf<Pair<String, Int>>()
    input.forEach {inputLine ->
        if (inputLine.contains(",")) {
            marked.add(inputLine.split(",").first().toInt() to inputLine.split(",").last().toInt())
        }
        if (inputLine.contains("=")) {
            val split = inputLine.split("=")
            folds.add(split.first().last().toString() to split[1].toInt())
        }
    }
    println("Initial marked points: $marked")
    println("Input folds $folds")

//    marked = marked.foldPaper(folds.first().first, folds.first().second)
//    println("After first fold: $marked")
    folds.forEach { fold ->
        marked = marked.foldPaper(fold.first, fold.second)
    }
    val matrix = Array(6) { CharArray(40) { '.' } }
    marked.forEach {
        matrix[it.second][it.first] = '#'
    }
    matrix.print()
//    println(marked.count())
}

private fun MutableSet<Pair<Int, Int>>.foldPaper(axis: String, line: Int) : MutableSet<Pair<Int,Int>> {
    return this.map {
        var targetX = it.first
        var targetY = it.second

        if (axis == "y" && targetY > line) {
            targetY = (2 * line) - targetY
        }
        if (axis == "x" && targetX > line) {
            targetX = (2 * line) - targetX
        }

        targetX to targetY
    }.toMutableSet()
}
