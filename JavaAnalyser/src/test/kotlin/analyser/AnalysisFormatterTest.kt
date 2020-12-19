package analyser

import JavaFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class AnalysisFormatterTest {
    @Test
    fun `violations are included in the list of evaluations`() {
        assertThat(
            AnalysisFormatter().format(
                listOf(
                    RoomForImprovement(
                        JavaFile(
                            "SomeClass",
                            "public class SomeClass {}"
                        ), "Oh no!"
                    )
                )
            ),
            equalTo(
                "{" +
                        "\"evaluations\": [{" +
                        "\"className\": \"SomeClass\", " +
                        "\"feedback\": \"Oh no!\"" +
                        "}]" +
                        "}"
            )
        )
    }

    @Test
    fun `conformant files are empty objects in the list of evaluations`() {
        assertThat(
            AnalysisFormatter().format(
                listOf(
                    AllFine(
                        JavaFile(
                            "SomeClass",
                            "public class SomeClass {}"
                        )
                    )
                )
            ),
            equalTo(
                "{" +
                        "\"evaluations\": [{" +
                        "\"className\": \"SomeClass\", " +
                        "\"feedback\": \"All fine\"" +
                        "}]" +
                        "}"
            )
        )
    }

    @Test
    fun `empty list`() {
        assertThat(
            AnalysisFormatter().format(emptyList()),
            equalTo("{\"evaluations\": []}")
        )
    }
}