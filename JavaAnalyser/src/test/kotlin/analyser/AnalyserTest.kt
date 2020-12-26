package analyser

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.isEmpty
import javafile.JavaFile
import org.junit.jupiter.api.Test

class AnalyserTest {

    private val javaFile = JavaFile(
        "EmptyClass.java",
        "class EmptyClass {\n}"
    )


    @Test
    fun `can report that everything is fine`() {
        val feedback = AllFine()
        val analyser = Analyser(listOf(CodeAnalysis { feedback }))
        assertThat(analyser.analyse(javaFile), hasElement(feedback))
    }

    @Test
    fun `reports violations when given`() {
        val feedback = RoomForImprovement("Oh no!")
        val analyser = Analyser(listOf(CodeAnalysis { feedback }))
        assertThat(analyser.analyse(javaFile), hasElement(feedback))
    }

    @Test
    fun `doesn't create report entries if there are no constraints`() {
        val analyser = Analyser(emptyList())
        assertThat(analyser.analyse(javaFile), isEmpty)
    }
}