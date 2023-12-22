import kotlin.math.max
import kotlin.math.min

fun main() {

    val day = "22"

    data class Brick(val x0: Int, val y0: Int, var z0: Int, val x1: Int, val y1: Int, var z1: Int) {
        val supportedBricks = mutableSetOf<Brick>()
        val supportingBricks = mutableSetOf<Brick>()
    }

    infix fun Brick.intersectsWith(other: Brick) =
        max(this.x0, other.x0) <= min(this.x1, other.x1) && max(this.y0, other.y0) <= min(this.y1, other.y1)

    fun Brick.drop(bricksBelow: List<Brick>): Brick {
        var z = 1
        for (otherBrick in bricksBelow) {
            if (this intersectsWith otherBrick) {
                z = z.coerceAtLeast(otherBrick.z1 + 1)
            }
        }
        return this.apply {
            z1 -= (this.z0 - z)
            z0 = z
        }
    }

    fun parseInput(input: List<String>) = input
        .filter { it.isNotBlank() }
        .map { it.numbers() }
        .map { (x0, y0, z0, x1, y1, z1) -> Brick(x0, y0, z0, x1, y1, z1) }
        .sortedBy { it.z0 }

    fun dropBricks(bricks: List<Brick>): List<Brick> {
        val droppedBricks = bricks
            .mapIndexed { i, brick -> brick.drop(bricks.take(i)) }
            .sortedBy { it.z0 }

        for ((i, brick) in droppedBricks.withIndex()) {
            for (otherBrick in droppedBricks.take(i)) {
                if (brick intersectsWith otherBrick && (brick.z0 - otherBrick.z1) == 1) {
                    brick.supportingBricks.add(otherBrick)
                    otherBrick.supportedBricks.add(brick)
                }
            }
        }

        return droppedBricks
    }

    fun part1(input: List<String>): Int {
        val bricks = parseInput(input)
        return dropBricks(bricks)
            .count { brick ->
                brick.supportedBricks
                    .all { it.supportingBricks.any { supporter -> supporter != brick } }
            }
    }

    fun part2(input: List<String>): Int {
        val bricks = parseInput(input)
        val droppedBricks = dropBricks(bricks)

        var ans = 0
        for (brick in droppedBricks) {
            val droppingBricks = HashSet<Brick>()
            val dependents = brick.supportedBricks.filter { it.supportingBricks.none { supporter -> supporter != brick } }
            val dependentsQueue = ArrayDeque<Brick>()
            dependentsQueue.addAll(dependents)
            droppingBricks.addAll(dependents)
            droppingBricks.add(brick)

            while (dependentsQueue.isNotEmpty()) {
                val dependentBrick = dependentsQueue.removeFirst()

                for (transitiveBrick in dependentBrick.supportedBricks) {
                    if (transitiveBrick !in droppingBricks && transitiveBrick.supportingBricks.all { it in droppingBricks }) {
                        dependentsQueue += transitiveBrick
                        droppingBricks += transitiveBrick
                    }
                }
            }
            ans += droppingBricks.size
        }
        return ans - droppedBricks.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}