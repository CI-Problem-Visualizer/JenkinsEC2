package analyser

import java.util.*

sealed class ConstraintEvaluation {
    // By giving the {@link StringJoiner} to the ConstraintEvaluation implementations, they are free to add or not add
    // their contribution to the class. This provides greater flexibility for different behaviours.
    abstract fun addJsonValueTo(joiner: StringJoiner)
}

/**
 * I'm suppressing this, because in the future I want to be able to provide not only negative feedback in the form of
 * object calisthenics violations, but also positive feedback in the conformant section on code that displays
 * particular qualities that are amenable to flexible and reusable design.
 */
@Suppress("CanSealedSubClassBeObject")
class Conformant : ConstraintEvaluation() {
    override fun addJsonValueTo(joiner: StringJoiner) {
        joiner.add("{}")
    }
}

class Violation(private val fileName: String, private val violationMessage: String) : ConstraintEvaluation() {
    override fun addJsonValueTo(joiner: StringJoiner) {
        joiner.add("{\"fileName\": \"$fileName\", \"violationMessage\": \"$violationMessage\"}")
    }
}
