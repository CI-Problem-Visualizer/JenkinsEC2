package analyser

import java.util.*

class AnalysisFormatter {
    fun format(evaluations: List<ConstraintEvaluation>): String {
        val joiner = StringJoiner(",")
        for (evaluation in evaluations) {
            evaluation.addJsonValueTo(joiner)
        }
        return "{\"evaluations\": [$joiner]}"
    }
}
