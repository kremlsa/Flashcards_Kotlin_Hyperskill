package flashcards

import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.random.*

var isRun = true
val scanner = Scanner(System.`in`)
var flashcards: MutableList<Card> = mutableListOf<Card>()
var log = Log()
var isExport = false
var exportPath = ""

fun main(args: Array<String>) {
    processArgs(args)
    while (isRun) {
        println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        log.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        menu(scanner.nextLine())
    }
}

fun processArgs(args: Array<String>) {
    if (args.size > 0) {
        for (i in 0 until args.size) {
            when (args[i]) {
                "-import" -> import(args[i+1])
                "-export" -> {
                    isExport = true
                    exportPath = args[i+1]
                }
            }
        }
    }
}

fun hardestCard() {
    var max = 0
    var res = ""
    var themOrIt = "it"
    var cardOrS = "card"
    var areOrIs = "is"
    if (flashcards.size > 0) {
        for (i in 0 until flashcards.size) {
            if (flashcards[i].mistakes > max) {
                max = flashcards[i].mistakes
            }
        }
    }

    if (max == 0) {
        println("There are no cards with errors.")
        log.add("There are no cards with errors.")
    } else {
        for (i in 0 until flashcards.size) {
            if (flashcards[i].mistakes == max) {
                res += "\"${flashcards[i].front}\", "
            }
        }
        res = res.substring(0, res.length-2)
        if (res.contains(",")) {
            themOrIt = "them"
            cardOrS = "cards"
            areOrIs = "are"
        }
        println("The hardest $cardOrS $areOrIs $res. You have $max errors answering $themOrIt.")
        log.add("The hardest $cardOrS $areOrIs $res. You have $max errors answering $themOrIt.")
        println()
    }

}

fun resetStats() {
    if (flashcards.size > 0) {
        for (i in 0 until flashcards.size) {
            flashcards[i].mistakes = 0
        }
    }
    println("Card statistics have been reset.")
    log.add("Card statistics have been reset.")
}

fun createCard() {
    var front = ""
    var back = ""
    var mistakes = 0
    println("The card:")
    front = scanner.nextLine()
    if (!checkCards(front, back)) return
    println("The definition of the card:")
    log.add("The definition of the card:")
    back = scanner.nextLine()
    log.add(back)
    if (!checkCards(front, back)) return
    flashcards.add(Card(front, back, mistakes))
    println("The pair (\"$front\":\"$back\") has been added.")
    log.add("The pair (\"$front\":\"$back\") has been added.")
    println()
}

fun removeCard() {
    var front = ""
    println("The card:")
    log.add("The card:")
    front = scanner.nextLine()
    if (flashcards.size > 0) {
        for (i in 0 until flashcards.size) {
            if (flashcards[i].front == front) {
                flashcards.removeAt(i)
                println("The card has been removed.")
                log.add("The card has been removed.")
                return
            }
        }
    }
    println("Can't remove \"$front\": there is no such card.")
    log.add("Can't remove \"$front\": there is no such card.")
    println()
}

fun checkCards(front: String, back: String): Boolean {
    for (card in flashcards) {
        when {
            card.front == front -> {
                //println("The term \"$front\" already exists. Try again:")
                println("The card \"$front\" already exists. Try again:")
                log.add("The card \"$front\" already exists. Try again:")
                return false
            }
            card.back == back -> {
                println("The definition \"$back\" already exists. Try again:")
                log.add("The definition \"$back\" already exists. Try again:")
                return false
            }
        }
    }
    return true
}

fun checkAnswers() {
    println("How many times to ask?")
    var range = scanner.nextLine().toInt()
    log.add("How many times to ask?")
    log.add("$range")
    repeat(range) {
        var num = kotlin.random.Random.nextInt(0, flashcards.size)
        println("Print the definition of \"${flashcards[num].front}\":")
        var answer = scanner.nextLine()
        log.add("Print the definition of \"${flashcards[num].front}\": $answer")
        println(checkAnswer(answer, flashcards[num]))
    }
    println()
}

