package com.raeanandres.kmm.shared

import com.raeanandres.kmm.shared.cache.Database
import com.raeanandres.kmm.shared.cache.DatabaseDriverFactory
import com.raeanandres.kmm.shared.entity.RocketLaunch
import com.raeanandres.kmm.shared.network.SpaceXApi

class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {

    private val database = Database(databaseDriverFactory)
    private val spaceXApi = SpaceXApi()




    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            spaceXApi.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }

}