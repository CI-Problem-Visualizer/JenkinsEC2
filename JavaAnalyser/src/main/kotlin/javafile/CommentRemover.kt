package javafile

import java.util.stream.Collectors.joining

class CommentRemover {
    fun codeWithoutComments(code: String): String {
        return code.lines()
            .filter { !it.startsWith("//") }
            .map { it.removeEndOfLineDoubleSlashComment() }
            .stream().collect(joining("\n"))
            .removeBlockCommentedSections()
            .trim()
    }
}

private fun String.removeBlockCommentedSections(): String {
    var i = 0
    var regionStart = -1
    val blockCommentRegions: MutableList<Pair<Int, Int>> = mutableListOf()
    var passOver = false
    var startingSlashSeen = false
    var insideBlockComment = false
    var endingStarSeen = false
    while (i < length) {
        if (this[i] == '\"') {
            passOver = !passOver
        }
        if (passOver) {
            i++
            continue
        }
        if (this[i] == '/' && !insideBlockComment) {
            startingSlashSeen = true
        }
        if (this[i] == '*' && insideBlockComment) {
            endingStarSeen = true
        }
        if (this[i] == '*' && startingSlashSeen) {
            insideBlockComment = true
            startingSlashSeen = false
            regionStart = i
        }
        if (this[i] == '/' && endingStarSeen) {
            insideBlockComment = false
            endingStarSeen = false
            blockCommentRegions.add(Pair(regionStart, i))
            regionStart = -1
        }
        i++
    }
    if (blockCommentRegions.isEmpty()) {
        return this
    }
    var result: String = this
    blockCommentRegions.reversed().forEach {
        result = result.removeRange(it.first - 1, it.second + 1)
    }
    return result
}

private fun String.removeEndOfLineDoubleSlashComment(): String {
    if (length < 3) {
        return this
    }
    var i = 0
    var passOver = false
    var oneSlashSeen = false
    var hasEndOfLineDoubleSlashComment = false
    while (i < length) {
        if (this[i] == '\"') {
            passOver = !passOver
        }
        if (passOver) {
            i++
            continue
        }
        if (this[i] == '/' && oneSlashSeen) {
            hasEndOfLineDoubleSlashComment = true
            break
        } else if (this[i] == '/') {
            oneSlashSeen = true
        } else if (oneSlashSeen) {
            oneSlashSeen = false
        }
        i++
    }
    if (!hasEndOfLineDoubleSlashComment) {
        return this
    }
    return this.substring(0, i - 1).trimEnd()
}
