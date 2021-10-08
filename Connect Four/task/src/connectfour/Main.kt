package connectfour

var firstPlayerName = ""
var secondPlayerName = ""
var rows = 6;
var columns = 7;
val boardList2D = mutableListOf<MutableList<Char>>()
var numbersOfGames = 1
var scoreFirst = 0
var scoreSecond = 0
var gameOver = false
var currentPlayer = 1
const val UP = -1
const val DOWN = 1
const val LEFT = -1
const val RIGHT = 1
const val NO = 0

fun main() {
    println("Connect Four")
    println("First player's name:")
    firstPlayerName = readLine()!!
    println("Second player's name:")
    secondPlayerName = readLine()!!
    parseDimensions()
    parseNumberOfGames()
    println("$firstPlayerName VS $secondPlayerName\n" +
            "$rows X $columns board")
    if (numbersOfGames == 1) {
        println("Single game")
        constructBoardAndPlay()
    }
    else {
        println("Total $numbersOfGames games")
        for (i in 1..numbersOfGames) {
            if (gameOver) return
            println("Game #$i")
            currentPlayer = i % 2
            constructBoardAndPlay()
            println("Score\n" +
                    "$firstPlayerName: $scoreFirst $secondPlayerName: $scoreSecond")
        }
        println("Game over!")
    }
}

fun constructBoardAndPlay() {
    constructBoard()
    drawBoard()
    play()
}

fun parseDimensions() {
    while (true) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        val dimensions = readLine()!!.uppercase().replace("\\s+".toRegex(),"")
        if (dimensions.isEmpty()) break
        if (!dimensions.matches("\\d+X\\d+".toRegex()))
            println("Invalid input")
        else {
            val dimensionsList = dimensions.split("X").toMutableList()
            rows = dimensionsList[0].toInt()
            if (rows !in 5..9) {
                println("Board rows should be from 5 to 9")
                continue
            }
            columns = dimensionsList[1].toInt()
            if (columns !in 5..9) {
                println("Board columns should be from 5 to 9")
                continue
            }
            break
        }
    }
}

fun parseNumberOfGames() {
    while (true) {
        println("""Do you want to play single or multiple games?
                   For a single game, input 1 or press Enter
                   Input a number of games:""".trimIndent())
        val input = readLine()!!
        if (input.isEmpty()) break
        try {
            numbersOfGames = input.toInt()
            if (numbersOfGames == 0) {
                println("Invalid input")
                continue
            }
            break
        }
        catch (e: NumberFormatException) {
            println("Invalid input")
        }
    }
}

fun constructBoard() {
    boardList2D.clear()
    for (i in 1..rows) {
        val rowList = mutableListOf<Char>()
        for (i in 1..columns)
            rowList.add(' ')
        boardList2D.add(rowList)
    }
}

fun drawBoard() {
    for (i in 1..columns) print(" $i")
    println()
    for (row in boardList2D)
        println(row.joinToString("║", "║", "║"))
    println(List(columns) { "═" }.joinToString("╩", "╚", "╝"))
}

fun boardIsFull() : Boolean {
    for (i in 0 until columns)
        if (boardList2D[0][i] == ' ') {
            return false
    }
    return true
}

fun buildStringOf4(row: Int, rowMargin: Int, col: Int, colMargin: Int): String {
    return buildString { for (i in 0..3)
        if ((row + rowMargin*i in 0 until rows) && (col + colMargin*i in 0 until columns))
            append(boardList2D[row + rowMargin*i][col + colMargin*i])}
}

fun isVictory(mark: Char, cell: Pair<Int, Int>) : Boolean {
    val winningString = "$mark".repeat(4)
    val row = cell.first
    val col = cell.second
    return (buildStringOf4(row, DOWN , col, NO) == winningString) ||
        (buildStringOf4(row, NO , col, RIGHT) == winningString) ||
        (buildStringOf4(row, NO , col, LEFT) == winningString) ||
        (buildStringOf4(row, DOWN , col, RIGHT) == winningString) ||
        (buildStringOf4(row, DOWN , col, LEFT) == winningString) ||
        (buildStringOf4(row, UP , col, RIGHT) == winningString) ||
        (buildStringOf4(row, UP , col, LEFT) == winningString)
}

fun play() {
    var currentCell: Pair<Int, Int> = Pair(0, 0)

    while(true) {
        val playerName = if (currentPlayer == 1) firstPlayerName else secondPlayerName
        val mark = if (currentPlayer == 1) 'o' else '*'
        println("$playerName's turn:")
        val input = readLine()!!
        when {
            input == "end" -> {
                println("Game over!")
                gameOver = true
                return
            }
            !input.matches("\\d+".toRegex()) -> {
                println("Incorrect column number")
                continue
            }
        }
        val column = input.toInt()
        if (column !in 1..columns) {
            println("The column number is out of range (1 - $columns)")
            continue
        }
        if (boardList2D[0][column - 1] != ' ') {
            println("Column $column is full")
            continue
        }
        for (i in rows - 1 downTo 0)
            if (boardList2D[i][column - 1] == ' ') {
                boardList2D[i][column - 1] = mark
                currentCell = Pair(i,column - 1)
                break
            }
        drawBoard()
        if (isVictory(mark, currentCell)) {
            println("Player $playerName won")
            if (numbersOfGames == 1) println("Game over!")
            if (currentPlayer == 1) scoreFirst += 2 else scoreSecond +=2
            return
        }
        else if (boardIsFull()) {
            println("It is a draw")
            if (numbersOfGames == 1) println("Game over!")
            scoreFirst++
            scoreSecond++
            return
        }
        currentPlayer = if (currentPlayer == 1) 2 else 1
    }
}