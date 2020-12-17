import analyser.Analyser
import analyser.AnalysisFormatter
import constraint.NoElseKeywordConstraint
import constraint.OneLevelOfIndentationConstraint
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer

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
    return { request: Request ->
        if (!request.body.payload.hasRemaining()) {
            Response(BAD_REQUEST).body("Please include a request body")
        } else {
            Response(OK).body(analysisFormatter.format(analyser.analyse(JavaFile.from(request))))
        }
    }
}
