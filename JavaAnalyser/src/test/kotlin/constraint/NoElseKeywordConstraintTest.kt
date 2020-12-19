package constraint

import constraint.ConstraintAssertion.assertConstraintMet
import constraint.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class NoElseKeywordConstraintTest {
    private val constraint = NoElseKeywordConstraint()

    @Test
    fun `passes if there's no else statement`() {
        assertConstraintMet(constraint, "no-else-keyword/AllOk.java")
    }

    @Test
    fun `finds 'else' statement`() {
        assertConstraintNotMet(constraint, "no-else-keyword/HasElse.java")
    }
}
