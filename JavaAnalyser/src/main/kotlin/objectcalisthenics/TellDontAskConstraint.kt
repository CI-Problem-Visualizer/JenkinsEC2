package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.body.VariableDeclarator
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
            .map { InstanceVariable(it) }
        if (javaFile.parse().methods.any {
                it.isGetter(fieldNames) or it.isSetter(fieldNames)
            }) {
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

class InstanceVariable(private val variableDeclarator: VariableDeclarator) {
    fun name(): String {
        return variableDeclarator.nameAsString
    }
}

private fun MethodDeclaration.bodyHasMoreThanOneLine() =
    toString().lines().size > 3

private fun MethodDeclaration.isGetter(fieldNames: List<InstanceVariable>): Boolean {
    if (bodyHasMoreThanOneLine()) {
        return false
    }
    val isGetterVisitor = IsGetterVisitor(fieldNames)
    accept(isGetterVisitor, null)
    return isGetterVisitor.isGetter
}

private fun MethodDeclaration.isSetter(fieldNames: List<InstanceVariable>): Boolean {
    if (bodyHasMoreThanOneLine()) {
        return false
    }
    val isSetterVisitor = IsSetterVisitor(this, fieldNames)
    accept(isSetterVisitor, null)
    return isSetterVisitor.isSetter
}

class IsGetterVisitor(
    private val instanceVariables: List<InstanceVariable>
) : VoidVisitorAdapter<Void>() {
    var isGetter = false

    override fun visit(n: ReturnStmt?, arg: Void?) {
        if (instanceVariables.any { n.toString() == "return ${it.name()};" }) {
            isGetter = true
        }
        super.visit(n, arg)
    }
}

class IsSetterVisitor(
    private val methodDeclaration: MethodDeclaration,
    private val instanceVariables: List<InstanceVariable>
) : VoidVisitorAdapter<Void>() {
    var isSetter = false

    override fun visit(n: AssignExpr?, arg: Void?) {
        val assignExpr = n!!
        val parameters: NodeList<Parameter> = methodDeclaration.parameters
        if (instanceVariables.any { field ->
                assignExpr.target.toString() == "this.${field.name()}" &&
                        parameters.any { parameter ->
                            assignExpr.value.toString() == parameter.nameAsString
                        }

            }) {
            isSetter = true
        }
        super.visit(n, arg)
    }
}
