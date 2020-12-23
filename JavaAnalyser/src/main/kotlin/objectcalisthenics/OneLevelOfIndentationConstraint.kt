package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.util.stream.Collectors.joining

class OneLevelOfIndentationConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (hasMoreThanOneLevelOfIndentation(javaFile)) {
            return RoomForImprovement(
                "This code has more than one level of indentation. This may " +
                        "be an indication that you have methods which are " +
                        "doing more than one thing, which limits your " +
                        "possibility to re-use them, and also makes it " +
                        "harder to come up with intention-revealing names " +
                        "for them. Consider extracting behaviour out from " +
                        "these methods."
            )
        }

        return AllFine()
    }

    private fun hasMoreThanOneLevelOfIndentation(javaFile: JavaFile): Boolean {
        val allBraces: String = extractSemanticBlocksOf(javaFile)
        var nestingLevel = 0
        for (i in allBraces.indices) {
            if (allBraces[i] == '{') nestingLevel++
            if (allBraces[i] == '}') nestingLevel--
            if (nestingLevel > 3) return true
        }
        return hasMoreThanOneLevelOfIndentationInBlocksWithoutBraces(javaFile)
    }

    private fun extractSemanticBlocksOf(javaFile: JavaFile): String {
        return javaFile.fileContentWithoutComments().filter { it in "{}" }
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

            // This is here because when there are multiple if-else statements
            // in a cascade, then they are visited in order and the program
            // would otherwise consider each of them as their own nesting level.
            // This conditional statement prevents that.
            if (n?.hasCascadingIfStmt() == true) {
                decreaseIndentationLevel()
            }

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
}

