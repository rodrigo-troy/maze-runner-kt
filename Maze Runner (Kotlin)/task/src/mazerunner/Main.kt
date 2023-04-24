package mazerunner

import kotlin.random.Random

data class Cell(val row: Int, val col: Int)

fun main() {
    val maze = generateMaze(10, 10)
    printMaze(maze)
}

fun generateMaze(rows: Int, columns: Int): Array<IntArray> {
    val maze = Array(rows) { IntArray(columns) { 1 } }
    val entranceRow = Random.nextInt(1, rows - 1)
    val exitRow = Random.nextInt(1, rows - 1)
    maze[entranceRow][0] = 0
    maze[exitRow][columns - 1] = 0

    val visited = Array(rows) { BooleanArray(columns) { false } }
    val stack = mutableListOf<Cell>()
    stack.add(Cell(entranceRow, 1))

    while (stack.isNotEmpty()) {
        val currentCell = stack.last()
        val row = currentCell.row
        val col = currentCell.col
        visited[row][col] = true
        maze[row][col] = 0

        val neighbors = listOf(
            Cell(row - 2, col),
            Cell(row + 2, col),
            Cell(row, col - 2),
            Cell(row, col + 2)
        ).shuffled()

        var hasUnvisitedNeighbors = false
        for (neighbor in neighbors) {
            if (neighbor.row in 1 until (rows - 1) && neighbor.col in 1 until (columns - 1) && !visited[neighbor.row][neighbor.col]) {
                val betweenRow = (row + neighbor.row) / 2
                val betweenCol = (col + neighbor.col) / 2

                maze[betweenRow][betweenCol] = 0
                stack.add(neighbor)
                hasUnvisitedNeighbors = true
                break
            }
        }

        if (!hasUnvisitedNeighbors) {
            stack.removeLast()
        }
    }

    return maze
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
