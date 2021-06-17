package br.com.shido.spacexkmm

import br.com.shido.spacexkmm.cache.Database
import br.com.shido.spacexkmm.cache.DatabaseDriverFactory
import br.com.shido.spacexkmm.entity.RocketLaunch
import br.com.shido.spacexkmm.network.SpaceXApi

class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}


