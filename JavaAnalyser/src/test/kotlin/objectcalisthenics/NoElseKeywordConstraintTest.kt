package objectcalisthenics

import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class NoElseKeywordConstraintTest {
    private val constraint = NoElseKeywordConstraint()

    @Test
    fun `ignores the word else in comments`() {
        assertConstraintMet(constraint, "no-else-keyword/ElseInComments.java")
    }

    @Test
    fun `passes if there's no else statement`() {
        assertConstraintMet(constraint, "no-else-keyword/AllOk.java")
    }

    @Test
    fun `finds 'else' statement`() {
        assertConstraintNotMet(constraint, "no-else-keyword/HasElse.java")
    }
}
