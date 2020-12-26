package analyser

import javafile.JavaFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class AnalysisReportTest {
    @Test
    fun `room for improvement feedback is included in the list of feedbacks`() {
        assertThat(
            AnalysisReport(
                JavaFile("SomeClass", "public class SomeClass {}"),
                listOf(RoomForImprovement("Oh no!"))
            ).toJson(),
            equalTo(
                "{" +
                        "\"fullyQualifiedClassName\": \"SomeClass\", " +
                        "\"feedbacks\": [\"Oh no!\"]" +
                        "}"
            )
        )
    }

    @Test
    fun `files which are all fine don't generate noisy extra feedback`() {
        assertThat(
            AnalysisReport(
                JavaFile("SomeClass", "public class SomeClass {}"),
                listOf(AllFine())
            ).toJson(),
            equalTo(
                "{" +
                        "\"fullyQualifiedClassName\": \"SomeClass\", " +
                        "\"feedbacks\": []" +
                        "}"
            )
        )
    }

    @Test
    fun `feedbacks json array is empty if there is no feedback reported`() {
        assertThat(
            AnalysisReport(
                JavaFile("SomeClass", "public class SomeClass {}"),
                emptyList()
            ).toJson(),
            equalTo(
                "{" +
                        "\"fullyQualifiedClassName\": \"SomeClass\", " +
                        "\"feedbacks\": []" +
                        "}"
            )
        )
    }
}