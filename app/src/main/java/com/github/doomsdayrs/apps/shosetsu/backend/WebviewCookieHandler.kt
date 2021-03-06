package com.github.doomsdayrs.apps.shosetsu.backend

import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.*

/*
 * This file is part of Shosetsu.
 *
 * Shosetsu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shosetsu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Shosetsu.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 * shosetsu
 * 01 / 08 / 2019
 *
 */
/**
 * Provides a synchronization point between the webview cookie store and okhttp3.OkHttpClient cookie store
 */
class WebviewCookieHandler : CookieJar {
    private val webviewCookieManager = CookieManager.getInstance()
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val urlString = url.toString()
        for (cookie in cookies) {
            webviewCookieManager.setCookie(urlString, cookie.toString())
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val urlString = url.toString()
        val cookiesString = webviewCookieManager.getCookie(urlString)
        if (cookiesString != null && cookiesString.isNotEmpty()) {
            //We can split on the ';' char as the cookie manager only returns cookies
            //that match the url and haven't expired, so the cookie attributes aren't included
            val cookieHeaders = cookiesString.split(";".toRegex()).toTypedArray()
            val cookies: MutableList<Cookie> = ArrayList(cookieHeaders.size)
            for (header in cookieHeaders) {
                val c = Cookie.parse(url, header)
                if (c != null) {
                    cookies.add(c)
                }
            }
            return cookies
        }
        return emptyList()
    }
}