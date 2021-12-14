package solutions

import readFile
import java.util.*

var currentPairs = mutableMapOf<String, Long>()

fun day14() {
    val input = readFile("src/main/assets/day14.txt")
    val compound: LinkedList<String> = LinkedList()
    val insertions = mutableMapOf<String, String>()
    input.forEach { line ->
        if (line.isNullOrBlank()) return@forEach
        if (line.contains("->")) {
            insertions[line.split("->").first().trim()] = line.split("->").last().trim()
        } else {
            compound += line.map { it.toString() }
        }
    }
    println("Initial compound: $compound")

    for (i in 1 until compound.count()) {
        val elements = "${compound[i-1]}${compound[i]}"
        if (currentPairs[elements] == null) {
            currentPairs[elements] = 1
        } else {
            currentPairs[elements] = currentPairs[elements]!! + 1
        }
    }

    println("Pairs: $currentPairs")
    println("Insertions: $insertions")

    repeat(40) {
        currentPairs = mutableMapOf<String, Long>().withDefault { 0 }.apply {
            currentPairs.forEach {
                val elem = it.key
                val count = it.value
                val insertInBetweenValue = insertions[elem]
                val first = "${elem[0]}$insertInBetweenValue"
                val second = "$insertInBetweenValue${elem[1]}"
                put(first, getValue(first) + count)
                put(second, getValue(second) + count)
            }
        }
        println("After run $it:")
        println("Pairs: $currentPairs")
    }

    val charCount = mutableMapOf<String, Long>().withDefault { 0 }.apply {
        currentPairs.entries.forEach { (elem, count) ->
            put(elem[0].toString(), getValue(elem[0].toString()) + count)
            put(elem[1].toString(), getValue(elem[1].toString()) + count)
        }
        put(compound.first, getValue(compound.first) + 1)
        put(compound.last, getValue(compound.last) + 1)
    }.mapValues { it.value / 2 }
    val max = charCount.maxByOrNull { it.value }!!.value
    val min = charCount.minByOrNull { it.value }!!.value

    println("CharCount: $charCount")
    println("$max - $min = ${max - min}")
}
