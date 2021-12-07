package solutions

import readFile
import kotlin.math.abs

fun day7() {
    val input = readFile("src/main/assets/day7.txt")[0].split(",").map { it.toInt() }
    val min = input.minOrNull()
    val max = input.maxOrNull()
    var minFuel = Int.MAX_VALUE
    (min!!..max!!).forEach {
        val fuel = calculateFuelForInput(input, it)
        if (fuel < minFuel) {
            minFuel = fuel
            println("MinFuel found to be $minFuel at position: $it")
        }
    }
}

fun calculateFuelForInput(input: List<Int>, targetPosition: Int): Int {
    var result = 0
    input.forEach {
        val dif = abs(it - targetPosition)
        result += (dif)*(dif+1)/2
//        println("Result for $it and targetPos: $targetPosition = $result")
    }
    return result
}
