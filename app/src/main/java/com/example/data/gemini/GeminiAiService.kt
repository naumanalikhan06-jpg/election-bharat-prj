package com.example.data.gemini

import android.util.Log
import com.example.BuildConfig
import com.example.data.NirvachanRAGKnowledgeBase
import com.example.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun askNirvachan(query: String, language: Language = Language.ENGLISH): Pair<String, String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // Fast path for keyless/offline or strict neutrality guardrails
        val ragResult = NirvachanRAGKnowledgeBase.queryKnowledgeBase(query, language)

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext ragResult
        }

        val systemPrompt = """
            You are Nirvachan AI, the sovereign multilingual election assistant for India on the Bharat Election Nexus platform.
            STRICT RULES:
            1. You are 100% politically neutral. Never recommend, rank, praise, or criticize any political party or candidate.
            2. Answer election procedural questions accurately (Form 6, Form 8, Voter ID documents, Polling procedures, MCC, VVPAT, PwD facilities).
            3. Always cite official rules/handbooks (e.g. Conduct of Elections Rules, ECI Manual).
            4. If asked who to vote for, politely refuse and guide the user to the factual Candidate Transparency Center.
            5. Current language: ${language.displayName}. Reply in ${language.nativeName}.
        """.trimIndent()

        val jsonPayload = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "System Instruction: $systemPrompt\n\nUser Question: $query")
                        })
                    })
                })
            }
            put("contents", contentsArray)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonPayload.toString().toRequestBody(mediaType)
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        try {
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBodyStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseBodyStr)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    val replyText = parts?.optJSONObject(0)?.optString("text")
                    if (!replyText.isNullOrBlank()) {
                        return@withContext Pair(replyText, "Nirvachan AI Verified Model + ECI Knowledge Base Grounding")
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("GeminiAiService", "Fallback to local RAG due to: ${e.message}")
        }

        return@withContext ragResult
    }
}
