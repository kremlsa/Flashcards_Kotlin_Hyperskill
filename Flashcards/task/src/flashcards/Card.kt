package flashcards

class Card {
    var front = ""
    var back = ""
    var mistakes = 0

    constructor(front: String, back: String, mistakes: Int) {
        this.front = front
        this.back = back
        this.mistakes = mistakes
    }

    fun addMistake() = mistakes++

    override fun toString(): String {
        return "$front;$back;$mistakes\n"
    }
}