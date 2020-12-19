package constraint

import JavaFile
import TestDataFiles
import analyser.AllFine
import analyser.Constraint
import analyser.RoomForImprovement
import org.junit.jupiter.api.Assertions.assertTrue

object ConstraintAssertion {
    fun assertConstraintNotMet(constraint: Constraint, testDataFilePath: String) {
        assertTrue(
            evaluation(constraint, testDataFilePath) is RoomForImprovement,
            "Expected a violation, but got conformance."
        )
    }

    fun assertConstraintMet(constraint: Constraint, testDataFilePath: String) {
        assertTrue(
            evaluation(constraint, testDataFilePath) is AllFine,
            "Expected conformance, but got violation(s)."
        )
    }

    private fun evaluation(
        constraint: Constraint,
        testDataFilePath: String
    ) = constraint.evaluate(javaFile(testDataFilePath))

    private fun javaFile(testDataFilePath: String): JavaFile {
        val className = testDataFilePath.split("/").last().dropLast(5)
        return JavaFile(className, TestDataFiles.contentOf(testDataFilePath))
    }
}