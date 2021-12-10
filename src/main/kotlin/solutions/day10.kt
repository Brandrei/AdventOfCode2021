package solutions

import readFile

fun day10() {
    val input = readFile("src/main/assets/day10.txt")
    val opening = listOf("{", "[", "(", "<")
    val closing = listOf("}", "]", ")", ">")
    val points = mapOf(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)
//    println(input)
    var total = 0
    val completionsScore = mutableListOf<Long>()
    input.forEach { line ->
        val stack: Stack<String> = mutableListOf()
        var broken = false
        for (i in 0 until line.length) {
            if (opening.contains(line[i].toString())) stack.push(line[i].toString())
            if (closing.contains(line[i].toString())) {
                val top = stack.peek().toString()
                if (line[i].toString().inverts(top)) {
                    stack.pop()
                } else {
                    broken = true
                    total += points[line[i].toString()]!!
                    break
                }
            }
        }
        if (!broken && stack.size > 0) {
//            println("Stack after for: $stack")
//            println("Found incomplete line: $line with stack: $stack")
            val score = caclulateScoreFor(stack)
//            println("Score = $score")
            completionsScore.add(score)
        }

    }
    println("Total: $total")
    completionsScore.sort()
    println("Scores: $completionsScore \n" +
            "Scores size: ${completionsScore.size} and mid at ${completionsScore.size / 2}" +
            "with mid at ${completionsScore[completionsScore.size/2 ]}")
}

fun caclulateScoreFor(stack: MutableList<String>): Long {
    var score = 0L
    while (stack.size > 0) {
        score *= 5
        val element = stack.pop()
        println("$element")
        if (")".inverts(element!!)) score += 1
        if ("]".inverts(element)) score += 2
        if ("}".inverts(element)) score += 3
        if (">".inverts(element)) score += 4
    }
    return score
}

private fun String.inverts(other: String): Boolean {
    var result = false
    if (this == "}" && other == "{") result = true
    if (this == "]" && other == "[") result = true
    if (this == ")" && other == "(") result = true
    if (this == ">" && other == "<") result = true
    return result
}


/**
 * Stack as type alias of Mutable List
 */
typealias Stack<T> = MutableList<T>

/**
 * Pushes item to [Stack]
 * @param item Item to be pushed
 */
fun <T> Stack<T>.push(item: T) = add(item)

/**
 * Pops (removes and return) last item from [Stack]
 * @return item Last item if [Stack] is not empty, null otherwise
 */
fun <T> Stack<T>.pop(): T? = if (isNotEmpty()) removeAt(lastIndex) else null

/**
 * Peeks (return) last item from [Stack]
 * @return item Last item if [Stack] is not empty, null otherwise
 */
fun <T> Stack<T>.peek(): T? = if (isNotEmpty()) this[lastIndex] else null
