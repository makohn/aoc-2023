
fun main() {

    val day = "06"

    fun part1(input: List<String>): Int {
        val (raceLengths, records) = input.map { it.substringAfter(":").split("\\s".toRegex()).filter { it.isNotEmpty() }.map { it.toInt() } }

        val ans = raceLengths.withIndex().map { (idx, len) ->
            var count = 0
            for (i in 0..len) {
                if ((len - i) * i > records[idx]) count ++
            }
            count
        }
        return ans.product()
    }

    fun part2(input: List<String>): Int {
        val (raceLength, record) = input.map { it.substringAfter(":").split("\\s".toRegex()).filter { it.isNotEmpty() }.joinToString("").toLong() }

        var count = 0
        for (i in 0..raceLength) {
            if ((raceLength - i) * i > record) count ++
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}