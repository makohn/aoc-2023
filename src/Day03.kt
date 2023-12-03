fun main() {

    val day = "03"

    fun Char.isSymbol(): Boolean {
        return !this.isDigit() && this != '.'
    }

    fun part1(input: List<String>): Int {
        val schematic = input.toCharMatrix()
        val partNumbers = ArrayList<Int>()

        var number = ""
        var isPartNumber = false

        fun reset() {
            if (number.isNotEmpty() && isPartNumber) {
                partNumbers.add(number.toInt())
            }
            number = ""
            isPartNumber = false
        }

        for ((i, row) in schematic.withIndex()) {
            for ((j, char) in row.withIndex()) {
                when (char) {
                    in '0'..'9' -> {
                        number += char
                        if (schematic.adjacentTo(i, j).any { it.data.isSymbol() }) isPartNumber = true
                    }
                    else -> reset()
                }
            }
            reset()
        }

        return partNumbers.sum()
    }

    fun part2(input: List<String>): Int {
        val schematic = input.toCharMatrix()
        val gearPositions = HashSet<CharCell>()
        val gearCandidates = HashMap<CharCell, MutableList<Int>>()

        var number = ""

        fun reset() {
            if (number.isNotEmpty()) {
                for (pos in gearPositions) {
                    gearCandidates.putIfAbsent(pos, ArrayList())
                    gearCandidates[pos]?.add(number.toInt())
                }
                number = ""
                gearPositions.clear()
            }
        }

        for ((i, row) in schematic.withIndex()) {
            for ((j, char) in row.withIndex()) {
                when (char) {
                    in '0'..'9' -> {
                        number += char
                        gearPositions.addAll(schematic
                            .adjacentTo(i, j)
                            .filter { it.data == '*' }
                            .toList())
                    }
                    else -> reset()
                }
            }
            reset()
        }

        return gearCandidates
            .map { it.value }
            .filter { it.size == 2 }
            .sumOf { it.product() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}