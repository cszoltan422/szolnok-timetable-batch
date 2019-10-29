package org.zenbot.szolnok.timetable.backend.batch.utils.common.util

import org.springframework.stereotype.Component

/**
 * Cleans a string, replaces all the UTF-8 special characters in the string with accurate hungarian characters
 */
@Component
class StringCleaner {

    /**
     * Cleans a string, replaces all the UTF-8 special characters in the string with accurate hungarian characters
     * @param string The string to clean
     */
    fun clean(string: String): String {
        return string
                .replace("û".toRegex(), "ű")
                .replace("õ".toRegex(), "ő")
                .replace("ô".toRegex(), "ő")
                .replace("\\.".toRegex(), "")
                .trim { it <= ' ' }
                .replace(" +".toRegex(), " ")
    }
}
