package objectcalisthenics

import analyser.CodeAnalysis
import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class NoMoreThanTwoFieldsConstraintTest {
    private val constraint: CodeAnalysis = NoMoreThanTwoFieldsConstraint()

    @Test
    fun `fails for single field declaration declaring three fields`() {
        assertConstraintNotMet(
            constraint,
            "no-more-than-two-fields/ThreeFieldsInOneFieldDeclaration.java"
        )
    }

    @Test
    fun `passes for classes with two fields`() {
        assertConstraintMet(
            constraint,
            "no-more-than-two-fields/TwoFields.java"
        )
    }

    @Test
    fun `fails for classes with three fields`() {
        assertConstraintNotMet(
            constraint,
            "no-more-than-two-fields/ThreeFields.java"
        )
    }
}