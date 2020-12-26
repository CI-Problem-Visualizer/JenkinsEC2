import javafile.CommentRemover
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CommentRemoverTest {
    private val commentRemover: CommentRemover = CommentRemover()

    @Test
    fun `ignores block comment that is only at the start of the line`() {
        assertEquals(
            "return new MouseWheel();",
            commentRemover.codeWithoutComments(
                "/*something in a commment*/return new MouseWheel();"
            )
        )
    }

    @Test
    fun `ignores multi-line multiplication operation`() {
        val code = "int x = 5\n" +
                "* (4 +\n" +
                "j * 3);"
        assertEquals(code, commentRemover.codeWithoutComments(code))
    }

    @Test
    fun `removes multiple block comments spanning multiple lines`() {
        assertEquals(
            "X x = new X();  return x.something(5);",
            commentRemover.codeWithoutComments(
                "X /*first*/x = new/*second*/ X(); /*third\n" +
                        "* spanning many lines\n" +
                        "* and ending abruptly\n" +
                        "with some code */ return x.something(/*\n" +
                        "there's a number here at the end*/5);"
            )
        )
    }

    @Test
    fun `removes multiple block comments`() {
        assertEquals(
            "X x = new X();",
            commentRemover.codeWithoutComments(
                "X /*first*/x = new/*second*/ X(); /*third*/"
            )
        )
    }

    @Test
    fun `ignores quoted block comment`() {
        assertEquals(
            "X x = new X(\"But /* here's a block comment in a String */\");",
            commentRemover.codeWithoutComments(
                "X x = new X(/* This should be removed*/\"But /* here's a block comment in a String */\");"
            )
        )
    }

    @Test
    fun `removes one-line block comment`() {
        assertEquals(
            "X x = new X();",
            commentRemover.codeWithoutComments(
                "X x/* This is a comment */ = new X();"
            )
        )
    }

    @Test
    fun `removes javadoc comment`() {
        assertEquals(
            "X x = new X();",
            commentRemover.codeWithoutComments(
                "/**\n" +
                        "* This is a javadoc comment\n" +
                        "* It's great\n" +
                        "*/" +
                        "X x = new X();"
            )
        )
    }

    @Test
    fun `ignores quoted double slash`() {
        assertEquals(
            "X x = new X(\"A // dodgy // string\");",
            commentRemover.codeWithoutComments(
                "X x = new X(\"A // dodgy // string\"); // An actual comment"
            )
        )
    }

    @Test
    fun `removes double-slash end-of-line comments`() {
        assertEquals(
            "X x = new X();",
            commentRemover.codeWithoutComments(
                "X x = new X(); // Some commented code"
            )
        )
    }

    @Test
    fun `removes double-slash whole-line comments`() {
        assertEquals(
            "X x = new X();",
            commentRemover.codeWithoutComments(
                "// Some commented code\n" +
                        "X x = new X();"
            )
        )
    }
}