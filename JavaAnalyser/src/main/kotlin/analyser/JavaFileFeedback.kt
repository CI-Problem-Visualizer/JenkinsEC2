package analyser

import java.util.*

sealed class JavaFileFeedback(
    private val feedbackJson: String
) {
    fun addJsonValueTo(joiner: StringJoiner) {
        joiner.add("{\"feedback\": ${feedbackJson}}")
    }
}

class AllFine : JavaFileFeedback("\"All fine\"")

class RoomForImprovement(feedbackMessage: String) :
    JavaFileFeedback("\"${feedbackMessage}\"")
