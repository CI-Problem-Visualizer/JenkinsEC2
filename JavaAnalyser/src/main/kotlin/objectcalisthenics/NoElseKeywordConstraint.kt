package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class NoElseKeywordConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (usesElseKeyword(javaFile)) {
            return RoomForImprovement(
                "This code uses the 'else' keyword. There may be a more " +
                        "scalable way to handle this conditional case, such " +
                        "as using polymorphism, or the 'Null Object' design " +
                        "pattern."
            )
        }
        return AllFine()
    }

    private fun usesElseKeyword(javaFile: JavaFile): Boolean {
        if (!javaFile.fileContent().contains("else")) {
            return false
        }
        val methodContainsElseKeyword: (MethodDeclaration) -> Boolean = {
            val findElseKeywordVisitor = FindElseKeywordVisitor()
            it.accept(findElseKeywordVisitor, null)
            findElseKeywordVisitor.foundElseKeyword
        }
        return javaFile.parse().methods.any(methodContainsElseKeyword)
    }

    /**
     * This class is a visitor which is used to assess whether a given method
     * contains the 'else' keyword.
     */
    private class FindElseKeywordVisitor : VoidVisitorAdapter<Void>() {
        var foundElseKeyword = false

        override fun visit(n: IfStmt?, arg: Void?) {
            if (n?.hasElseBranch() == true) {
                foundElseKeyword = true
            }
            super.visit(n, arg)
        }
    }
}
