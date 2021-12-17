package solutions

import readFile
import java.math.BigInteger

fun day16() {
    val input = readFile("src/main/assets/day16.txt")
    val listInBinary = input.first().map { char ->
        Integer.toBinaryString(Integer.parseInt(char.toString(), 16)).padStart(4, '0')
    }.joinToString("")
    println(input)
    println(listInBinary)
    val versions = mutableListOf<Int>()
    val protocols = mutableListOf<Int>()
    val literalValues = mutableListOf<BigInteger>()
    val packets = mutableListOf<Packet>()
    var index = 0
    while (index < listInBinary.length) {
        if (index > listInBinary.length - 7) break
        //starting new packet
        val version = Integer.parseInt(listInBinary.substring(index until index + 3), 2)
        versions.add(version)
        index += 3
//        println("Version: $version")
        val protocol = Integer.parseInt(listInBinary.substring(index until index + 3), 2)
        index += 3
//        println("Protocol $protocol")
        protocols.add(protocol)
        if (protocol == 4) {
            //parse literal
            var literal = ""
            while (listInBinary[index] == '1') {
                index++
                literal += listInBinary.substring(index, index + 4)
                index += 4
            }
            index++
            literal += listInBinary.substring(index, index + 4)
            index += 4
//            println("Literal: $literal")

            literalValues.add(BigInteger(literal, 2))
            packets.add(Packet(version, protocol, literal.length, BigInteger(literal, 2).toLong(), mutableListOf()))
        } else {
            if (listInBinary[index] == '0') {
                index++
                val packetsBinary = listInBinary.substring(index, index + 15)
                val packetLength = Integer.parseInt(packetsBinary, 2)
//                println("Number of packets in subgroup = $packetsBinary -> $packetLength")
                packets.add(Packet(version, protocol, packetLength, 0, mutableListOf()))
                index += 15
            } else {
                index++
                val packetsBinary = listInBinary.substring(index, index + 11)
                val noOfPackets = Integer.parseInt(packetsBinary, 2)
//                println("Number of packets in subgroup = $packetsBinary -> $packetLength")
                packets.add(Packet(version = version,
                    protocol = protocol,
                    length = 0,
                    value = 0,
                    subPackets = mutableListOf(),
                    noOfSubPackets = noOfPackets))
                index += 11
            }
        }
//        println("Index after last packet: $index")

    }
    println("Versions: $versions with sum ${versions.sum()}")
    println("Protocols: $protocols")
    println("Literals: $literalValues")
    println("Input lenght: ${listInBinary.length}")
    println("Packets: $packets")

//    println("Part2 ${buildTreeFromString(listInBinary)}")
}

var newIndex = 0

//fun parseInputAndEval(inputList: String, startIndex: Int): List<Long> {
////    if (startIndex + length > inputList.length) return listOf(0L)
//    val result = mutableListOf<Long>()
//    var index = startIndex
//    val length = inputList.length
//    while (index < length - 6) {
//        index += 3
//        val protocol = Integer.parseInt(inputList.substring(index, index + 3), 2)
//        index += 3
//        if (protocol == 4) {
//            var literal = ""
//            while (inputList[index] == '1') {
//                index++
//                literal += inputList.substring(index, index + 4)
//                index += 4
//            }
//            index++
//            literal += inputList.substring(index, index + 4)
//            index += 4
//            val litInt = BigInteger(literal, 2).toLong()
//            println("Found literal $litInt, index at $index")
//            result.add(litInt)
//        } else {
//            val longPacket = inputList[index] == '0'
//            val lenghtInBits = if (longPacket) 15 else 11
//            index++
//            val packetsBinary = inputList.substring(index, index + lenghtInBits)
//            val packetLength = Integer.parseInt(packetsBinary, 2)
////            println("Number of packets in subgroup = $packetsBinary -> $packetLength")
//            index += lenghtInBits
//            result.add(
//                calculateFor(
//                    protocol,
//                    parseInputAndEval(
//                        inputList.substring(index, if (longPacket) packetLength else inputList.length),
//                        0
//                    )
//                )
//            )
//            val addToIndex = if (lenghtInBits == 15) packetLength else newIndex
//            index += addToIndex
//        }
//    }
//    newIndex += index
//    return result
//}

fun buildTreeFromString(input: String): Packet? {
    if (input.length < 6) return null
    var version = 0
    var protocol = 0
    var subPackets = mutableListOf<Packet>()
    var index = 0
    while (index < input.length - 6) {
        val vers = Integer.parseInt(input.substring(index until index + 3), 2)
        version = vers
        index += 3
        val proto = Integer.parseInt(input.substring(index until index + 3), 2)
        index += 3
        protocol = proto
        if (protocol == 4) {
            var literal = ""
            while (input[index] == '1') {
                index++
                literal += input.substring(index, index + 4)
                index += 4
            }
            index++
            literal += input.substring(index, index + 4)
            index += 4
            val litInt = BigInteger(literal, 2).toLong()
            println("Found literal $litInt, index at $index")
            val res = Packet(version, protocol, index, litInt, mutableListOf())
            println("Built: $res")
            return res
        } else {
            if (input[index] == '0') {
                index++
                val packetsBinary = input.substring(index, index + 15)
                val packetLength = Integer.parseInt(packetsBinary, 2)
                index += 15
                var taken = 0
                while (taken < packetLength) {
                    val packet = buildTreeFromString(input.substring(index, packetLength))!!
                    packet.value = packet.eval()
                    subPackets.add(packet)
                    taken = packet.length
                    index += packet.length
                }
            } else {
                index++
                val noOfPacketsBinary = input.substring(index, index + 11)
                val numberOfPackets = Integer.parseInt(noOfPacketsBinary, 2)
//                println("Number of packets in subgroup = $packetsBinary -> $packetLength")
                index += 11
                repeat(numberOfPackets) {
                    val packet = buildTreeFromString(input.substring(index, input.length))!!
                    packet.value = packet.eval()
                    subPackets.add(packet)
                    index += packet.length
                }
            }
        }

    }
    val res = Packet(version, protocol, index, 0, subPackets)
    res.value = res.eval()
    println("Built: $res")
    return res
}

class Packet(
    val version: Int,
    val protocol: Int,
    val length: Int,
    var value: Long,
    val subPackets: MutableList<Packet>,
    val noOfSubPackets: Int = 0
) {

    fun eval(): Long {
        var result: Long? = null
        if (value!= 0L) result = value
        print("Calculating op $protocol for $subPackets")
        result = when (protocol) {
            0 -> subPackets.map { it.eval() }.sum()
            1 -> subPackets.map { it.eval() }.reduce { acc, l -> acc * l }
            2 -> subPackets.map { it.eval() }.minOrNull()!!
            3 -> subPackets.map { it.eval() }.maxOrNull()!!
            4 -> value
            5 -> if (subPackets[0].eval() > subPackets[1].eval()) 1 else 0
            6 -> if (subPackets[0].eval() < subPackets[1].eval()) 1 else 0
            7 -> if (subPackets[0].eval() == subPackets[1].eval()) 1 else 0
            else -> {
                throw Exception()
            }
        }
        println(" -> $result")
        return result!!
    }

    override fun toString(): String {
        return "Packet type: $protocol, length: $length, value: $value, childrens: $noOfSubPackets" +
                "children: {  ${subPackets.map { it.value }}  }"
    }
}

