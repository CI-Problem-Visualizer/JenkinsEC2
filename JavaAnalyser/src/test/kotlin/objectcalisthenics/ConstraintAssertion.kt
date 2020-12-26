package objectcalisthenics

import javafile.JavaFile
import TestDataFiles
import analyser.AllFine
import analyser.RoomForImprovement
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import org.junit.jupiter.api.Assertions.assertTrue

object ConstraintAssertion {
    fun assertConstraintNotMet(
        codeAnalysis: CodeAnalysis,
        testDataFilePath: String
    ) {
        val feedback: JavaFileFeedback =
            evaluation(codeAnalysis, testDataFilePath)
        assertTrue(
            feedback is RoomForImprovement,
            "Expected ${RoomForImprovement::class}, but got ${feedback::class}."
        )
    }

    fun assertConstraintMet(
        codeAnalysis: CodeAnalysis,
        testDataFilePath: String
    ) {
        val feedback: JavaFileFeedback =
            evaluation(codeAnalysis, testDataFilePath)
        assertTrue(
            feedback is AllFine,
            "Expected ${AllFine::class}, but got ${feedback::class}.\n" +
                    "The full return value was $feedback"
        )
    }

    private fun evaluation(
        codeAnalysis: CodeAnalysis,
        testDataFilePath: String
    ) = codeAnalysis.evaluate(javaFile(testDataFilePath))

    private fun javaFile(testDataFilePath: String): JavaFile {
        val className = testDataFilePath.split("/").last().dropLast(5)
        return JavaFile(className, TestDataFiles.contentOf(testDataFilePath))
    }
}