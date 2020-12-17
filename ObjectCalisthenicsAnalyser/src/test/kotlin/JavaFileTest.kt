import com.github.javaparser.ast.AccessSpecifier
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Body
import org.http4k.core.Method
import org.junit.jupiter.api.Test
import org.http4k.core.Request.Companion as Request

class JavaFileTest {
    @Test
    fun `auto-marshals correctly from request`() {
        val body = Body(
            "{" +
                    "\"className\": \"MyClass\", " +
                    "\"fileContent\": \"public class MyClass {}\"" +
                    "}"
        )
        val javaFile: JavaFile = JavaFile.from(
            Request(Method.POST, "http://localhost:9000/object-calisthenics-report").body(body)
        )
        assertThat(javaFile.className(), equalTo("MyClass"))
        assertThat(javaFile.fileContent(), equalTo("public class MyClass {}"))
        assertThat(javaFile.parse().accessSpecifier, equalTo(AccessSpecifier.PUBLIC))
    }
}