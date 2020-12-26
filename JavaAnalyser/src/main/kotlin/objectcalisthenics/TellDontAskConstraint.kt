package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.ReturnStmt
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class TellDontAskConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (javaFile.fieldDeclarations().any {
                it.hasModifier(Modifier.publicModifier().keyword)
            }) {
            return RoomForImprovement("")
        }

        val fieldNames = javaFile.fieldDeclarations()
            .flatMap { it.variables }
            .map { it.nameAsString }
        if (javaFile.parse().methods.any { it.isGetterOrSetter(fieldNames) }) {
            return RoomForImprovement("")
        }

        return AllFine()
    }
}

private fun MethodDeclaration.isGetterOrSetter(fieldNames: List<String>): Boolean {
    val isGetterOrSetterVisitor = IsGetterOrSetterVisitor(fieldNames)
    this.accept(isGetterOrSetterVisitor, null)
    return isGetterOrSetterVisitor.isGetterOrSetter
}

class IsGetterOrSetterVisitor(
    private val fieldNames: List<String>
) : VoidVisitorAdapter<Void>() {
    var isGetterOrSetter = false

    override fun visit(n: ReturnStmt?, arg: Void?) {
        if (fieldNames.any { n.toString() == "return $it;" }) {
            isGetterOrSetter = true
        }
        super.visit(n, arg)
    }
}
