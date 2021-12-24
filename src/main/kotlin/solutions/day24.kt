package solutions

import readFile
import kotlin.system.measureTimeMillis

fun day24() {
    val input = readFile("src/main/assets/day24.txt")

    println(input)
    val t = measureTimeMillis {
        var found = false
        var modelNos = buildList()
        println("Options size: ${modelNos.size}")
        var min = Int.MAX_VALUE

        for (number in modelNos) {
                val result = solveForInput(input, number.map { it.digitToInt() })
//                print("$number -> ")
            if (result.second < min) {
                min = result.second
                println("Found min $min for $number")
            }
                if (result.first) break
            }
        }
    println("Execution time in s: ${t / 1000L}")
}

fun buildList(): List<String> {
    val result = mutableListOf<String>()
    (3 downTo 1).forEach { n1 ->
        (9 downTo 6).forEach { n2 ->
            (9 downTo 8).forEach { n3 ->
                (9 downTo 4).forEach { n6 ->
                    (8 downTo 1).forEach { n7 ->
                        (4 downTo 1).forEach { n9 ->
                            (9 downTo 1).forEach { n11 ->
                                val newNumber = "$n1$n2$n3" +
                                        "${n3-7}${n2-5}$n6" +
                                        "$n7${n7+1}$n9" +
                                        "${n9+6}$n11$n11" +
                                        "${n6-3}${n1+6}"
                                result.add(newNumber)
                            }
                        }
                    }

                }
            }
        }
    }
//    (1..9).forEach { n1 ->
//        (6..9).forEach { n2 ->
//            (8..9).forEach { n3 ->
//                (4..6).forEach { n6 ->
//                    (1..8).forEach { n7 ->
//                        (1..3).forEach { n9 ->
//                            (1..9).forEach { n11 ->
//                                val newNumber = "$n1$n2$n3" +
//                                        "${n3-7}${n2-5}$n6" +
//                                        "$n7${n7+1}$n9" +
//                                        "${n9+6}$n11$n11" +
//                                        "${n6-3}${n6+3}"
//                                result.add(newNumber)
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
//    }

    return result
}

private fun solveForInput(input: List<String>, inputNumbers: List<Int>): Pair<Boolean, Int> {
    val registry = Array(4) { 0 }
    var inputIndex = 0
    input.forEach { line ->
        val instr = line.split(" ")
        if (instr.size == 2) {
            registry[varToIndex(instr[1])] = inputNumbers[inputIndex]
            inputIndex++
        } else {
            try {
                val seconNumber = instr.last().toInt()
                registry[varToIndex(instr[1])] = performOperation(
                    instr.first(),
                    registry[varToIndex(instr[1])],
                    seconNumber)
            } catch (_: Exception) {
                registry[varToIndex(instr[1])] = performOperation(
                    instr.first(),
                    registry[varToIndex(instr[1])],
                    registry[varToIndex(instr[2])])
            }
        }
    }
//    println("Found no: $inputNumbers is ${registry.toList()}")
    if (registry[varToIndex("z")] == 0) {
        println("Found no: $inputNumbers is ${registry.toList()}")
        return true to registry[3]
    }
    return false to registry[3]
}

private fun varToIndex(c: String): Int {
    return when (c) {
        "w" -> 0
        "x" -> 1
        "y" -> 2
        "z" -> 3
        else -> throw Exception("No registry for $c")
    }
}

fun performOperation(operation: String, var1: Int, var2: Int): Int {
    return when (operation) {
        "inp" -> var1
        "add" -> var1 + var2
        "mul" -> var1 * var2
        "div" -> var1 / var2
        "mod" -> var1 % var2
        "eql" -> if (var1 == var2) 1 else 0
        else -> throw Exception("Unsupported operation $operation")
    }
}

//Ended up solving it in Excel, wiht the following format:
//
//z                                         value       possible values
//w1+9			                                1	    1-3
//(w1+9)*26+(w2+4)			                    6	    6-9
//(w1+9)*26*26+(w2+4)*26+(w3+2)			        8	    8-9
//(w1+9)*26+(w2+4)		                        w3-7	1
//(w1+9)		                                w2-5	1
//(w1+9)*26+(w6+6)			                    4	    4-9
//(w1+9)*26*26+(w6+6)*26*26+(w7+11)			    1   	1-8
//(w1+9)*26+(w6+6)*26		                    w7+1	2
//(w1+9)*26*26+(w6+6)*26*26 + (w9 + 7)			1	    1-4
//(w1+9)*26+(w6+6)*26		                    w9+5	6
//(w1+9)*26*26+(w6+6)*26*26 + (W11+15)			1	    1-9
//(w1+9)*26+(w6+6)*26		                    w11	    1
//(w1+9)+(w6+6)		                            w6-3	1
//0		                                        w1+6	7
