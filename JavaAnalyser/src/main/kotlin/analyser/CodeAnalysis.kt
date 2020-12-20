package analyser

import JavaFile

fun interface CodeAnalysis {
    fun evaluate(javaFile: JavaFile): JavaFileFeedback
}