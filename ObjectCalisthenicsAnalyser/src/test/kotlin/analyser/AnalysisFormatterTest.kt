package analyser

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class AnalysisFormatterTest {
    @Test
    fun `violations are included in the list of evaluations`() {
        assertThat(
            AnalysisFormatter().format(listOf(Violation("MyClass", "Oh no!"))),
            equalTo("{\"evaluations\": [{\"fileName\": \"MyClass\", \"violationMessage\": \"Oh no!\"}]}")
        )
    }

    @Test
    fun `conformant files are empty objects in the list of evaluations`() {
        assertThat(
            AnalysisFormatter().format(listOf(Conformant())),
            equalTo("{\"evaluations\": [{}]}")
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