package solutions

import readFile
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

private const val CUBE_SIZE = 101

fun day22() {
    val input = readFile("src/main/assets/day22.txt")
    println(input)
    val cube = Array(CUBE_SIZE) { Array(CUBE_SIZE) { IntArray(CUBE_SIZE) { 0 } } }
    val instructions = mutableListOf<Instruction>()
    input.forEach { line ->
        val turnOn = line.split(" ")[0] == "on"
        val coords = line.split(" ")[1].split(",")
        val x = coords[0].substring(2).split("..")[0].toInt() + 50..coords[0].substring(2).split("..")[1].toInt() + 50
        val y = coords[1].substring(2).split("..")[0].toInt() + 50..coords[1].substring(2).split("..")[1].toInt() + 50
        val z = coords[2].substring(2).split("..")[0].toInt() + 50..coords[2].substring(2).split("..")[1].toInt() + 50
        instructions.add(Instruction(turnOn, x, y, z))
    }
    println("Instructions: $instructions")
    instructions.forEach { instr ->
        val inRange =
            instr.x.intersect(0..CUBE_SIZE).isNotEmpty() &&
                    instr.y.intersect(0..CUBE_SIZE).isNotEmpty() &&
                    instr.z.intersect(0..CUBE_SIZE).isNotEmpty()

        if (inRange) {
            instr.x.forEach { xInd ->
                instr.y.forEach { yInd ->
                    instr.z.forEach { zInd ->
                        if (inRange(xInd, yInd, zInd)) {
                            cube[xInd][yInd][zInd] = if (instr.on) 1 else 0
                        }
                    }
                }
            }
        }
    }
    val result = getScore(cube)
    println("Part1 result: $result")
    var p2Result: Long
    val t = measureTimeMillis {
        p2Result = part2(instructions)
    }
    println("Part2 result: $p2Result in $t")
}

fun inRange(xInd: Int, yInd: Int, zInd: Int): Boolean {
    return xInd in 0 until CUBE_SIZE && yInd in 0 until CUBE_SIZE && zInd in 0 until CUBE_SIZE
}

private fun part2(instructions: List<Instruction>): Long {
    var cubes = mutableListOf<Cube>()
    instructions.forEach { instr ->
        val newCube = Cube(instr.x.first, instr.x.last, instr.y.first, instr.y.last, instr.z.first, instr.z.last, instr.on)
        val toUpdate = cubes.toMutableList()
        if (instr.on) {
            toUpdate.add(newCube)
        }
        cubes.forEach { oldCube ->
            val xp1 = max(newCube.x0, oldCube.x0)
            val xp2 = min(newCube.x1, oldCube.x1)
            val yp1 = max(newCube.y0, oldCube.y0)
            val yp2 = min(newCube.y1, oldCube.y1)
            val zp1 = max(newCube.z0, oldCube.z0)
            val zp2 = min(newCube.z1, oldCube.z1)
            if (xp1 <= xp2 && yp1 <= yp2 && zp1 <= zp2) {
                toUpdate.add(Cube(xp1, xp2, yp1, yp2, zp1, zp2, !oldCube.on))
            }
        }

        cubes = toUpdate
    }
    var total = 0L
    cubes.forEach { cube ->
        total += (cube.x1 - cube.x0 + 1).toLong() *
                (cube.y1 - cube.y0 + 1).toLong() *
                (cube.z1 - cube.z0 + 1).toLong() *
                (if (cube.on) 1 else -1)
    }
    return total
}

data class Cube(val x0: Int, val x1: Int, val y0: Int, val y1: Int, val z0: Int, val z1: Int, val on: Boolean)

fun getScore(cube: Array<Array<IntArray>>): Int {
    var count = 0
    cube.forEach { x ->
        x.forEach { y ->
            y.forEach { z ->
                count += z
            }
        }
    }
    return count
}

data class Instruction(val on: Boolean, val x: IntRange, val y: IntRange, val z: IntRange)
