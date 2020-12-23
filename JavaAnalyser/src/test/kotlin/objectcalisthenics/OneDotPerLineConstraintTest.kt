package objectcalisthenics

import analyser.CodeAnalysis
import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class OneDotPerLineConstraintTest {
    private val constraint: CodeAnalysis = OneDotPerLineConstraint()

    @Test
    fun `passes if there is no law of demeter violation`() {
        assertConstraintMet(constraint, "one-dot-per-line/AllOk.java")
    }

    @Test
    fun `fails for a nested property access`() {
        assertConstraintNotMet(
            constraint,
            "one-dot-per-line/NestedPropertyAccess.java"
        )
    }
}