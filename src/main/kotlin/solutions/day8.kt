package solutions

import readFile

fun day8() {
    val input = readFile("src/main/assets/day8.txt")
//    println(input)
    val inputPart1 = input.map { it.split(" | ")[1].split(" ") }
    println("Input part1: $inputPart1")
    val knownNumbers = listOf(2, 4, 3, 7)
    var total = 0
    inputPart1.flatten().forEach {
        if (it.length in knownNumbers) total++
    }
    println("Total: $total")

    total = 0
    input.forEach { line ->
        val lineInput = line.split(" | ")[0].split(" ")
        println("Input: ${lineInput.map { it.sort() }}")
        val mapping = createMappingFor(lineInput)
        println(mapping)

        val output = line.split(" | ")[1].split(" ")
        var sum = ""
        output.forEach { outputNumber ->
            sum += mapping.indexOf(outputNumber.sort())
            println("${outputNumber.sort()}, $sum")
        }
        println("sum for output $output is $sum")
        total += sum.toInt()
        sum = ""
        println("total: $total")
    }
    println("total: $total")


}

fun createMappingFor(input: List<String>): List<String> {
    val result = mutableListOf<String>()
    repeat(10) { result.add("") }

    while (result.hasEmptyPositions()) {
        input.forEach {
            when (it.length) {
                2 -> result[1] = it
                3 -> result[7] = it
                4 -> result[4] = it
                7 -> result[8] = it
                5 -> {
                    if (it.toList().intersect(result[1].toList()).size == 2) {
                        result[3] = it // a 3
                    } else if (it.toList().intersect(result[4].toList()).size == 3) {
                        result[5] = it // a 5
                    } else {
                        result[2] = it // a 2
                    }
                }
                6 -> {
                    if (it.toList().intersect(result[1].toList()).size < 2) {
                        result[6] = it // a 6
                    } else if (it.toList().intersect(result[4].toList()).size == 4) {
                        result[9] = it // a 9
                    } else {
                        result[0] = it // a 0
                    }
                }
            }

        }
    }
    return result.map { it.sort() }
}

fun List<String>.hasEmptyPositions() = filter { it.length == 0 }.size > 0

fun String.sort() = this.toCharArray().sorted().joinToString("")