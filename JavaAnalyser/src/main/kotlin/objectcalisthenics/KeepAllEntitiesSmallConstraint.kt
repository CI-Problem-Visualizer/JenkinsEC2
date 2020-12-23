package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement

class KeepAllEntitiesSmallConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (javaFile.fileContentWithoutComments().lines().size > 50) {
            return RoomForImprovement(
                "This class is over 50 lines long. You may want to consider " +
                        "making it shorter by extracting new classes to " +
                        "represent more fine-grained elements of your " +
                        "problem domain, which will allow your program's " +
                        "to scale up more gracefully as your representation " +
                        "of the problem domain becomes richer."
            )
        }

        return AllFine()
    }
}
