import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import java.util.PriorityQueue
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Reads a string from the given input txt file.
 */
fun readInputString(name: String) = Path("src/$name.txt").readText()

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

fun CharMatrix.toCharCells() = flatMapIndexed { i, row -> row.mapIndexed { j, col -> CharCell(i, j, col)  } }

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

fun <N> dijkstra(start: N, adjacent: (N) -> List<Pair<N, Int>>): HashMap<N, Int> {

    val queue = PriorityQueue<Pair<Int, N>>(compareBy{ it.first })
    val visited = HashMap<N, Int>()

    fun enqueue(node: N, distance: Int) {
        if (node !in visited || distance < visited[node]!!) {
            visited[node] = distance
            queue += distance to node
        }
    }

    enqueue(start, 0)

    while (queue.isNotEmpty()) {
        val (cost, node) = queue.remove()
        if (cost <= visited[node]!!) {
            for ((otherNode, weight) in adjacent(node)) {
                val distance = cost + weight
                enqueue(otherNode, distance)
            }
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

fun CharMatrix.adjacentToUnbound(cell: CharCell, vararg dirs: Direction) = buildList {
    val (n, m) = dimension
    val (x, y, _) = cell
    for ((j, i) in dirs) {
        val xx = x + i
        val yy = y + j
        add(Cell(xx, yy, this@adjacentToUnbound[xx fmod n][yy fmod m]))
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

fun <T> MutableList<T>.replaceIf(instead: T, predicate: (T) -> Boolean) {
    this@replaceIf[this.indexOfFirst(predicate)] = instead
}

open class Matrix(
    private val rows: Int,
    private val cols: Int,
    val data: DoubleArray = DoubleArray(rows * cols) { 0.0 }
) {

    operator fun times(other: Matrix): Matrix {
        check(this.cols == other.rows)

        val size = this.rows * other.cols
        val res = DoubleArray(size)

        for (i in 0..<this.rows) {
            for (j in 0..<other.cols) {
                var sum = 0.0
                for (k in 0..<this.cols) {
                    sum += this.data[i * this.cols + k] * other.data[k * other.cols + j]
                }
                res[i * other.cols + j] = sum
            }
        }
        return Matrix(this.rows, other.cols, res)
    }

    override fun toString() = buildString {
        for (i in 0..<rows) {
            for (j in 0..<cols) {
                append(data[i + j]).append(" ")
            }
            append("\n")
        }
    }
}

class Matrix4x4(data: DoubleArray = DoubleArray(16) { 0.0 }): Matrix(4, 4, data) {

    fun inverse(): Matrix4x4 {

        fun valueAt(ii: Int, jj: Int): Double {
            val o = 2+(jj-ii)
            val i = ii + 4 + o
            val j = jj + 4 - o

            val e = { a: Int, b: Int -> data[ ((j+b)%4)*4 + ((i+a)%4) ]}

            val inv = e(+1,-1)*e(+0,+0)*e(-1,+1) +
                    e(+1,+1)*e(+0,-1)*e(-1,+0) +
                    e(-1,-1)*e(+1,+0)*e(+0,+1) -
                    e(-1,-1)*e(+0,+0)*e(+1,+1) -
                    e(-1,+1)*e(+0,-1)*e(+1,+0) -
                    e(+1,-1)*e(-1,+0)*e(+0,+1)

            return if (o%2 == 0) inv else -inv
        }

        val inv = DoubleArray(16)

        for (i in 0..<4) {
            for (j in 0..<4) {
                inv[j*4+i] = valueAt(i, j)
            }
        }

        var d = 0.0
        for (k in 0..<4) d += data[k] * inv[k*4]

        d = 1.0 / d

        for (i in 0..<16) inv[i] *= d

        return Matrix4x4(inv)
    }
}

fun <T> List<T>.combinations() = sequence {
    for (i in this@combinations.indices) {
        for (j in i+1 ..< size) {
            yield(get(i) to get(j))
        }
    }
}

fun String.numbers() = Regex("\\d+").findAll(this).map { it.value.toInt() }.toList()
fun String.numbersAsDouble() = Regex("-?\\d+").findAll(this).map { it.value.toDouble() }.toList()

operator fun <T> List<T>.component6() = get(5)

infix fun Int.fmod(base: Int) = ((this % base) + base) % base