package mazerunner

import java.io.File

/**
 * Created with IntelliJ IDEA.
 * $ Project: Maze Runner (Kotlin)
 * User: rodrigotroy
 * Date: 01-05-23
 * Time: 13:20
 */
class Maze {
    private val maze: Array<IntArray>

    constructor(height: Int, width: Int) {
        this.maze = Array(height) { i -> IntArray(width) { j -> if (i % 2 == 0 || j % 2 == 0) 1 else 0 } }
        generateMaze()
    }

    constructor(maze: Array<IntArray>) {
        this.maze = maze
    }

    constructor(file: File) {
        this.maze = file.readLines().map { it.split(" ").map { it.toInt() }.toIntArray() }.toTypedArray()
    }

    private fun removeWalls(current: Pair<Int, Int>, next: Pair<Int, Int>, maze: Array<IntArray>) {
        val xDiff = next.first - current.first
        val yDiff = next.second - current.second
        val removeX = current.first + xDiff / 2
        val removeY = current.second + yDiff / 2
        maze[removeX][removeY] = 0
    }

    fun printMaze() {
        println()

        for (row in maze) {
            for (cell in row) {
                if (cell == 1) print("\u2588\u2588")
                if (cell == 0) print("  ")
                if (cell == 2) print("//")
            }

            println()
        }

        println()
    }

    private fun generateMaze() {
        val height = maze.size
        val width = maze[0].size

        if (checkRowHasSpaces(height - 1)) {
            swapRows(height - 2, height - 1)
        }

        if (checkColumnHasSpaces(width - 1)) {
            swapColumns(width - 2, width - 1)
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

        generateEnterAndExit()
    }

    private fun generateEnterAndExit() {
        val width = maze.size
        val height = maze[0].size
        val enter = Pair(1, 0)
        val exit = Pair(width - 2, height - 1)
        maze[enter.first][enter.second] = 0
        maze[exit.first][exit.second] = 0
    }

    private fun swapRows(row1: Int, row2: Int) {
        val temp = maze[row1]
        maze[row1] = maze[row2]
        maze[row2] = temp
    }

    private fun swapColumns(col1: Int, col2: Int) {
        for (i in maze.indices) {
            val temp = maze[i][col1]
            maze[i][col1] = maze[i][col2]
            maze[i][col2] = temp
        }
    }

    private fun checkRowHasSpaces(row: Int): Boolean {
        return maze[row].any { it == 0 }
    }

    private fun checkColumnHasSpaces(col: Int): Boolean {
        return maze.any { it[col] == 0 }
    }

    fun saveMazeToFile(filename: String) {
        val file = File(filename)

        for (i in maze.indices) {
            for (j in maze[i].indices) {
                if (maze[i][j] == 2) {
                    maze[i][j] = 0
                }
            }
        }

        file.writeText(maze.joinToString(separator = "\n") { it.joinToString(separator = " ") })
    }


    fun findEscape() {
        val height = maze.size
        val width = maze[0].size
        val start = Pair(1, 0)
        val end = Pair(width - 2, height - 1)
        val stack = mutableListOf(start)
        val visited = mutableSetOf(start)
        val directions = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
        val path = mutableListOf(start)

        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            val unvisitedNeighbors = directions.map { Pair(current.first + it.first, current.second + it.second) }
                .filter { it.first in 0 until height && it.second in 0 until width && it !in visited && maze[it.first][it.second] == 0 }

            if (current == end) {
                break
            }

            if (unvisitedNeighbors.isNotEmpty()) {
                val next = unvisitedNeighbors.random()
                visited.add(next)
                stack.add(current)
                stack.add(next)
                path.add(next)
            } else {
                path.removeLast()
            }
        }

        if (path.last() == end) {
            println("The exit is found!")
        } else {
            println("There is no exit!")
        }

        for (i in path.indices) {
            maze[path[i].first][path[i].second] = 2
        }

        printMaze()
    }
}
