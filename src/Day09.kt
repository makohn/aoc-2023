fun main() {

    val day = "09"

    fun predictLast(history: List<Int>): Int {
        if (history.all { it == 0 }) return 0
        return history.last() + predictLast(history.zipWithNext { a, b -> b - a })
    }

    fun predictFirst(history: List<Int>): Int {
        if (history.all { it == 0 }) return 0
        return history.first() - predictFirst(history.zipWithNext { a, b -> b - a })
    }

    fun part1(input: List<String>) = input
        .map { it.split(" ").map { it.toInt() } }
        .sumOf { predictLast(it) }

    fun part2(input: List<String>) = input
        .map { it.split(" ").map { it.toInt() } }
        .sumOf { predictFirst(it) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}