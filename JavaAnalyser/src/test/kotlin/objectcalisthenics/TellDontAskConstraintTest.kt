package objectcalisthenics

import analyser.CodeAnalysis
import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class TellDontAskConstraintTest {
    private val constraint: CodeAnalysis = TellDontAskConstraint()

    @Test
    fun `passes for method which starts with 'get' but isn't a getter`() {
        assertConstraintMet(constraint, "tell-dont-ask/MethodThatLooksLikeGetterButIsNot.java")
    }

    @Test
    fun `fails for class with getter`() {
        assertConstraintNotMet(constraint, "tell-dont-ask/Getter.java")
    }

    @Test
    fun `passes for class with private properties without getters or setters`() {
        assertConstraintMet(constraint, "tell-dont-ask/AllOk.java")
    }

    @Test
    fun `fails for class with public field`() {
        assertConstraintNotMet(constraint, "tell-dont-ask/PublicField.java")
    }
}