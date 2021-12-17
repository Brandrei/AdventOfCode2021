package solutions

import kotlin.math.max

const val txMin = 265
const val txMax = 287
const val tyMin = -103
const val tyMax = -58
//const val txMin = 20
//const val txMax = 30
//const val tyMin = -10
//const val tyMax = -5

fun day17() {
    var max = 0
    val goodShots = mutableListOf<Pair<Int, Int>>()
    for (i in 1..txMax) {
        for (j in -200..200) {
            val height = getMaxHeight(i, j)
            if (height > -10000000) goodShots.add(i to j)
//            println("Found height $height for $i - $j")
            if ( height > max) {
                max = height
                println("Found new max height $max for $i - $j")
            }
        }
    }
    println(goodShots)
    println(goodShots.size)
}

private fun getMaxHeight(dirX: Int, dirY: Int): Int {
//    println("Checking for $dirX, $dirY")
    var posX = 0
    var posY = 0
    var maxHeight = 0
    var step = 0
    while (posX < txMax && posY > tyMin) {
        posX += max(0, dirX - step)
        posY += dirY - step
//        println("new position: $posX, $posY, maxH: $maxHeight")
        if (posY > maxHeight) maxHeight = posY
        if (inTarget(posX, posY)) {
//            println("In target for $dirX, $dirY")
            return maxHeight
        }
        step++
    }
    return -10000000
}

private fun inTarget(x: Int, y: Int) = x in txMin..txMax && y in tyMin.. tyMax
