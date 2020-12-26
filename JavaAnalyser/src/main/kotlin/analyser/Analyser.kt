package analyser

import javafile.JavaFile

class Analyser(private val codeAnalyses: List<CodeAnalysis>) {
    fun analyse(javaFile: JavaFile): List<JavaFileFeedback> {
        val feedbacks = mutableListOf<JavaFileFeedback>()
        for (analysis in codeAnalyses) {
            feedbacks.add(analysis.evaluate(javaFile))
        }
        return feedbacks
    }
}
