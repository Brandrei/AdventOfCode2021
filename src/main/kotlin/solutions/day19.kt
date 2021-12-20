package solutions

import readFile
import kotlin.math.abs

fun day19() {
//    testMapping()
//    return
    val input = readFile("src/main/assets/day19.txt")
    println(input)
    val scanners = mutableListOf<Scanner>()
    var currentId = 0
    val beacons = mutableListOf<Beacon>()
    input.forEach { line ->
        when {
            line.isBlank() -> {
            }
            line.startsWith("---") -> {
                scanners.add(Scanner(currentId, beacons.toList()))
                currentId++
                beacons.removeAll { true }
            }
            else -> {
                val split = line.split(",").map { it.toInt() }
                beacons.add(Beacon(split[0], split[1], split[2]))
            }
        }
    }
    scanners.add(Scanner(currentId, beacons))
//    println(scanners)
    scanners[0] = scanners[0].copy(relativePosition = RelativePosition(0, 0, 0))
    val unmatched = scanners.toMutableList()
    unmatched.remove(scanners[0])
    val matched = mutableListOf(scanners[0])
    while (unmatched.size > 0) {
        for (unMatchedScanner in unmatched.toList()) {
            for (matchedScanner in matched.toList()) {
//                println("Mapping ${unMatchedScanner.id} with ${matchedScanner.id}")

                val aligned = align(matchedScanner, unMatchedScanner)
                if (aligned!= null) {
//                    println("Matched src: ${unMatchedScanner.id} at ${aligned.relativePosition}")
                    unmatched.remove(unMatchedScanner)
                    matched.add(aligned)
                    continue
                }
            }
        }
    }

    println("Unmatched: ${unmatched.map { it.id }}")
    println("Matched: ${matched.map { "${it.id} Pos ${it.relativePosition}" }}")
    val uniqueBeacons = mutableSetOf<Beacon>()
    matched.forEach { scn ->
        uniqueBeacons.addAll(scn.beacons)
    }
    println("Total beacons: ${uniqueBeacons.size}")

    var maxDist = 0
    for (i in 0 until matched.size) {
        for (j in i until matched.size) {
            val dist = manhattanDist(matched[i].relativePosition!!, matched[j].relativePosition!!)
            if (dist > maxDist) maxDist = dist
        }
    }

    println("Maximum distance: $maxDist")

}

fun manhattanDist(pos1: RelativePosition, pos2: RelativePosition): Int {
    return abs(pos1.x - pos2.x) + abs(pos1.y - pos2.y) + abs(pos1.z - pos2.z)
}

fun align(source: Scanner, target: Scanner): Scanner? {
    for (perm in 0..5) {
        for (neg in negatives) {
            val newTarget = Scanner(
                target.id, beacons = target.beacons.map {
                    it.applyRotation(perm, neg)
                }
            )
//            println("Scanner ${target.id} rotation $perm -> $neg")
            val mapped = mutableMapOf<String, Int>()
            for (srcBeacon in source.beacons) {
                for (trgBeacon in newTarget.beacons) {
                    val newX = srcBeacon.x - trgBeacon.x
                    val newY = srcBeacon.y - trgBeacon.y
                    val newZ = srcBeacon.z - trgBeacon.z
                    val key = "$newX,$newY,$newZ"
                    if (mapped[key] == null) {
                        mapped[key] = 1
                    } else {
                        mapped[key] = mapped[key]!! + 1
                    }
                }
            }
//            println("Mapped points: ${mapped.map { it.value > 1 }}")
            mapped.forEach { (key, value) ->
                if (value >= 12) {
//                    println("aligned $perm - $neg")
                    return alignScannerToPoint(newTarget, key)
                }
            }
        }
    }
    return null
}

fun alignScannerToPoint(toAlign: Scanner, posDiff: String): Scanner {
    val diff = posDiff.split(",").map { it.toInt() }.toMutableList()
    return Scanner(
        id = toAlign.id,
        beacons = toAlign.beacons.map {
            Beacon(it.x + diff[0], it.y + diff[1], it.z + diff[2])
        },
        relativePosition = RelativePosition(diff[0], diff[1], diff[2])
    )
}

val negatives = listOf(
    Triple(1,1,1),
    Triple(1, 1, -1),
    Triple(1, -1, -1),
    Triple(1, -1, 1),
    Triple(-1, 1, -1),
    Triple(-1, -1, -1),
    Triple(-1, -1, 1),
    Triple(-1, 1, 1)
)

data class Beacon(val x: Int, val y: Int, val z: Int) {
    fun applyRotation(permutation: Int, negatives: Triple<Int, Int, Int>): Beacon {
        return when (permutation) {
            0 -> Beacon(x * negatives.first, y * negatives.second, z * negatives.third)
            1 -> Beacon(x * negatives.first, z * negatives.second, y * negatives.third)
            2 -> Beacon(y * negatives.first, x * negatives.second, z * negatives.third)
            3 -> Beacon(y * negatives.first, z * negatives.second, x * negatives.third)
            4 -> Beacon(z * negatives.first, x * negatives.second, y * negatives.third)
            5 -> Beacon(z * negatives.first, y * negatives.second, x * negatives.third)
            else -> throw Exception("Invalid permutation")
        }
    }
}

data class Scanner(
    val id: Int,
    val beacons: List<Beacon>,
    val relativePosition: RelativePosition? = null
) {
    override fun toString() = "\nScanner: $id Pos $relativePosition"

}

data class RelativePosition(val x: Int, val y: Int, val z: Int)

fun testMapping() {
    val source = Scanner(
        0, listOf(
            Beacon(5, 5, 5), Beacon(6, 6, 6), Beacon(7, 7, 7)
        )
    )
    val target = Scanner(
        0, listOf(
            Beacon(4, 4, 4), Beacon(5, 5, 5), Beacon(6, 6, 6)
        )
    )
    val mapped = mutableMapOf<String, Int>()
    for (srcBeacon in source.beacons) {
        for (trgBeacon in target.beacons) {
            val newX = srcBeacon.x - trgBeacon.x
            val newY = srcBeacon.y - trgBeacon.y
            val newZ = srcBeacon.z - trgBeacon.z
            val key = "$newX,$newY,$newZ"
            if (mapped[key] == null) {
                mapped[key] = 1
            } else {
                mapped[key] = mapped[key]!! + 1
            }
            println(mapped)
        }
    }
}

//1 at 68,-1246,-43 (relative to scanner 0)
//3 at -92,-2380,-20
//4 at -20,-1133,1061
//2 at 1105,-1205,1229