fun checkAnswer(answer: String, card: Card): String {
    return if(answer == card.back) {
        log.add("Correct!")
        "Correct!"
    } else if (answer != card.back && isAnswer(answer)) {
        card.addMistake()
        log.add("Wrong. The right answer is \"${card.back}\", but your definition is correct for \"${findAnswer(answer)}\".")
        "Wrong. The right answer is \"${card.back}\", but your definition is correct for \"${findAnswer(answer)}\"."
    } else {
        card.addMistake()
        log.add("Wrong. The right answer is \"${card.back}\".")
        "Wrong. The right answer is \"${card.back}\"."
    }
    println()
}

fun isAnswer(input: String): Boolean {
    for (card in flashcards) {
        if (card.back == input) return true
    }
    return false
}

fun findAnswer(answer: String): String {
    for (card in flashcards) {
        if (card.back == answer) return card.front
    }
    return ""
}

fun findCard(front: String): String {
    for (card in flashcards) {
        if (card.front == front) return card.front
    }
    return ""
}

fun menu(functionName: String) {
    log.add(functionName)
    when (functionName) {
        "export" -> saveCards()
        "import" -> loadCards()
        "add" -> createCard()
        "ask" -> checkAnswers()
        "remove" -> removeCard()
        "hardest card" -> hardestCard()
        "reset stats" -> resetStats()
        "log" -> log()
        "exit" -> {
            println("Bye bye!")
            log.add("Bye bye!")
            if (isExport) export(exportPath)
            isRun = false
        }
    }
}

fun log() {
    println ("File name:")
    val fileName = scanner.nextLine()
    log.add("File name: $fileName")
    val myFile = File(fileName).writeText(log.body)
    println("The log has been saved.")
    log.add("The log has been saved.")
    println()
}

fun saveCards() {
    var exportCards = ""
    for (card in flashcards) exportCards += card.toString()
    println ("File name:")
    val fileName = scanner.nextLine()
    log.add("File name: $fileName")
    val myFile = File(fileName).writeText(exportCards)
    println("${flashcards.size} cards have been saved.")
    log.add("${flashcards.size} cards have been saved.")
    println()
}

fun export(fileName: String) {
    var exportCards = ""
    for (card in flashcards) exportCards += card.toString()
    val myFile = File(fileName).writeText(exportCards)
    println("${flashcards.size} cards have been saved.")
    log.add("${flashcards.size} cards have been saved.")
    println()
}

fun loadCards() {
    println ("File name:")
    val fileName = scanner.nextLine()
    log.add("File name: $fileName")
    try {
        val lines = File(fileName).readLines()
        for (line in lines) {
            updateCards(line)
        }
        println("${File(fileName).readLines().size} cards have been loaded.")
        log.add("${File(fileName).readLines().size} cards have been loaded.")
    } catch (e: Exception) {
        println("File not found.")
        log.add("File not found.")
    }
    println()
}

fun import(fileName: String) {
    try {
        val lines = File(fileName).readLines()
        for (line in lines) {
            updateCards(line)
        }
        println("${File(fileName).readLines().size} cards have been loaded.")
        log.add("${File(fileName).readLines().size} cards have been loaded.")
    } catch (e: Exception) {
        println("File not found.")
        log.add("File not found.")
    }
    println()
}

fun updateCards(input: String) {
    var front = input.split(";")[0]
    var back = input.split(";")[1]
    var mistakes = input.split(";")[2].toInt()
    if (flashcards.size > 0) {
        if (findCard(front) != "") {
            for (i in 0 until flashcards.size) {
                if (flashcards[i].front == front) {
                    flashcards.removeAt(i)
                    flashcards.add(Card(front, back, mistakes))
                }
            }
        } else flashcards.add(Card(front, back, mistakes))
    } else {
        flashcards.add(Card(front, back, mistakes))
    }
}