package solutions;

import readFile

fun day12() {
    val input = readFile("src/main/assets/day12.txt")
    val graph = Graph<Cave>()
    var start: Cave? = null
    var end: Cave? = null
    input.forEach { line ->
        val src = line.split("-").first().trim()
        val dest = line.split("-").last().trim()
        val firstCave = Cave(src, src[0].isUpperCase())
        if (src == "start") start = firstCave
        if (src == "end") end = firstCave
        val secondCave = Cave(dest, dest[0].isUpperCase())
        if (dest == "start") start = secondCave
        if (dest == "end") end = secondCave
        graph.addEdge(firstCave, secondCave)
    }
//    println("Graph: $graph")
    val paths = graph.findPathsBetween(start!!, end!!)
//    println("Total paths: ${paths}")
    println("Total paths: ${paths.count()}")
}

data class Cave(val name: String, val isLarge: Boolean)

class Graph<T> {
    val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()

    fun addEdge(sourceVertex: T, destinationVertex: T) {
        // Add edge to source vertex / node.
        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(destinationVertex)
        // Add edge to destination vertex / node.
        adjacencyMap
            .computeIfAbsent(destinationVertex) { HashSet() }
            .add(sourceVertex)
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()

    val solutions = mutableSetOf<String>()

    fun findPathsBetween(start: T, end: T): Set<String> {
//        println("Finding paths between $start and $end")
        solutions.clear()
        findPaths(start, end, "start", mutableListOf(start as Cave), false)
        return solutions
    }

    fun findPaths(start: T, end: T, currentPath: String, visited: List<Cave>, didVisitTwice: Boolean) {
//        println("FindPaths: $start -> $end")
//        println("CurrentPath: $currentPath")
//        println("Solutions: $solutions")
        if (start == end) {
//            println("Start is end")
            solutions.add(currentPath)
            return
        }
        adjacencyMap[start]?.forEach { nextCave ->
            val cave = nextCave as Cave
//            println("Trying next cave at $cave")
            var doubleVisit = didVisitTwice
            val canVisit = if (cave.isLarge) {
                true
            } else if (cave.name.equals("start")) {
                false
            } else if (visited.contains(cave) && !didVisitTwice){
                doubleVisit = true
                true
            } else {
                !visited.contains(cave)
            }
            if (canVisit) {
                println(visited)
//                println("can visit: $nextCave, doubleVisit: $doubleVisit")
                val newv = visited.map { it } + cave
                findPaths(nextCave, end, "$currentPath,${cave.name}", newv, doubleVisit)
            }
        }
    }

}
