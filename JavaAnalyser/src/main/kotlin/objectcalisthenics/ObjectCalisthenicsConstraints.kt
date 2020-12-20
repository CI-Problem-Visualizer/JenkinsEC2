package objectcalisthenics

import analyser.CodeAnalysis

class ObjectCalisthenicsConstraints : ArrayList<CodeAnalysis>() {
    init {
        addAll(
            listOf(
                OneLevelOfIndentationConstraint(),
                NoElseKeywordConstraint(),
                WrapSimpleTypesConstraint()
                // First class collections
                // One dot per line
                // Don't abbreviate
                // Keep all entities small
                // No classes with more than two instance variables
                // No getters/setters/properties
            )
        )
    }
}