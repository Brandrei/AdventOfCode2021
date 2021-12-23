package solutions

import readFile
import kotlin.math.ceil
import kotlin.math.floor


fun day18() {
    val input = readFile("src/main/assets/day18.txt")
    println(input)
    part1(input)
    part2(input)
}

private fun part2(input: List<String>) {
    var maxMagnitude = 0
    input.forEachIndexed { indL1, l1 ->
        input.forEachIndexed { indL2, l2 ->
            if (indL1 != indL2) {
                val result = addLines(parseLine(l1), parseLine(l2))
                reduceLine(result)
                val mag = magnitude(result)
                if (mag > maxMagnitude) {
                    maxMagnitude = mag
                    println("Found new max for lines $indL1 and $indL2: $mag")
                }
            }
        }
    }
    println("Maximum magnitude found: $maxMagnitude")
}

private fun part1(input: List<String>) {
    var result = parseLine(input[0])
    (1 until input.size).forEach { index ->
        val addedLine = addLines(result, parseLine(input[index]))
        val reduced = reduceLine(addedLine)
        result = reduced
//        println("After adding line $index: $result")
    }
    println("After adding all lines: $result")
    println("Magnitude: ${magnitude(result)}")
}

fun reduceLine(line: MutableList<SnailFish>): MutableList<SnailFish> {
    var reduced = true
    while (reduced) {
        reduced = false
        reduced = explode(line)
        if (reduced) continue
        reduced = splitLine(line)
    }
    return line
}

fun splitLine(line: MutableList<SnailFish>): Boolean {
    for (i in 0 until line.size) {
        if (line[i].number > 9) {
            val toSplit = line.removeAt(i)
            val newDepth = toSplit.depth + 1
            val splitValue = toSplit.number.toDouble()
            line.add(i, SnailFish(floor(splitValue / 2).toInt(), newDepth))
            line.add(i + 1, SnailFish(ceil(splitValue / 2).toInt(), newDepth))
            return true
        }
    }
    return false
}

fun explode(line: MutableList<SnailFish>): Boolean {
    for (i in 0 until line.size) {
        if (line[i].depth != 5) continue
        val left = line[i]
        val newDepth = left.depth - 1
        if (i != 0) {
            line[i - 1].number += left.number
        }
        val right = line[i + 1]
        if (i < line.size - 2) {
            line[i + 2].number += right.number
        }
        line.removeAt(i + 1)
        line.removeAt(i)
        line.add(i, SnailFish(0, newDepth))
        return true
    }
    return false
}

private fun magnitude(line: MutableList<SnailFish>) : Int {
    var depth = 4
    while (depth > 0) {
        var reduced = true
        while (reduced) {
            reduced = false
            for (i in 0 until line.size - 1) {
//                println("Index $i for size ${line.size}")
                if (line[i].depth == depth) {
//                    println("Calculating magnitude at $i")
                    val left = line[i]
                    val right = line[i+1]
                    line.removeAt(i+1)
                    line.removeAt(i)
                    line.add(i, SnailFish(3 * left.number + 2 * right.number, depth - 1 ))
//                    println("Mag after reduced depth $depth : $line")
                    reduced = true
                    break
                }
            }
        }
        depth--
    }
    return line.first().number
}

private fun parseLine(line:String) : MutableList<SnailFish> {
    var depth = 0
    val result = mutableListOf<SnailFish>()
    line.forEach {
        when (it) {
            '[' -> depth++
            ']' -> depth--
            ',' -> {}
            else -> {
                result.add(SnailFish(it.digitToInt(), depth))
            }
        }
    }
    return result
}

private fun addLines(l1: MutableList<SnailFish>, l2: MutableList<SnailFish>): MutableList<SnailFish> {
    return (l1 + l2).map { it.copy(depth = it.depth + 1) }.toMutableList()
}

data class SnailFish(var number: Int, var depth: Int) {
    override fun toString() = "[$number $depth]"
}
