import analyser.Analyser
import analyser.AnalysisFormatter
import analyser.ConstraintEvaluation
import constraint.NoElseKeywordConstraint
import constraint.OneLevelOfIndentationConstraint
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ObjectCalisthenicsServer {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            createObjectCalisthenicsServer(9000).start()
        }
    }
}

fun createObjectCalisthenicsServer(port: Int): Http4kServer =
        routes("/object-calisthenics-report" bind defaultApp()).asServer(Netty(port))

private fun defaultApp(): (Request) -> Response {
    val constraints = listOf(
            OneLevelOfIndentationConstraint(),
            NoElseKeywordConstraint()
    )
    return app(Analyser(constraints), AnalysisFormatter())
}

private fun app(analyser: Analyser, analysisFormatter: AnalysisFormatter): (Request) -> Response {
    val logger: Logger = LoggerFactory.getLogger(Http4kServer::class.java)
    logger.info("Creating analysis application")
    return { request: Request ->
        try {
            logger.info(request.toString())
            val response: Response = response(logger, request, analyser, analysisFormatter)
            logger.info(response.toString())
            response
        } catch (e: Exception) {
            logger.error("Failed to produce an analysis", e)
            Response(INTERNAL_SERVER_ERROR)
        }
    }
}

private fun response(logger: Logger,
                     request: Request,
                     analyser: Analyser,
                     analysisFormatter: AnalysisFormatter): Response =
        if (!request.body.payload.hasRemaining()) {
            Response(BAD_REQUEST).body("Please include a request body")
        } else {
            val javaFile: JavaFile = JavaFile.from(request)
            logger.info(javaFile.toString())
            val evaluations: List<ConstraintEvaluation> = analyser.analyse(javaFile)
            logger.info(evaluations.toString())
            Response(OK).body(analysisFormatter.format(evaluations))
        }
