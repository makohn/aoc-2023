
fun main() {

    val day = "<DAY>"

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 0)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}