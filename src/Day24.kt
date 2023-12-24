import kotlin.math.roundToLong

fun main() {

    val day = "24"

    data class Vec2(val x: Double, val y: Double)

    data class LinearFunction(val a: Double, val m: Double) {
        operator fun invoke(x: Double) = a*x + m
    }

    data class Hailstone(val x: Double, val y: Double, val z: Double, val vx: Double, val vy: Double, val vz: Double) {
        val f = LinearFunction(vy/vx, -((vy/vx) * x - y))

        infix fun intersect(other: Hailstone): Vec2 {
            val g = other.f
            val ix = (g.m - f.m)/(f.a - g.a)
            val iy = f(ix)
            return Vec2(ix, iy)
        }
    }

    fun Hailstone.inFuture(p: Vec2) = if (vx > 0) p.x > x else p.x < x

    fun part1(input: List<String>, min: Double = 7.0, max: Double = 27.0): Int {
        val hailstones = input
            .map { it.numbersAsDouble() }
            .map { (a, b, c, d, e, f) -> Hailstone(a, b, c, d, e, f) }

        val intersections = mutableListOf<Vec2>()

        for ((i, stone) in hailstones.withIndex()) {
            for (otherStone in hailstones.drop(i+1)) {
                val intersection = stone intersect otherStone

                if (intersection.x in min..max &&
                    intersection.y in min..max &&
                    stone.inFuture(intersection) &&
                    otherStone.inFuture(intersection)) {
                    intersections += intersection
                }
            }
        }

        return intersections.count()
    }

    fun part2(input: List<String>): Long {
        val hailstones = input
            .map { it.numbersAsDouble() }
            .map { (a, b, c, d, e, f) -> Hailstone(a, b, c, d, e, f) }

        val res = hailstones
            .combinations()
            .take(4)
            .map { (h1, h2) ->
                listOf(h2.vy - h1.vy, h1.vx - h2.vx, h1.y - h2.y, h2.x - h1.x) to
                        h1.vx * h1.y - h2.vx * h2.y + h2.x * h2.vy - h1.x * h1.vy
            }
            .toList()

        val A = Matrix4x4(res.map { (a, _) -> a }.flatten().toDoubleArray())
        val v = Matrix(4, 1, res.map { (_, b) -> b }.toDoubleArray())

        val x = A.inverse() * v
        val (a, b, d, _) = x.data.map { it.roundToLong() }

        val h0 = hailstones[0]
        val t0 = (a - h0.x) / (h0.vx - d)

        val h1 = hailstones[1]
        val t1 = (a - h1.x) / (h1.vx - d)

        val f = ((h0.z - h1.z) + t0 * h0.vz - t1 * h1.vz) / (t0 - t1)
        val c = h0.z + t0 * (h0.vz - f)

        return (a + b + c).toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 47L)

    val input = readInput("Day${day}")
    println(part1(input, min = 200000000000000.0, max = 400000000000000.0))
    println(part2(input))
}