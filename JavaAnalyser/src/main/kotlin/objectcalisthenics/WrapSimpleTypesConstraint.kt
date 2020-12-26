package objectcalisthenics

import javafile.JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.type.PrimitiveType
import com.github.javaparser.ast.type.Type

class WrapSimpleTypesConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (javaFile.numberOfFields() < 2) {
            return AllFine()
        }
        if (javaFile.fieldDeclarations().any(this::hasSimpleType)) {
            return RoomForImprovement(
                "This code uses primitive type fields " +
                        "without wrapping them into domain-specific types. " +
                        "This means you lose the opportunity to express the " +
                        "intention of those fields using types which are " +
                        "specific to your problem domain. Having small, " +
                        "domain-specific types also makes it easy to find " +
                        "an intuitive home for all of your 'helper' " +
                        "functions, in a way that makes them easier to find " +
                        "and prevent coupling between behaviour which is " +
                        "unrelated in the domain, which would otherwise " +
                        "surface as a result of the growth of these " +
                        "'Helper' or 'Utils' classes."
            )
        }
        return AllFine()
    }

    private fun hasSimpleType(f: FieldDeclaration): Boolean {
        val fieldType = f.elementType
        return fieldType.isPrimitiveType or
                PrimitiveType.Primitive.values()
                    .map { it.toBoxedType().name.asString() }
                    .plus("String")
                    .any { fieldType.hasElementTypeName(it) }
    }
}

private fun Type.hasElementTypeName(elementTypeName: String): Boolean {
    val name = toString().filter { !it.isWhitespace() }
    if (name == elementTypeName) {
        return true
    }
    return name.contains("<$elementTypeName>") ||
            name.contains("<$elementTypeName,") ||
            name.contains(",$elementTypeName>") ||
            name.contains(",$elementTypeName,")
}