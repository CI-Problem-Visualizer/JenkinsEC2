import analyser.Analyser
import analyser.CodeAnalysis
import objectcalisthenics.ObjectCalisthenicsConstraints
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AnalyserServer {
    fun analysis(): (Request) -> Response {
        val logger = LoggerFactory.getLogger(AnalyserServer::class.java)
        val codeAnalyses: List<CodeAnalysis> = ObjectCalisthenicsConstraints()
        val analysis = analysis(logger, Analyser(logger, codeAnalyses))
        logger.info("Running analysis application")
        return analysis
    }

    private fun analysis(
        logger: Logger,
        analyser: Analyser
    ): (Request) -> Response {
        return { request: Request ->
            try {
                logger.info(request.toString())
                val response: Response = analyser.response(request)
                logger.info(response.toString())
                response
            } catch (e: Exception) {
                logger.error("Failed to produce an analysis", e)
                Response(Status.INTERNAL_SERVER_ERROR)
            }
        }
    }
}


