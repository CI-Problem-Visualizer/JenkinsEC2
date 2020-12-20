package objectcalisthenics

import JavaFile
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement

class WrapSimpleTypesConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        return RoomForImprovement("Wrap primitives and Strings")
    }
}