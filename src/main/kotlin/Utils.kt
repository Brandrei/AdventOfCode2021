import java.io.File

fun readFile(filename: String) : List<String> {
    val lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach { lineList.add(it) }}
    return lineList
}

fun readFileToInt(filename: String) : List<Int> = readFile(filename).map { it.toInt() }
