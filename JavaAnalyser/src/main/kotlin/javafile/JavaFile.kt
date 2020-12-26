package javafile

import com.github.javaparser.JavaParser
import com.github.javaparser.ParseException
import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.format.Jackson.auto
import java.util.*

class JavaFile(private val className: String, private val fileContent: String) {
    private val commentRemover = CommentRemover()

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

    fun fileContentWithoutComments(): String {
        return commentRemover.codeWithoutComments(fileContent)
    }

    fun className(): String {
        return className
    }

    fun parse(): ClassOrInterfaceDeclaration {
        val compilationUnit: CompilationUnit = compilationUnit()
        val optionalClassByName: Optional<ClassOrInterfaceDeclaration> =
            compilationUnit.getClassByName(className())
        if (optionalClassByName.isPresent) {
            return optionalClassByName.get()
        }
        val optionalInterfaceByName: Optional<ClassOrInterfaceDeclaration> =
            compilationUnit.getInterfaceByName(className())
        if (optionalInterfaceByName.isPresent) {
            return optionalInterfaceByName.get()
        }
        throw IllegalArgumentException("Filename doesn't match name of declared class/interface in the source")
    }

    fun fullyQualifiedClassName(): String {
        val compilationUnit: CompilationUnit = compilationUnit()
        val optionalPackageDeclaration: Optional<PackageDeclaration> =
            compilationUnit.packageDeclaration
        if (!optionalPackageDeclaration.isPresent) {
            return className
        }
        val packageDeclaration: PackageDeclaration =
            optionalPackageDeclaration.get()
        return "${packageDeclaration.nameAsString}.${className}"
    }

    private fun compilationUnit(): CompilationUnit {
        val parseResult: ParseResult<CompilationUnit> =
            JavaParser().parse(fileContentWithoutComments())
        if (!parseResult.isSuccessful) {
            throw ParseException(parseResult.toString())
        }
        val optionalResult: Optional<CompilationUnit> = parseResult.result
        if (!optionalResult.isPresent) {
            throw IllegalArgumentException(parseResult.toString())
        }
        return optionalResult.get()
    }

    fun numberOfFields(): Int =
        parse().fields.map { it.variables.count() }.sum()

    fun fieldDeclarations() = parse().fields
}
