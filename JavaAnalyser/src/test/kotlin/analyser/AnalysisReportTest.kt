package analyser

import JavaFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class AnalysisReportTest {
    @Test
    fun `violations are included in the list of evaluations`() {
        assertThat(
            AnalysisReport(
                JavaFile("SomeClass", "public class SomeClass {}"),
                listOf(RoomForImprovement("Oh no!"))
            ).toJson(),
            equalTo(
                "{" +
                        "\"fullyQualifiedClassName\": \"SomeClass\", " +
                        "\"evaluations\": [{" +
                        "\"feedback\": \"Oh no!\"" +
                        "}]" +
                        "}"
            )
        )
    }

    @Test
    fun `conformant files are empty objects in the list of evaluations`() {
        assertThat(
            AnalysisReport(
                JavaFile("SomeClass", "public class SomeClass {}"),
                listOf(AllFine())
            ).toJson(),
            equalTo(
                "{" +
                        "\"fullyQualifiedClassName\": \"SomeClass\", " +
                        "\"evaluations\": [{" +
                        "\"feedback\": \"All fine\"" +
                        "}]" +
                        "}"
            )
        )
    }

    @Test
    fun `empty list`() {
        assertThat(
            AnalysisReport(
                JavaFile("SomeClass", "public class SomeClass {}"),
                emptyList()
            ).toJson(),
            equalTo(
                "{" +
                        "\"fullyQualifiedClassName\": \"SomeClass\", " +
                        "\"evaluations\": []" +
                        "}"
            )
        )
    }
}