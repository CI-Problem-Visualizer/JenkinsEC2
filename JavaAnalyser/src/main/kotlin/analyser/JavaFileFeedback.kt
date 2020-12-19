package analyser

import JavaFile
import java.util.*

sealed class JavaFileFeedback(
    private val javaFile: JavaFile,
    private val feedbackMessage: String
) {
    fun addJsonValueTo(joiner: StringJoiner) {
        joiner.add("{\"className\": \"${javaFile.fullyQualifiedClassName()}\", \"feedback\": ${feedbackMessage}}")
    }
}

class AllFine(javaFile: JavaFile) :
    JavaFileFeedback(javaFile, "\"All fine\"")

class RoomForImprovement(
    javaFile: JavaFile,
    feedbackMessage: String
) : JavaFileFeedback(javaFile, "\"${feedbackMessage}\"")
