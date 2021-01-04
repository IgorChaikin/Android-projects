package com.laba.firebasegame

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class User(
    var nickname: String,
    var email: String,
    var isGravatar: Boolean = true,
    var wonGames: Int = 0
) : Serializable
