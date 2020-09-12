package app.shosetsu.android.datasource.remote.model

import app.shosetsu.android.common.consts.ErrorKeys
import app.shosetsu.android.common.consts.repoFolderStruct
import app.shosetsu.android.common.dto.HResult
import app.shosetsu.android.common.dto.errorResult
import app.shosetsu.android.common.dto.successResult
import app.shosetsu.android.common.ext.quickie
import app.shosetsu.android.datasource.remote.base.IRemoteExtRepoDataSource
import app.shosetsu.android.domain.model.local.RepositoryEntity
import okhttp3.OkHttpClient
import org.json.JSONObject

/*
 * This file is part of shosetsu.
 *
 * shosetsu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * shosetsu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with shosetsu.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * shosetsu
 * 13 / 05 / 2020
 */
class RemoteExtRepoDataSource(
		private val client: OkHttpClient,
) : IRemoteExtRepoDataSource {
	override suspend fun downloadRepoData(
			repo: RepositoryEntity,
	): HResult<JSONObject> = try {
		@Suppress("BlockingMethodInNonBlockingContext")
		(successResult(
				JSONObject(
						client.quickie(
								"${repo.url}${repoFolderStruct}index.json"
						).body!!.string()
				)
		))
	} catch (e: Exception) {
		errorResult(ErrorKeys.ERROR_GENERAL, e.message ?: "Unknown general error")
	}

}