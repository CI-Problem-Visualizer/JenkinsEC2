package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.type.Type

class WrapSimpleTypesConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        val classOrInterfaceDeclaration = javaFile.parse()
        val fields: MutableList<FieldDeclaration> =
            classOrInterfaceDeclaration.fields
        if (fields.size != 2) {
            return AllFine()
        }
        if (fields.any(this::hasSimpleType)) {
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
        val elementType = f.elementType
        return isPrimitiveType(elementType) or isStringType(elementType)
    }

    private fun isPrimitiveType(elementType: Type) =
        elementType.isPrimitiveType

    private fun isStringType(type: Type): Boolean {
        val typeName = type.toString().filter { !it.isWhitespace() }
        if (typeName == "String") {
            return true
        }
        return typeName.contains("<String>") ||
                typeName.contains("<String,") ||
                typeName.contains(",String>") ||
                typeName.contains(",String,")
    }
}