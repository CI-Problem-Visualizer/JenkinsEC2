package constraint

import constraint.ConstraintAssertion.assertConstraintMet
import constraint.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class OneLevelOfIndentationConstraintTest {
    private val constraint = OneLevelOfIndentationConstraint()

    @Test
    fun `passes when 'if' statement is followed by 'for' loop`() {
        assertConstraintMet(constraint, "one-level-of-indentation/IfThenFor.java")
    }

    @Test
    fun `finds an 'if' statement placed inside a 'for' loop`() {
        assertConstraintNotMet(constraint, "one-level-of-indentation/IfInFor.java")
    }

    @Test
    fun `finds nested 'if' statements even when they don't have braces`() {
        assertConstraintNotMet(constraint, "one-level-of-indentation/NestedIfsNoBraces.java")
    }

    @Test
    fun `ignores comments of all kinds in the source code`() {
        assertConstraintMet(constraint, "one-level-of-indentation/BracesInComments.java")
    }

    @Test
    fun `passes for a single method employing multiple 'if' statements`() {
        assertConstraintMet(constraint, "one-level-of-indentation/TwoSingleIfs.java")
    }

    @Test
    fun `passes source code with no nesting at all`() {
        assertConstraintMet(constraint, "one-level-of-indentation/AllOk.java")
    }

    @Test
    fun `finds nested 'if' statements in a method`() {
        assertConstraintNotMet(constraint, "one-level-of-indentation/NestedIfs.java")
    }
}