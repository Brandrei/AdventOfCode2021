package solutions

import readFileToInt

fun day1() {
    val input = readFileToInt("src/main/assets/day1.txt")
    var count = 0
    input.forEachIndexed{ index, value ->
        if (index > 0 && value > input[index - 1]) count ++
    }

    var count2 = 0
    for (index in 3 until input.size) {
        val sumPrev = input[index - 3] + input[index -2] + input[index-1]
        val sum = input[index - 2] + input[index -1] + input[index]
        if (sum > sumPrev) count2++
    }

    println("Result: $count")
    println("Result part2: $count2")
}