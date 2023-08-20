package com.kunalfarmah.apps.readerapp.model

data class MUser(
    val id: String?,
    val uid: String,
    val name: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String
) {
    fun toMap(): MutableMap<String, Any> =
        mutableMapOf(
            "user_id" to this.uid,
            "display_name" to this.name,
            "quote" to this.quote,
            "avatar_url" to this.avatarUrl,
            "profession" to this.profession
        )

}
