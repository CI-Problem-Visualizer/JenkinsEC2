import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.client.ApacheClient
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("unused")
class MainTest {
    private val client = ApacheClient()
    private val server = createObjectCalisthenicsServer(0)

    @BeforeEach
    fun setup() {
        server.start()
    }

    @AfterEach
    fun teardown() {
        server.stop()
    }

    private fun remoteSocket() = RemoteSocket.get()
    private fun localSocket() = "localhost:${server.port()}"
    private fun socket() = localSocket()

    @Test
    fun `without a request body the response is bad request`() {
        assertThat(
                client(Request(POST, "http://${socket()}/code-analysis")),
                hasStatus(BAD_REQUEST))
    }

    @Test
    fun `given a sensible request the response is ok`() {
        val body = Body("{" +
                "\"className\": \"MyClass\", " +
                "\"fileContent\": \"public class MyClass {}\"" +
                "}")
        assertThat(
                client(Request(POST, "http://${socket()}/code-analysis").body(body)),
                hasStatus(OK))
    }
}