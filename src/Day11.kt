import kotlin.math.abs

fun main() {

    val day = "11"

    fun part1(input: List<String>): Int {
        val universe = mutableListOf<String>()
        for (row in input) {
            universe.add(row)
            if ("#" !in row) {
                universe.add(row)
            }
        }
        val input2 = universe.map { it.toList() }.transpose().map { it.joinToString("") }
        val interimUniverse = mutableListOf<String>()
        for (row in input2) {
            interimUniverse.add(row)
            if ("#" !in row) {
                interimUniverse.add(row)
            }
        }
        val expandedUniverse = interimUniverse.map { it.toList() }.transpose().map { it.joinToString("") }

        val galaxies = mutableListOf<Pair<Int, Int>>()

        for ((i, row) in expandedUniverse.withIndex()) {
            for ((j, col) in row.withIndex()) {
                if (col == '#') {
                    galaxies.add(i to j)
                }
            }
        }

        var ans = 0
        for ((i, galaxy) in galaxies.withIndex()) {
            for (otherGalaxy in galaxies.drop(i)) {
                ans += abs(galaxy.first - otherGalaxy.first) + abs(galaxy.second - otherGalaxy.second)
            }
        }
        return ans
    }

    fun part2(input: List<String>, addEmpty: Int = 1_000_000): Long {
        val galaxyRows = mutableListOf(0)
        for (row in input) {
            galaxyRows.add(galaxyRows.last() + if ("#" !in row) addEmpty else 1)
        }
        val input2 = input.map { it.toList() }.transpose().map { it.joinToString("") }

        val galaxyCols = mutableListOf(0)
        for (row in input2) {
            galaxyCols.add(galaxyCols.last() + if ("#" !in row) addEmpty else 1)
        }
        val galaxies = mutableListOf<Pair<Int, Int>>()

        for ((i, row) in input.withIndex()) {
            for ((j, col) in row.withIndex()) {
                if (col == '#') {
                    galaxies.add(i to j)
                }
            }
        }

        var ans = 0L
        for ((i, galaxy) in galaxies.withIndex()) {
            for (otherGalaxy in galaxies.drop(i)) {
                ans += abs(galaxyRows[galaxy.first] - galaxyRows[otherGalaxy.first]) +
                        abs(galaxyCols[galaxy.second] - galaxyCols[otherGalaxy.second])
            }
        }
        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 374)
    check(part2(testInput, 100) == 8410L)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}