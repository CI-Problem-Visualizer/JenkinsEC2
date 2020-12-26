package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AssignExpr
import com.github.javaparser.ast.stmt.ReturnStmt
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class TellDontAskConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (javaFile.fieldDeclarations().any {
                it.hasModifier(Modifier.publicModifier().keyword)
            }) {
            return RoomForImprovement(
                "This class has at least one public field. This means that " +
                        "the class is exposing its internal implementation " +
                        "details to its collaborators, which means that you " +
                        "might be missing out on the flexibility that is " +
                        "created by the use of strong encapsulation. " +
                        "Consider making the field(s) private, and instead " +
                        "exposing public methods for behaviours that require " +
                        "their use."
            )
        }

        val fieldNames = javaFile.fieldDeclarations()
            .flatMap { it.variables }
            .map { it.nameAsString }
        if (javaFile.parse().methods.any { it.isGetterOrSetter(fieldNames) }) {
            return RoomForImprovement(
                "This class appears to have at least one getter/setter " +
                        "method. This is an example of the class exposing " +
                        "its internal implementation details to its " +
                        "collaborators, which means that you might be " +
                        "missing out on the flexibility that is created by " +
                        "using strong encapsulation. Consider moving the " +
                        "behaviour for which getter/setter methods are " +
                        "required, into the class which exposes the " +
                        "getter/setter methods."
            )
        }

        return AllFine()
    }
}

private fun MethodDeclaration.isGetterOrSetter(fieldNames: List<String>): Boolean {
    val isGetterVisitor = IsGetterVisitor(fieldNames)
    this.accept(isGetterVisitor, null)
    if (isGetterVisitor.isGetter) {
        return true
    }

    val isSetterVisitor = IsSetterVisitor(fieldNames)
    this.accept(isSetterVisitor, null)
    return isSetterVisitor.isSetter
}

class IsGetterVisitor(
    private val fieldNames: List<String>
) : VoidVisitorAdapter<Void>() {
    var isGetter = false

    override fun visit(n: ReturnStmt?, arg: Void?) {
        if (fieldNames.any { n.toString() == "return $it;" }) {
            isGetter = true
        }
        super.visit(n, arg)
    }
}

class IsSetterVisitor(
    private val fieldNames: List<String>
) : VoidVisitorAdapter<Void>() {
    var isSetter = false

    override fun visit(n: AssignExpr?, arg: Void?) {
        val assignExpr = n!!
        if (fieldNames.any {
                assignExpr.value.toString() == it &&
                        assignExpr.target.toString() == "this.$it"
            }) {
            isSetter = true
        }
        super.visit(n, arg)
    }
}
