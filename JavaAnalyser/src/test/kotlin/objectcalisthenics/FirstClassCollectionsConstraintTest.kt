package objectcalisthenics

import analyser.CodeAnalysis
import objectcalisthenics.ConstraintAssertion.assertConstraintMet
import objectcalisthenics.ConstraintAssertion.assertConstraintNotMet
import org.junit.jupiter.api.Test

class FirstClassCollectionsConstraintTest {
    private val constraint: CodeAnalysis = FirstClassCollectionsConstraint()

    @Test
    fun `treats arrays as collection types`() {
        assertConstraintNotMet(constraint, "first-class-collections/ArrayField.java")
    }

    @Test
    fun `detects collection types with no type parameters specified`() {
        assertConstraintNotMet(constraint, "first-class-collections/NonGenericCollection.java")
    }

    @Test
    fun `passes when there are no collection fields`() {
        assertConstraintMet(constraint, "first-class-collections/NoCollectionFields.java")
    }

    @Test
    fun `fails for collection with a list and another field`() {
        assertConstraintNotMet(constraint, "first-class-collections/TwoFields.java")
    }
}