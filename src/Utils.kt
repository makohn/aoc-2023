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

tailrec fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

fun Iterable<Long>.lcm() = reduce { acc, i -> lcm(acc, i) }

fun <N> bfs(startNode: N, adjacent: (N) -> List<N>): HashMap<N, Int> {

    val queue = ArrayDeque<N>()
    val visited = HashMap<N, Int>()

    fun enqueue(node: N, distance: Int) {
        if (node in visited) return
        visited[node] = distance
        queue += node
    }

    enqueue(startNode, 0)

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        val distance = visited[node]!! + 1

        for (otherNode in adjacent(node)) {
            enqueue(otherNode, distance)
        }
    }

    return visited
}

enum class Direction(val xDir: Int, val yDir: Int) {
    North(0, -1),
    West(-1, 0),
    East(1, 0),
    South(0, 1);

    operator fun component1() = xDir
    operator fun component2() = yDir
}

fun CharMatrix.adjacentTo(cell: CharCell, vararg dirs: Direction) = buildList {
    val (n, m) = dimension
    val (x, y, _) = cell
    for ((j, i) in dirs) {
        val xx = x + i
        val yy = y + j
        if (xx in 0..<n && yy in 0..<m) add(Cell(xx, yy, this@adjacentTo[xx][yy]))
    }
}

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val result = (first().indices).map { mutableListOf<T>() }.toMutableList()
    forEach { list -> result.zip(list).forEach { it.first.add(it.second) } }
    return result
}

fun CharMatrix.rotateClockwise(): CharMatrix {
    val (n, m) = dimension
    val rotated = Array(m) { CharArray(n) }
    for (i in 0..<n) {
        for (j in 0..<m) {
            rotated[j][i] = this@rotateClockwise[n-i-1][j]
        }
    }
    return rotated
}

fun CharMatrix.asString(separator: String = "\n") =
    joinToString(separator) { it.joinToString("") }