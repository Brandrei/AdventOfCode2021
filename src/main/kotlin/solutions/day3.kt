package solutions

import readFile

fun day3() {
    val input = readFile("src/main/assets/day3.txt")
    var count = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
    input.forEach { line ->
        line.forEachIndexed { index, c ->
            if (c == '1') count[index]++
        }
    }
    println("Count: $count")
    val gamma = count.map { if (it > (input.size / 2)) 1 else 0 }.joinToString(separator = "")
    val epsilon = count.map { if (it < (input.size / 2)) 1 else 0 }.joinToString(separator = "")
    println("Gamma: $gamma = ${gamma.toInt(2)}, Epsilon: $epsilon = ${epsilon.toInt(2)}")
    println("Product = ${gamma.toInt(2)*epsilon.toInt(2)}")

    var ogr = input
    var csr = input
    var index = 0
    while (ogr.size > 1 && index < 12) {
        count = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        ogr.forEach { line ->
            line.forEachIndexed { index, c ->
                if (c == '1') count[index]++
            }
        }
        ogr = ogr.filter {
            it[index] == if (count[index] >= (ogr.size / 2 + ogr.size % 2)) '1' else '0'
        }
        println("Count = $count")
        println("I = $index Ogr = $ogr")

        index++
    }
    println("\n___________\n")
    index = 0
    while (csr.size > 1 && index < 12) {
        count = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        csr.forEach { line ->
            line.forEachIndexed { index, c ->
                if (c == '1') count[index]++
            }
        }
        csr = csr.filter {
            it[index] == if (count[index] < (csr.size / 2 + csr.size % 2)) '1' else '0'
        }

        println("Count = $count")
        println("I = $index CSR = $csr")

        index++
    }
    println("Ogr = $ogr = ${ogr.last().toInt(2)}")
    println("CSR = $csr = ${csr.last().toInt(2)}")
    println("Total = ${ogr.first().toInt(2)*csr.first().toInt(2)}")
}
