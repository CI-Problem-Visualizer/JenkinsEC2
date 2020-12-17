package analyser

import JavaFile

class Analyser(private val constraints: List<Constraint>) {
    fun analyse(javaFile: JavaFile): List<ConstraintEvaluation> {
        val evaluations = mutableListOf<ConstraintEvaluation>()
        for (constraint in constraints) {
            evaluations.add(constraint.evaluate(javaFile))
        }
        return evaluations
    }
}