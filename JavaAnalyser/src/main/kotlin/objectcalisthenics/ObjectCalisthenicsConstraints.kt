package objectcalisthenics

import analyser.CodeAnalysis

object ObjectCalisthenicsConstraints : ArrayList<CodeAnalysis>() {
    init {
        addAll(
            listOf(
                OneLevelOfIndentationConstraint(),
                NoElseKeywordConstraint(),
                WrapSimpleTypesConstraint(),
                FirstClassCollectionsConstraint(),
                OneDotPerLineConstraint(),
                KeepAllEntitiesSmallConstraint(),
                NoMoreThanTwoFieldsConstraint(),
                TellDontAskConstraint()
            )
        )
    }
}