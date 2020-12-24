package objectcalisthenics

import JavaFile
import analyser.AllFine
import analyser.CodeAnalysis
import analyser.JavaFileFeedback
import analyser.RoomForImprovement

class NoMoreThanTwoFieldsConstraint : CodeAnalysis {
    override fun evaluate(javaFile: JavaFile): JavaFileFeedback {
        if (javaFile.numberOfFields() > 2) {
            return RoomForImprovement(
                "This class has more than two fields, which indicates that " +
                        "it might have quite a diverse set of " +
                        "responsibilities. It may improve the flexibility " +
                        "and reuse in the code if you decompose this class " +
                        "and reduce the number of fields, which may lead to " +
                        "simpler, more composed object models."
            )
        }
        return AllFine()
    }
}
