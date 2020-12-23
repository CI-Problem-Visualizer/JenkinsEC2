package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement

class OneDotPerLineConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (javaFile.fileContentWithoutComments().lines()
                .any { line -> line.filter { it == '.' }.length > 1 }
        ) {
            return RoomForImprovement(
                "This class contains a line of source code with more " +
                        "than one dot on it, which possibly indicates the " +
                        "presence of feature envy. Code like this causes " +
                        "coupling between clients of an interface, and the " +
                        "interface's implementation, which reduces the " +
                        "code's flexibility to changing requirements. You " +
                        "may be able to address this using the 'move method' " +
                        "or/and the 'extract method' refactorings."
            )
        }
        return AllFine()
    }
}
