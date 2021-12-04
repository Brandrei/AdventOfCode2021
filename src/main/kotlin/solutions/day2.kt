package solutions

import readFile

fun day2() {
    val input = readFile("src/main/assets/day2.txt")
    //part 1
    var depth = 0
    var length = 0
    input.forEach {
        val (direction, value) = it.split(' ')
        if (direction == "forward") length += value.toInt()
        if (direction == "up") depth += value.toInt()
        if (direction == "down") depth -= value.toInt()
    }

    println("Length: $length, Depth: $depth, Product: ${depth*length}")

    //part 2
    depth = 0
    length = 0
    var aim = 0
    input.forEach {
        val (direction, value) = it.split(' ')
        println("$direction -> $value")
        if (direction == "forward") {
            length += value.toInt()
            depth += value.toInt() * aim
        }
        if (direction == "up") aim -= value.toInt()
        if (direction == "down") aim += value.toInt()
        println("Current length: $length, Current depth: $depth, Current aim: $aim")
    }

    println("Length: $length, Depth: $depth, Product: ${depth*length}")
}
