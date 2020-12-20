package objectcalisthenics

import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class WrapSimpleTypesConstraintTest {
    private val constraint = WrapSimpleTypesConstraint()

    @Test
    fun `detects a field where String is a type parameter in between two others`() {
        assertConstraintNotMet(
            constraint,
            "wrap-simple-types/DomainTypeAndStringValuedMiddleTypeParameter.java"
        )
    }

    @Test
    fun `detects a field which is a Map with String values`() {
        assertConstraintNotMet(
            constraint,
            "wrap-simple-types/DomainTypeAndMapWithStringValues.java"
        )
    }

    @Test
    fun `detects a field which is a Map with String keys`() {
        assertConstraintNotMet(
            constraint,
            "wrap-simple-types/DomainTypeAndMapWithStringKeys.java"
        )
    }

    @Test
    fun `fails with two String list fields`() {
        assertConstraintNotMet(
            constraint,
            "wrap-simple-types/TwoStringListFields.java"
        )
    }

    @Test
    fun `passes with domain-specific types that have String in the name`() {
        assertConstraintMet(
            constraint,
            "wrap-simple-types/FieldsWithStringInTheName.java"
        )
    }

    @Test
    fun `fails with two fields of type String`() {
        assertConstraintNotMet(
            constraint,
            "wrap-simple-types/TwoStringFields.java"
        )
    }

    @Test
    fun `passes with just one field of type String`() {
        assertConstraintMet(
            constraint,
            "wrap-simple-types/StringField.java"
        )
    }

    @Test
    fun `fails with an int field plus a domain-specific type`() {
        assertConstraintNotMet(
            constraint,
            "wrap-simple-types/IntFieldPlusDomainType.java"
        )
    }

    @Test
    fun `passes with no fields`() {
        assertConstraintMet(
            constraint,
            "wrap-simple-types/NoFields.java"
        )
    }
}