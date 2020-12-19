package constraint

import JavaFile
import analyser.AllFine
import analyser.Constraint
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class NoElseKeywordConstraint : Constraint {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (usesElseKeyword(javaFile)) {
            return RoomForImprovement(javaFile, "Uses the 'else' keyword.")
        }
        return AllFine(javaFile)
    }

    private fun usesElseKeyword(javaFile: JavaFile): Boolean {
        if (!javaFile.fileContent().contains("else")) {
            return false
        }
        val classByName: ClassOrInterfaceDeclaration = javaFile.parse()
        val methodContainsElseKeyword: (MethodDeclaration) -> Boolean = {
            val findElseKeywordVisitor = FindElseKeywordVisitor()
            it.accept(findElseKeywordVisitor, null)
            findElseKeywordVisitor.foundElseKeyword
        }
        return classByName.methods.any(methodContainsElseKeyword)
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
