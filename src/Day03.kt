fun main() {

    val day = "03"

    fun Char.isSymbol(): Boolean {
        return !this.isDigit() && this != '.'
    }

    fun part1(input: List<String>): Int {
        val partNumbers = mutableListOf<Int>()
        val n = input.size
        val m = input[0].length
        val map = Array(n) { CharArray(m) }
        for ((i, line) in input.withIndex()) {
            for ((j, c) in line.withIndex()) {
                map[i][j] = c
            }
        }

        for ((a, row) in map.withIndex()) {
            var isPartNumber = false
            var number = ""
            for ((b, col) in row.withIndex()) {
                when (col) {
                    in '0'..'9' -> {
                        number += col
                        for (i in -1..1) for (j in -1..1) {
                            val x = a + i
                            val y = b + j
                            if (x in 0..<n && y in 0..<m) if (map[x][y].isSymbol()) isPartNumber = true
                        }
                    }
                    else -> {
                        if (number.isNotEmpty() && isPartNumber) {
                            partNumbers.add(number.toInt())
                        }
                        number = ""
                        isPartNumber = false
                    }
                }
            }
            if (number.isNotEmpty() && isPartNumber) {
                partNumbers.add(number.toInt())
            }
        }

        return partNumbers.sum()
    }

    fun part2(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        val map = Array(n) { CharArray(m) }
        for ((i, line) in input.withIndex()) {
            for ((j, c) in line.withIndex()) {
                map[i][j] = c
            }
        }

        val gearPos = mutableSetOf<Pair<Int, Int>>()
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        for ((a, row) in map.withIndex()) {
            var number = ""
            for ((b, col) in row.withIndex()) {
                when (col) {
                    in '0'..'9' -> {
                        number += col
                        for (i in -1..1) for (j in -1..1) {
                            val x = a + i
                            val y = b + j
                            if (x in 0..<n && y in 0..<m) {
                                if (map[x][y] == '*') gearPos.add(x to y)
                            }
                        }
                    }
                    else -> {
                        if (number.isNotEmpty()) {
                            for (pos in gearPos) {
                                gears.putIfAbsent(pos, mutableListOf())
                                gears[pos]?.add(number.toInt())
                            }
                            number = ""
                            gearPos.clear()
                        }
                    }
                }
            }
            if (number.isNotEmpty()) {
                for (pos in gearPos) {
                    gears.putIfAbsent(pos, mutableListOf())
                    gears[pos]?.add(number.toInt())
                }
                gearPos.clear()
            }
        }

        println(gears)
        val ans = gears.filter { (_, v) -> v.size == 2 }.map { (_, v) -> v.reduce { p1, p2 -> p1 * p2 } }.sum()
        println(ans)
        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}