package analyser

import java.util.*

sealed class JavaFileFeedback(
    private val feedbackJson: String
) {
    open fun addJsonValueTo(joiner: StringJoiner) {
        joiner.add(feedbackJson)
    }
}

class AllFine : JavaFileFeedback("\"All fine\"") {
    override fun addJsonValueTo(joiner: StringJoiner) {
    }
}

class RoomForImprovement(private val feedbackMessage: String) :
    JavaFileFeedback("\"${feedbackMessage}\"") {

    override fun toString(): String {
        return "${this.javaClass.simpleName}<${feedbackMessage}>"
    }
}
