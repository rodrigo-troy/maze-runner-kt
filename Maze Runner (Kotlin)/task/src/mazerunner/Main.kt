package mazerunner

import java.io.File

fun main() {
    var maze: Maze? = null

    while (true) {
        if (maze == null) {
            printInitialMenu()
        } else {
            printFullMenu()
        }

        when (readln().toInt()) {
            1 -> {
                println("Enter the size of a new maze: ")
                val size = readln().toInt()
                maze = Maze(size, size)
                maze.printMaze()
            }

            2 -> {
                print("Enter file name: ")
                val fileName = readln()

                try {
                    maze = Maze(File(fileName))
                } catch (e: Exception) {
                    println("The file $fileName does not exist")
                }
            }

            3 -> {
                print("Enter file name: ")
                val fileName = readln()
                maze?.saveMazeToFile(fileName)
            }

            4 -> {
                maze?.printMaze()
            }

            5 -> {
                maze?.findEscape()
            }

            0 -> {
                println("Bye!")
                return
            }

            else -> {
                println("Incorrect option. Please try again")
            }
        }
    }

}

fun printInitialMenu() {
    println("=== Menu ===")
    println("1. Generate a new maze")
    println("2. Load a maze")
    println("0. Exit")
}

fun printFullMenu() {
    println("=== Menu ===")
    println("1. Generate a new maze")
    println("2. Load a maze")
    println("3. Save the maze")
    println("4. Display the maze")
    println("5. Find the escape")
    println("0. Exit")
}

