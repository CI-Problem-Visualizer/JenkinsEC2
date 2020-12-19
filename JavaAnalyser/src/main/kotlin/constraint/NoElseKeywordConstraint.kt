package constraint

import JavaFile
import analyser.Conformant
import analyser.Constraint
import analyser.ConstraintEvaluation
import analyser.Violation

class NoElseKeywordConstraint : Constraint {
    override fun evaluate(javaFile: JavaFile): ConstraintEvaluation {
        if (usesElseKeyword(javaFile)) {
            return Violation(javaFile.className(), "Uses the 'else' keyword.")
        }
        return Conformant()
    }

    // TODO: Ignore comments and strings
    // TODO: Ignore names of classes, methods, variables etc.
    private fun usesElseKeyword(javaFile: JavaFile): Boolean {
        return javaFile.fileContent().contains("else")
    }
}
