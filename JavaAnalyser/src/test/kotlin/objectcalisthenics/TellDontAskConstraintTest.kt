package objectcalisthenics

import analyser.CodeAnalysis
import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class TellDontAskConstraintTest {
    private val constraint: CodeAnalysis = TellDontAskConstraint()

    @Test
    fun `passes for method assigning parameter to field but isn't a setter`() {
        assertConstraintMet(constraint, "tell-dont-ask/ParameterNameShadowing.java")
    }

    @Test
    fun `passes for method returning a local variable shadowing a field but isn't a getter`() {
        assertConstraintMet(constraint, "tell-dont-ask/FieldNameShadowing.java")
    }

    @Test
    fun `detects a setter when the parameter name doesn't match the field name`() {
        assertConstraintNotMet(
            constraint,
            "tell-dont-ask/SetterParameterNameNotSameAsFieldName.java"
        )
    }

    @Test
    fun `passes for method which returns a field but isn't a getter`() {
        assertConstraintMet(
            constraint,
            "tell-dont-ask/ReturnsFieldButNotGetter.java"
        )
    }

    @Test
    fun `passes for method which contains an assignment but isn't a setter`() {
        assertConstraintMet(
            constraint,
            "tell-dont-ask/AssignmentToFieldButNotSetter.java"
        )
    }

    @Test
    fun `fails for class with setter`() {
        assertConstraintNotMet(constraint, "tell-dont-ask/Setter.java")
    }

    @Test
    fun `passes for method which starts with 'get' but isn't a getter`() {
        assertConstraintMet(
            constraint,
            "tell-dont-ask/MethodThatLooksLikeGetterButIsNot.java"
        )
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