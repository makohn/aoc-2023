import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

typealias CharMatrix = Array<CharArray>
fun List<String>.toCharMatrix() = Array(size) { idx -> get(idx).toCharArray() }
val CharMatrix.dimension get() = size to get(0).size

data class Cell<T>(val x: Int, val y: Int, val data: T)

typealias CharCell = Cell<Char>
/**
 * Returns a list of all adjacent cells (also diagonal)
 * ```
 *     y
 *   . . .
 * x . o .
 *   . . .
 * ```
 */
fun CharMatrix.adjacentTo(x: Int, y: Int) = buildList {
    val (n, m) = dimension
    for (i in -1..1) for (j in -1..1) {
        val xx = x + i
        val yy = y + j
        if (xx in 0..<n && yy in 0..<m) add(Cell(xx, yy, this@adjacentTo[xx][yy]))
    }
}

fun Iterable<Int>.product() = reduce { a, b -> a * b }