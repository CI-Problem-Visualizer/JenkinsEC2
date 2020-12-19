import com.github.javaparser.ast.AccessSpecifier
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Body
import org.http4k.core.Method
import org.junit.jupiter.api.Test
import org.http4k.core.Request.Companion as Request

class JavaFileTest {
    @Test
    fun `can parse interface`() {
        val javaFile = JavaFile(
            "StatementPrinter", "package com.bilgin.accounting;\n" +
                    "\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "public interface StatementPrinter {\n" +
                    "    void print(AccountStatement statement);\n" +
                    "\n" +
                    "    void printTransactions(List<Transaction> transactions);\n" +
                    "\n" +
                    "    void printTotalBalance(Amount amount);\n" +
                    "\n" +
                    "    void printRaw(String text);\n" +
                    "}"
        )
        assertThat(
            javaFile.parse().accessSpecifier,
            equalTo(AccessSpecifier.PUBLIC)
        )
    }

    @Test
    fun `can parse class with escaped newline characters inside quotes`() {
        val javaFile = JavaFile(
            "ConsolePrinter", "package com.bilgin.accounting;\n" +
                    "\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "public class ConsolePrinter implements StatementPrinter {\n" +
                    "    private final StringBuilder stringBuilder;\n" +
                    "\n" +
                    "    public ConsolePrinter(StringBuilder stringBuilder) {\n" +
                    "        this.stringBuilder = stringBuilder;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void print(AccountStatement statement) {\n" +
                    "        printRaw(\"---------------Statement--------------------\");\n" +
                    "        newLine();\n" +
                    "        statement.print(this);\n" +
                    "    }\n" +
                    "\n" +
                    "    public void printTransactions(List<Transaction> transactions) {\n" +
                    "        printRaw(\"---------------Transactions-----------------\");\n" +
                    "        newLine();\n" +
                    "        for (Transaction transaction : transactions) {\n" +
                    "            printTransaction(transaction);\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    private void printTransaction(Transaction transaction) {\n" +
                    "        transaction.print(this);\n" +
                    "        newLine();\n" +
                    "    }\n" +
                    "\n" +
                    "    public void printTotalBalance(Amount amount) {\n" +
                    "        printRaw(\"---------------Total Balance----------------\");\n" +
                    "        newLine();\n" +
                    "        amount.print(this);\n" +
                    "        newLine();\n" +
                    "    }\n" +
                    "\n" +
                    "    public void printRaw(String text) {\n" +
                    "        stringBuilder.append(text);\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    private void newLine() {\n" +
                    "        stringBuilder.append(\"\\n\");\n" +
                    "    }\n" +
                    "}"
        )
        assertThat(
            javaFile.parse().accessSpecifier,
            equalTo(AccessSpecifier.PUBLIC)
        )
    }

    @Test
    fun `can retrieve fully qualified class name without package declaration`() {
        val javaFile = JavaFile(
            "SomeClass", "public class SomeClass {\n" +
                    "    public int someMethod() {\n" +
                    "        return 5;\n" +
                    "    }\n" +
                    "}"
        )
        assertThat(
            javaFile.fullyQualifiedClassName(),
            equalTo("SomeClass")
        )
    }

    @Test
    internal fun `can retrieve fully qualified class name using package declaration`() {
        val javaFile = JavaFile(
            "SomeClass", "package com.rob;\n" +
                    "public class SomeClass {\n" +
                    "    public int someMethod() {\n" +
                    "        return 5;\n" +
                    "    }\n" +
                    "}"
        )
        assertThat(
            javaFile.fullyQualifiedClassName(),
            equalTo("com.rob.SomeClass")
        )
    }

    @Test
    fun `can parse when there are class comments`() {
        val javaFile = JavaFile(
            "ElseInComments", "/**\n" +
                    " * some comments\n" +
                    " * the word else in a comment\n" +
                    " */\n" +
                    "public class ElseInComments {\n" +
                    "    // the word else\n" +
                    "    public int noProblem() {\n" +
                    "        return 5;\n" +
                    "    }\n" +
                    "}"
        )
        assertThat(
            javaFile.parse().accessSpecifier,
            equalTo(AccessSpecifier.PUBLIC)
        )
    }

    @Test
    fun `auto-marshals correctly from request needing character escaping`() {
        val body = Body(
            "{" +
                    "\"className\": \"Account\", " +
                    "\"fileContent\": \"" +
                    "package com.bilgin.accounting;\n" +
                    "\n" +
                    "public class Amount {\n" +
                    "    private final int value;\n" +
                    "\n" +
                    "    public Amount(int value) {\n" +
                    "        this.value = value;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public boolean equals(Object o) {\n" +
                    "        if (this == o) return true;\n" +
                    "        if (!(o instanceof Amount)) return false;\n" +
                    "\n" +
                    "        Amount amount = (Amount) o;\n" +
                    "\n" +
                    "        return value == amount.value;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public int hashCode() {\n" +
                    "        return value;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void print(StatementPrinter printer) {\n" +
                    "        printer.printRaw(\\\"Amount: \\\" + value);\n" +
                    "    }\n" +
                    "\n" +
                    "    public Amount sum(Amount amount) {\n" +
                    "        return new Amount(value + amount.value);\n" +
                    "    }\n" +
                    "\n" +
                    "    public Amount subtract(Amount amount) {\n" +
                    "        return new Amount(value - amount.value);\n" +
                    "    }\n" +
                    "}" +
                    "\"}\n"
        )
        val javaFile: JavaFile = JavaFile.from(
            Request(
                Method.POST,
                "http://localhost:9000/object-calisthenics-report"
            ).body(body)
        )
        assertThat(javaFile.className(), equalTo("Account"))
    }

    @Test
    fun `auto-marshals correctly from request`() {
        val body = Body(
            "{" +
                    "\"className\": \"MyClass\", " +
                    "\"fileContent\": \"public class MyClass {}\"" +
                    "}"
        )
        val javaFile: JavaFile = JavaFile.from(
            Request(
                Method.POST,
                "http://localhost:9000/object-calisthenics-report"
            ).body(body)
        )
        assertThat(javaFile.className(), equalTo("MyClass"))
        assertThat(javaFile.fileContent(), equalTo("public class MyClass {}"))
        assertThat(
            javaFile.parse().accessSpecifier,
            equalTo(AccessSpecifier.PUBLIC)
        )
    }
}