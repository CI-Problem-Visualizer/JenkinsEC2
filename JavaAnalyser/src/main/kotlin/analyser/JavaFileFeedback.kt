package analyser

import java.util.*

sealed class JavaFileFeedback(
    private val feedbackJson: String
) {
    open fun addJsonValueTo(joiner: StringJoiner) {
        joiner.add("{\"feedback\": ${feedbackJson}}")
    }
}

class AllFine : JavaFileFeedback("\"All fine\"") {
    override fun addJsonValueTo(joiner: StringJoiner) {
    }
}

class RoomForImprovement(feedbackMessage: String) :
    JavaFileFeedback("\"${feedbackMessage}\"")
