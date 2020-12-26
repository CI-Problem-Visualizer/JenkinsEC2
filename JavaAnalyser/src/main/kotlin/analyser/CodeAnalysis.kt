package analyser

import javafile.JavaFile

fun interface CodeAnalysis {
    fun evaluate(javaFile: JavaFile): JavaFileFeedback
}