
fun main() {

    val day = "02"

    data class Step(var r: Int, var g: Int, var b: Int)
    data class Game(val steps: MutableList<Step>)

    fun part1(input: List<String>): Int {
        val games = mutableListOf<Game>()
        for (line in input) {
            println(line)
            val g = Game(mutableListOf())
            games += g
            val game = line.split(":")[1]
            val steps = game.split(";")
            for (step in steps) {
                val s = Step(0, 0, 0)
                val parts = step.split(",")
                for (part in parts) {
                    println(part)
                    val (n, color) = part.trim().split(" ")
                    when (color.trim()) {
                        "green" -> s.g = n.trim().toInt()
                        "blue" -> s.b = n.trim().toInt()
                        "red" -> s.r = n.trim().toInt()
                    }
                }
                g.steps += s
            }
        }
        var ans = 0
        // only 12 red cubes, 13 green cubes, and 14 blue cubes
        for ((i, game) in games.withIndex()) {
            if (game.steps.any { it.b > 14 }) continue
            if (game.steps.any { it.g > 13 }) continue
            if (game.steps.any { it.r > 12 }) continue
            ans += i+1
        }
        println(games)
        return ans
    }

    fun part2(input: List<String>): Int {
        val games = mutableListOf<Game>()
        for (line in input) {
            println(line)
            val g = Game(mutableListOf())
            games += g
            val game = line.split(":")[1]
            val steps = game.split(";")
            for (step in steps) {
                val s = Step(0, 0, 0)
                val parts = step.split(",")
                for (part in parts) {
                    println(part)
                    val (n, color) = part.trim().split(" ")
                    when (color.trim()) {
                        "green" -> s.g = n.trim().toInt()
                        "blue" -> s.b = n.trim().toInt()
                        "red" -> s.r = n.trim().toInt()
                    }
                }
                g.steps += s
            }
        }

        var ans = 0
        for ((i, game) in games.withIndex()) {
            val minB = game.steps.maxOf { it.b }
            val minR = game.steps.maxOf { it.r }
            val minG = game.steps.maxOf { it.g }
            ans += minB * minR * minG
        }

        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}