package analyser

import JavaFile

fun interface Constraint {
    fun evaluate(javaFile: JavaFile): JavaFileFeedback
}