package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model

import com.google.gson.annotations.SerializedName

data class AiChatBotRequestBodyModel(
    @SerializedName("max_tokens") val maxTokens: Int? = null,
    @SerializedName("model") val model: String? = null,
    @SerializedName("prompt") val prompt: String? = null,
    @SerializedName("temperature") val temperature: Double? = null
)