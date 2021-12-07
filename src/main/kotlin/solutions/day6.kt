package solutions

import readFile

fun day6() {
    val input = readFile("src/main/assets/day6.txt")[0].split(",").map { it.toInt() }
    println(input)
    val days = 80
    var totalFish = 0
    input.forEach {
        totalFish++
        totalFish += calculateAncestors(it, days)
    }
    println("Total: ${simulate(input, 20)}")
}

private fun simulate(values: List<Int>, turns: Int) : Long {
    val fish = values.groupBy { it }.map { (k, v) -> k to v.size.toLong() }.toMap().toMutableMap()
    println(fish)
    repeat(turns) {
        val updates = fish.map { (age, amount) ->
            if(age == 0) (6 to amount) else (age - 1 to amount)
        } + (8 to (fish[0] ?: 0))
        println(updates)
        fish.clear()
        updates.forEach { (age, amount) -> fish[age] = (fish[age] ?: 0) + amount  }
    }

    return fish.values.sum()
}

fun calculateAncestors(currentFishLife: Int, daysLeft: Int): Int {
//    println("calculate $currentFishLife | $daysLeft")
    if (daysLeft < 6 ) return 0
    if (currentFishLife == 0) {
        return 1 + calculateAncestors(6, daysLeft-1) + calculateAncestors(8, daysLeft-1)
    } else {
        return calculateAncestors(currentFishLife - 1, daysLeft - 1)
    }
}

class Fish(var daysToGiveBirth: Int) {
    fun passDay(): Fish? =
        if (daysToGiveBirth-- == 0) {
            daysToGiveBirth = 6
            Fish(8)
        } else {
            null
        }
}

