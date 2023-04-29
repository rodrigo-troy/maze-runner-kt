package mazerunner

fun main() {
    val maze = generateMaze(9, 9)
    printMaze(maze)
}

fun generateBorderWalls(maze: Array<IntArray>): Array<IntArray> {
    val width = maze.size
    val height = maze[0].size
    for (i in 0 until width) {
        maze[i][0] = 1
        maze[i][height - 1] = 1
    }
    for (j in 0 until height) {
        maze[0][j] = 1
        maze[width - 1][j] = 1
    }
    return maze
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


fun generateMaze(width: Int, height: Int): Array<IntArray> {
    val maze = Array(width) { i -> IntArray(height) { j -> if (i % 2 == 0 || j % 2 == 0) 1 else 0 } }
    val start = Pair(1, 1)
    val stack = mutableListOf(start)
    val visited = mutableSetOf(start)
    val directions = listOf(Pair(2, 0), Pair(0, 2), Pair(-2, 0), Pair(0, -2))

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        val unvisitedNeighbors = directions.map { Pair(current.first + it.first, current.second + it.second) }
            .filter { it.first in 1 until width - 1 && it.second in 1 until height - 1 && it !in visited }

        if (unvisitedNeighbors.isNotEmpty()) {
            val next = unvisitedNeighbors.random()
            removeWalls(current, next, maze)
            visited.add(next)
            stack.add(current)
            stack.add(next)
        }
    }

    generateBorderWalls(maze)
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
    for (row in maze) {
        for (cell in row) {
            if (cell == 1) {
                print("\u2588\u2588")
            } else {
                print("  ")
            }
        }
        println()
    }
}
