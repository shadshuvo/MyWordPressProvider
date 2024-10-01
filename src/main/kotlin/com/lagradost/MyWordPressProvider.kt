package com.lagradost

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@CloudstreamPlugin
class MyWordPressProviderPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(MyWordPressProvider())
    }
}

class MyWordPressProvider : MainAPI() {
    override val mainUrl = "https://dl.freedrivemovie.com"
    override val name = "MyWordPressProvider"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun load(url: String): LoadResponse {
        val response = app.get("$mainUrl/wp-json/my-custom-api/v1/posts").text
        val jsonArray = JSONArray(response)
        val movies = mutableListOf<MovieLoadResponse>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            val link = jsonObject.getString("link")

            movies.add(newMovieLoadResponse(title, link, TvType.Movie, link) {
                this.plot = "No plot available"
                this.year = null
                this.posterUrl = null
            })
        }

        return movies.firstOrNull() ?: throw ErrorLoadingException("No movies found")
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document: Document = app.get(data).document
        val buttons = document.select("div.inside-article div.is-content-justification-left.is-layout-flex.wp-container-1.wp-block-buttons div.wp-block-button a.wp-block-button__link")

        for (button in buttons) {
            val link = button.attr("href")
            val quality = button.text()
            callback.invoke(
                ExtractorLink(
                    source = name,
                    name = quality,
                    url = link,
                    isM3u8 = false,
                    quality = getQualityFromName(quality)
                )
            )
        }
        return true
    }

    private fun getQualityFromName(name: String): Int {
        return when {
            name.contains("1080", true) -> Qualities.P1080
            name.contains("720", true) -> Qualities.P720
            name.contains("480", true) -> Qualities.P480
            name.contains("360", true) -> Qualities.P360
            else -> Qualities.Unknown
        }
    }
}