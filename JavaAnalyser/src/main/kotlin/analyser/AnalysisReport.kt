package analyser

import JavaFile
import java.util.*

class AnalysisReport(
    private val javaFile: JavaFile,
    private val feedbacks: List<JavaFileFeedback>
) {
    fun toJson(): String {
        val joiner = StringJoiner(",")
        for (feedback in feedbacks) {
            feedback.addJsonValueTo(joiner)
        }
        return "{" +
                "\"fullyQualifiedClassName\": \"${javaFile.fullyQualifiedClassName()}\", " +
                "\"feedbacks\": [${joiner}]" +
                "}"
    }
}