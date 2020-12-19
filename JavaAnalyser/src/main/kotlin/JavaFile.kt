import com.github.javaparser.JavaParser
import com.github.javaparser.ParseException
import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.format.Jackson.auto
import java.util.*

class JavaFile(private val className: String, private val fileContent: String) {
    companion object {
        fun from(request: Request): JavaFile {
            val jacksonSafeRequestBody = request.bodyString()
                .replace("\n", "")
            return Body.auto<JavaFile>().toLens()(
                Request(
                    request.method,
                    request.uri
                ).body(jacksonSafeRequestBody)
            )
        }
    }

    fun fileContent(): String {
        return fileContent
    }

    fun className(): String {
        return className
    }

    fun parse(): ClassOrInterfaceDeclaration {
        val parseResult: ParseResult<CompilationUnit> =
            JavaParser().parse(fileContent())
        if (!parseResult.isSuccessful) {
            throw ParseException(parseResult.toString())
        }
        val optionalResult: Optional<CompilationUnit> = parseResult.result
        if (!optionalResult.isPresent) {
            throw IllegalArgumentException(parseResult.toString())
        }
        val compilationUnit: CompilationUnit = optionalResult.get()
        val optionalClassByName: Optional<ClassOrInterfaceDeclaration> =
            compilationUnit.getClassByName(className())
        if (!optionalClassByName.isPresent) {
            throw IllegalArgumentException("Filename doesn't match name of declared class in source")
        }
        return optionalClassByName.get()
    }

    /**
     * This method is here because JavaParser fails when trying to parse the Java file in
     * `src/test/resources/test-data-files/one-level-of-indentation/TwoSingleIfsBracesInComments`, which I believe is
     * due to its inability to correctly parse the comments, as indicated by the error message:
     * (line 1,col 102) Parse error. Found <EOF>, expected "}"
     */
    fun withoutClassComment(): JavaFile {
        val fileContent = fileContent()
        val lines = fileContent.lines()
        val lineIsntClassDeclaration: (String) -> Boolean = { line ->
            listOf(
                "class",
                "interface",
                "enum",
                "abstract",
                "final",
                "private",
                "public",
                "protected"
            )
                .none { line.trim().startsWith(it) }
        }
        return JavaFile(
            className,
            lines.dropWhile(lineIsntClassDeclaration).joinToString("\n")
        )
    }
}
