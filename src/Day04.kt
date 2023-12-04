fun main() {

    val day = "04"

    val numberRegex = Regex("\\d+")

    fun String.numbers() = numberRegex.findAll(this).map { it.value.toInt() }.toList()

    fun part1(input: List<String>): Int {
        return input
            .map { it.split("|") }
            .map { (a, b) -> a.substringAfter(":").numbers() to b.numbers() }
            .sumOf { (winning, numbers) ->
                numbers.filter { it in winning }.fold(0) { acc: Int, _ -> if (acc == 0) 1 else acc * 2 }
            }
    }

    fun part2(input: List<String>): Int {
        val parsed = input
            .map { it.split("|") }
            .map { (a, b) -> a.substringAfter(":").numbers() to b.numbers() }

        val winning = parsed.map { it.first }.toList()
        val cards = parsed.map { it.second }.toList()
        val cardCount = IntArray(cards.size) { 1 }

        for ((i, card) in cards.withIndex()) {
            val points = card.count { it in winning[i] }
            for (j in 1..points) {
                cardCount[i+j] += cardCount[i]
            }
        }
        return cardCount.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}