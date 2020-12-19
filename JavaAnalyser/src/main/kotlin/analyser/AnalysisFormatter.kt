package analyser

import java.util.*

class AnalysisFormatter {
    fun format(feedbacks: List<JavaFileFeedback>): String {
        val joiner = StringJoiner(",")
        for (feedback in feedbacks) {
            feedback.addJsonValueTo(joiner)
        }
        return "{\"evaluations\": [$joiner]}"
    }
}
