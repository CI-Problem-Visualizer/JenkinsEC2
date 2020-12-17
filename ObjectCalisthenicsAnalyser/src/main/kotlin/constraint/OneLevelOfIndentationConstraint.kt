package constraint

import JavaFile
import analyser.Conformant
import analyser.Constraint
import analyser.ConstraintEvaluation
import analyser.Violation
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.util.stream.Collectors.joining


class OneLevelOfIndentationConstraint : Constraint {
    override fun evaluate(javaFile: JavaFile): ConstraintEvaluation {
        if (hasMoreThanOneLevelOfIndentation(javaFile)) {
            return Violation(javaFile.className(), "More that one level of indentation.")
        }

        return Conformant()
    }

    // TODO: Ignore strings and comments
    // TODO: Handle blocks without braces
    private fun hasMoreThanOneLevelOfIndentation(javaFile: JavaFile): Boolean {
        val allBraces: String = extractSemanticBlocksOf(javaFile)
        var nestingLevel = 0
        for (i in allBraces.indices) {
            if (allBraces[i] == '{') nestingLevel++
            if (allBraces[i] == '}') nestingLevel--
            if (nestingLevel > 3) return true
        }
        // This code is here due to the fact that JavaParser can't seem to parse class comments.
        if (hasClassComment(javaFile)) {
            return false
        }
        return hasMoreThanOneLevelOfIndentationInBlocksWithoutBraces(javaFile)
    }

    private fun extractSemanticBlocksOf(javaFile: JavaFile): String {
        val fileContent = javaFile.fileContent()
        val isCommentedLine: (String) -> Boolean = { line ->
            val trimmedLine = line.trim()
            listOf("//", "*", "/*").none { trimmedLine.startsWith(it) } && !trimmedLine.endsWith("*/")
        }
        val fileContentWithoutComments =
            fileContent.lines().filter(isCommentedLine).stream().collect(joining())
        return fileContentWithoutComments.filter { it in "{}" }
    }

    private fun hasMoreThanOneLevelOfIndentationInBlocksWithoutBraces(javaFile: JavaFile): Boolean {
        val classByName: ClassOrInterfaceDeclaration = javaFile.parse()
        val methodContainsNestedBlocks: (MethodDeclaration) -> Boolean = {
            val nestedBlocksVisitor = NestedBlocksVisitor()
            it.accept(nestedBlocksVisitor, null)
            nestedBlocksVisitor.foundNestedBlock
        }
        return classByName.methods.any(methodContainsNestedBlocks)
    }

    /**
     * This class is a visitor which is used to assess whether a given method contains more than one level of
     * indentation. It visits the parsed nodes in order. The 'super' call in these methods involves descending further
     * into the syntax tree, which means greater levels of nesting. This is detected with a simple counter, and any
     * excessive nesting is recorded by a one-way assignment to the 'foundNestedBlock' field.
     */
    private class NestedBlocksVisitor : VoidVisitorAdapter<Void>() {
        private var indentationLevel = 0

        var foundNestedBlock = false

        private fun increaseIndentationLevel() {
            indentationLevel++
            if (indentationLevel > 1) {
                foundNestedBlock = true
            }
        }

        private fun decreaseIndentationLevel() {
            indentationLevel--
        }

        override fun visit(n: TryStmt?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }

        override fun visit(n: CatchClause?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }

        override fun visit(n: IfStmt?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }

        override fun visit(n: ForStmt?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }

        override fun visit(n: ForEachStmt?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }

        override fun visit(n: DoStmt?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }

        override fun visit(n: WhileStmt?, arg: Void?) {
            increaseIndentationLevel()
            super.visit(n, arg)
            decreaseIndentationLevel()
        }
    }

    /**
     * This method is here because JavaParser fails when trying to parse the Java file in
     * `src/test/resources/test-data-files/one-level-of-indentation/TwoSingleIfsBracesInComments`, which I believe is
     * due to its inability to correctly parse the comments, as indicated by the error message:
     * (line 1,col 102) Parse error. Found <EOF>, expected "}"
     */
    private fun hasClassComment(javaFile: JavaFile): Boolean {
        val fileContent = javaFile.fileContent()
        val lines = fileContent.split("\r\n")
        val lineHasClassOrInterfaceDeclaration: (String) -> Boolean = { line ->
            listOf("class", "interface", "enum", "abstract", "final", "private", "public", "protected")
                .none { line.trim().startsWith(it) }
        }
        val linesBeforeTheClassDeclaration = lines.takeWhile(lineHasClassOrInterfaceDeclaration)
        val isCommentedLine: (String) -> Boolean = { line ->
            listOf("//", "*", "/*").none { line.startsWith(it) } && !line.endsWith("*/")
        }
        return linesBeforeTheClassDeclaration.any(isCommentedLine)
    }
}

