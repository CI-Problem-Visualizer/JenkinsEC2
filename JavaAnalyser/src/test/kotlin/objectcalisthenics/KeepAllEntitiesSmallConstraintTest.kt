package objectcalisthenics

import analyser.CodeAnalysis
import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class KeepAllEntitiesSmallConstraintTest {
    private val constraint: CodeAnalysis = KeepAllEntitiesSmallConstraint()

    @Test
    fun `ignores comments when counting lines`() {
        assertConstraintMet(
            constraint,
            "keep-all-entities-small/ClassOverFiftyLinesWithComments.java"
        )
    }

    @Test
    fun `fails for class over 50 lines`() {
        assertConstraintNotMet(
            constraint,
            "keep-all-entities-small/ClassOverFiftyLines.java"
        )
    }
}