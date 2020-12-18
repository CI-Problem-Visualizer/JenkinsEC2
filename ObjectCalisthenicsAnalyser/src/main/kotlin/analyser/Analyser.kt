package analyser

import JavaFile
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.slf4j.Logger

class Analyser(private val logger: Logger, private val constraints: List<Constraint>) {
    fun analyse(javaFile: JavaFile): List<ConstraintEvaluation> {
        val evaluations = mutableListOf<ConstraintEvaluation>()
        for (constraint in constraints) {
            evaluations.add(constraint.evaluate(javaFile))
        }
        return evaluations
    }

    fun response(request: Request): Response =
            if (!request.body.payload.hasRemaining()) {
                Response(Status.BAD_REQUEST).body("Please include a request body")
            } else {
                val javaFile: JavaFile = JavaFile.from(request)
                logger.info(javaFile.toString())
                val evaluations: List<ConstraintEvaluation> = analyse(javaFile)
                logger.info(evaluations.toString())
                Response(Status.OK).body(AnalysisFormatter().format(evaluations))
            }
}
