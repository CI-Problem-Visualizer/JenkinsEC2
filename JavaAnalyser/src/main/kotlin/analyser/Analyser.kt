package analyser

import JavaFile
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.slf4j.Logger

class Analyser(
    private val logger: Logger,
    private val codeAnalyses: List<CodeAnalysis>
) {
    fun analyse(javaFile: JavaFile): List<JavaFileFeedback> {
        val feedbacks = mutableListOf<JavaFileFeedback>()
        for (analysis in codeAnalyses) {
            feedbacks.add(analysis.evaluate(javaFile))
        }
        return feedbacks
    }

    fun response(request: Request): Response =
        if (!request.body.payload.hasRemaining()) {
            Response(Status.BAD_REQUEST).body("Please include a request body")
        } else {
            val javaFile: JavaFile = JavaFile.from(request)
            logger.info(javaFile.toString())
            val feedbacks: List<JavaFileFeedback> = analyse(javaFile)
            logger.info(feedbacks.toString())
            Response(Status.OK).body(
                AnalysisReport(
                    javaFile,
                    feedbacks
                ).toJson()
            )
        }
}
