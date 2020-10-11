package flashcards

import java.text.SimpleDateFormat
import java.util.*

class Log {
    var body = ""
    fun add(line: String) {
        val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = dateFormat.format(Date())
        body += "\"$currentDate\", \"$line\"\n"
    }
}