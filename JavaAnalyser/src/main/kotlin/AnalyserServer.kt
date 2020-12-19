import analyser.Analyser
import constraint.NoElseKeywordConstraint
import constraint.OneLevelOfIndentationConstraint
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AnalyserServer {
    fun app(): (Request) -> Response {
        val logger = LoggerFactory.getLogger(AnalyserServer::class.java)
        val constraints = listOf(
                OneLevelOfIndentationConstraint(),
                NoElseKeywordConstraint()
        )
        return app(logger, Analyser(logger, constraints))
    }

    private fun app(logger: Logger, analyser: Analyser): (Request) -> Response {
        logger.info("Creating analysis application")
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


