import java.io.File

fun readFile(filename: String) : List<String> {
    val lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach { lineList.add(it) }}
    return lineList
}

fun readFileToInt(filename: String) : List<Int> = readFile(filename).map { it.toInt() }


fun Array<IntArray>.print() {
    this.forEach { rows ->
        rows.forEach { print("$it ") }
        println()
    }
    println()
}

fun Array<CharArray>.print() {
    this.forEach { rows ->
        rows.forEach { print("$it ") }
        println()
    }
    println()
}
