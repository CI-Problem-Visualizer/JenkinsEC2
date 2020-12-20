package analyser

import JavaFile
import TestDataFiles
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.isEmpty
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AnalyserTest {

    private val logger: Logger =
        LoggerFactory.getLogger(AnalyserTest::class.java)
    private val javaFile = JavaFile(
        "EmptyClass.java",
        "class EmptyClass {\n}"
    )


    @Test
    fun `can report that everything is fine`() {
        val feedback = AllFine()
        val analyser = Analyser(logger, listOf(CodeAnalysis { feedback }))
        assertThat(analyser.analyse(javaFile), hasElement(feedback))
    }

    @Test
    fun `reports violations when given`() {
        val feedback = RoomForImprovement("Oh no!")
        val analyser = Analyser(logger, listOf(CodeAnalysis { feedback }))
        assertThat(analyser.analyse(javaFile), hasElement(feedback))
    }

    @Test
    fun `doesn't create report entries if there are no constraints`() {
        val analyser = Analyser(logger, emptyList())
        assertThat(analyser.analyse(javaFile), isEmpty)
    }
}