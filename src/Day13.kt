
fun main() {

    val day = "13"

    fun part1(input: List<String>): Int {
        fun verticalMirrorPosition(pattern: CharMatrix): Int {
            val (n, m) = pattern.dimension
            moveMirror@for (mirror in 1..<m) {
                for (margin in 0..m) {
                    val left = mirror - 1 - margin
                    val right = mirror + margin
                    if (left in 0..<mirror && right in mirror..<m) {
                        for (row in 0..<n) {
                            if (pattern[row][left] != pattern[row][right]) continue@moveMirror
                        }
                    }
                }
                return mirror
            }
            return 0
        }

        fun horizontalMirrorPosition(pattern: CharMatrix): Int {
            val (n, m) = pattern.dimension
            moveMirror@for (mirror in 1..<n) {
                for (margin in 0..n) {
                    val above = mirror - 1 - margin
                    val below = mirror + margin
                    if (above in 0..<mirror && below in mirror..<n) {
                        for (col in 0..<m) {
                            if (pattern[above][col] != pattern[below][col]) continue@moveMirror
                        }
                    }
                }
                return mirror
            }
            return 0
        }

        return input
            .joinToString("\n")
            .split("\n\n")
            .map { it.split("\n").toCharMatrix() }
            .sumOf { verticalMirrorPosition(it) + 100*horizontalMirrorPosition(it) }
    }

    fun part2(input: List<String>): Int {
        fun verticalMirrorPosition(pattern: CharMatrix): Int {
            val (n, m) = pattern.dimension
            for (mirror in 1..<m) {
                var error = 0
                for (margin in 0..m) {
                    val left = mirror - 1 - margin
                    val right = mirror + margin
                    if (left in 0..<mirror && right in mirror..<m) {
                        for (row in 0..<n) {
                            if (pattern[row][left] != pattern[row][right]) error++
                        }
                    }
                }
                if (error == 1) return mirror
            }
            return 0
        }

        fun horizontalMirrorPosition(pattern: CharMatrix): Int {
            val (n, m) = pattern.dimension
            for (mirror in 1..<n) {
                var error = 0
                for (margin in 0..n) {
                    val above = mirror - 1 - margin
                    val below = mirror + margin
                    if (above in 0..<mirror && below in mirror..<n) {
                        for (col in 0..<m) {
                            if (pattern[above][col] != pattern[below][col]) error++
                        }
                    }
                }
                if (error == 1) return mirror
            }
            return 0
        }

        return input
            .joinToString("\n")
            .split("\n\n")
            .map { it.split("\n").toCharMatrix() }
            .sumOf { verticalMirrorPosition(it) + 100*horizontalMirrorPosition(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}