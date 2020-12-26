import analyser.Analyser
import analyser.AnalysisReport
import analyser.JavaFileFeedback
import javafile.JavaFile
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.slf4j.LoggerFactory

class AnalyserServer(private val analyser: Analyser) {
    companion object {
        private val logger = LoggerFactory.getLogger(AnalyserServer::class.java)
    }

    fun requestHandler(): (Request) -> Response {
        logger.info("Analyser server started")
        return { request: Request ->
            try {
                logger.info(request.toString())
                val response: Response = responseFor(request)
                logger.info(response.toString())
                response
            } catch (e: Exception) {
                logger.error("Failed to produce an analysis", e)
                Response(Status.INTERNAL_SERVER_ERROR)
            }
        }
    }

    private fun responseFor(request: Request): Response {
        if (!request.body.payload.hasRemaining()) {
            return Response(Status.BAD_REQUEST)
                .body("Please include a request body")
        }

        val javaFile: JavaFile = JavaFile.from(request)
        val feedbacks: List<JavaFileFeedback> = analyser.analyse(javaFile)
        return Response(Status.OK).body(
            AnalysisReport(javaFile, feedbacks).toJson()
        )
    }
}


