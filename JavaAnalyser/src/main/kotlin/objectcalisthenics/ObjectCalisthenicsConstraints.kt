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
                // Don't abbreviate
                KeepAllEntitiesSmallConstraint(),
                NoMoreThanTwoFieldsConstraint()
                // No classes with more than two instance variables
                // No getters/setters/properties
            )
        )
    }
}