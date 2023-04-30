package mazerunner

fun main() {
    val maze = generateMaze(10, 10)
    printMaze(maze)
}

fun generateEnterAndExit(maze: Array<IntArray>): Array<IntArray> {
    val width = maze.size
    val height = maze[0].size
    val enter = Pair(1, 0)
    val exit = Pair(width - 2, height - 1)
    maze[enter.first][enter.second] = 0
    maze[exit.first][exit.second] = 0
    return maze
}

fun swapRows(maze: Array<IntArray>, row1: Int, row2: Int) {
    val temp = maze[row1]
    maze[row1] = maze[row2]
    maze[row2] = temp
}

fun swapColumns(maze: Array<IntArray>, col1: Int, col2: Int) {
    for (i in maze.indices) {
        val temp = maze[i][col1]
        maze[i][col1] = maze[i][col2]
        maze[i][col2] = temp
    }
}

fun checkRowHasSpaces(maze: Array<IntArray>, row: Int): Boolean {
    return maze[row].any { it == 0 }
}

fun checkColumnHasSpaces(maze: Array<IntArray>, col: Int): Boolean {
    return maze.any { it[col] == 0 }
}

fun generateMaze(width: Int, height: Int): Array<IntArray> {
    val maze = Array(height) { i -> IntArray(width) { j -> if (i % 2 == 0 || j % 2 == 0) 1 else 0 } }

    if (checkRowHasSpaces(maze, height - 1)) {
        swapRows(maze, height - 2, height - 1)
    }

    if (checkColumnHasSpaces(maze, width - 1)) {
        swapColumns(maze, width - 2, width - 1)
    }

    val start = Pair(1, 1)
    val stack = mutableListOf(start)
    val visited = mutableSetOf(start)
    val directions = listOf(Pair(2, 0), Pair(0, 2), Pair(-2, 0), Pair(0, -2))

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        val unvisitedNeighbors = directions.map { Pair(current.first + it.first, current.second + it.second) }
            .filter { it.first in 1 until height - 1 && it.second in 1 until width - 1 && it !in visited }

        if (unvisitedNeighbors.isNotEmpty()) {
            val next = unvisitedNeighbors.random()
            removeWalls(current, next, maze)
            visited.add(next)
            stack.add(current)
            stack.add(next)
        }
    }

    generateEnterAndExit(maze)

    return maze
}

fun removeWalls(current: Pair<Int, Int>, next: Pair<Int, Int>, maze: Array<IntArray>) {
    val xDiff = next.first - current.first
    val yDiff = next.second - current.second
    val removeX = current.first + xDiff / 2
    val removeY = current.second + yDiff / 2
    maze[removeX][removeY] = 0
}

fun printMaze(maze: Array<IntArray>) {
    maze.forEach { row ->
        println(row.joinToString(separator = "") { if (it == 1) "\u2588\u2588" else "  " })
    }

    println()
}
