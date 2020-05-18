package com.haroldadmin.opengraphKt

/**
 * An inline class wrapping a Map of Open Graph tags.
 *
 * The keys in the map contain the value of the 'property' attribute from <meta> tags, and the values for each key
 * are all the <meta> tags containing this exact 'property'.
 */
inline class Tags(val map: Map<String, List<String>>) {

    /**
     * Get the first "title" og-tag from the web page, if any
     */
    val title: String?
        get() = allTitles().firstOrNull()

    /**
     * Get the first image url found in "og:image", "og:image:url" or "og:image:secure_url" on the web page, if any
     */
    val image: String?
        get() = allImages().firstOrNull()

    /**
     * Get the first url found in "og:url" on the web page
     */
    val url: String?
        get() = allUrls().firstOrNull()

    /**
     * Get the first "og:description" on the webpage, if any
     */
    val description: String?
        get() = allDescriptions().firstOrNull()

    /**
     * Get all "og:title" values on the web page.
     */
    fun allTitles(): List<String> {
        return map["title"] ?: emptyList()
    }

    /**
     * Get all the "og:image", "og:image:url" and "og:image:secure_url" tags on the web page
     */
    fun allImages(): List<String> {
        return extractImageUrl()
    }

    /**
     * Get all the "og:description" tags on the web page
     */
    fun allDescriptions(): List<String> {
        return map["description"] ?: emptyList()
    }

    /**
     * Get all the "og:url" tags on the web page
     */
    fun allUrls(): List<String> {
        return map["url"] ?: emptyList()
    }

    /**
     * Get the first occurrence of any arbitrary open-graph tag from the web page if it exists
     *
     * Example:
     * ```kotlin
     * val altText = tags.getProperty("image:alt")
     * ```
     */
    fun getProperty(name: String): String? {
        return getProperties(name).firstOrNull()
    }


    /**
     * Get all occurrences of any abitrary open-graph tag from the web page, if any exists
     *
     * Example:
     * ```kotlin
     * val locales = tags.getProperties("locale")
     * ```
     */
    fun getProperties(name: String): List<String> {
        return map[name] ?: emptyList()
    }

    private fun extractImageUrl(): List<String> {
        return map["image"] ?: map["image:url"] ?: map["image:secure_url"] ?: emptyList()
    }

    override fun toString(): String = buildString {
        map.forEach { (property, content) -> appendln("$property: $content") }
    }
}


/**
 * A helper method to merge two instances of Tags. When keys collide, it concatenates
 * the values.
 */
fun Tags.merge(other: Tags): Tags {
    val result = this.map.toMutableMap()

    other.map.entries.forEach {
        result[it.key] = (result[it.key] ?: emptyList()) + it.value
    }

    return Tags(result)
}