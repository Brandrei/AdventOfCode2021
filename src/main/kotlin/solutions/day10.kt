package solutions

import readFile

fun day10() {
    val input = readFile("src/main/assets/day10.txt")
    val opening = listOf("{", "[", "(", "<")
    val closing = listOf("}", "]", ")", ">")
    val points = mapOf(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)
//    println(input)
    var total = 0
    input.forEach { line ->
        println("Calculating for $line")
        val stack: Stack<Char> = mutableListOf()
        for (i in 0 until line.length) {
            println("Current stack: $stack, checking for ${line[i]}")
            if (opening.contains(line[i].toString())) stack.push(line[i])
            if (closing.contains(line[i].toString())) {
                println("Is closing")
                val top = stack.peek().toString() ?: break
                if (line[i].toString().inverts(top)) {
                    stack.pop()
                } else {
                    println("doesn't invert")
                    total += points[line[i].toString()]!!
                    println("Found error at $i -> ${line[i]} with value: ${points[line[i].toString()]!!}")
                    break
                }
            }
        }
    }
    println("Total: $total")
}

private fun String.inverts(other: String): Boolean {
    if (this == "}" && other == "{") return true
    if (this == "]" && other == "[") return true
    if (this == ")" && other == "(") return true
    if (this == ">" && other == "<") return true
    return false
}


/**
 * Stack as type alias of Mutable List
 */
typealias Stack<T> = MutableList<T>

/**
 * Pushes item to [Stack]
 * @param item Item to be pushed
 */
inline fun <T> Stack<T>.push(item: T) = add(item)

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
