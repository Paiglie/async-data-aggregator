package org.paiglie.config

import org.koin.dsl.module
import org.paiglie.datasource.UserCommentsApiClient
import org.paiglie.datasource.UserDataApiClient
import org.paiglie.datasource.defaultThirdPartyUsersHttpClient
import org.paiglie.service.UsersService

val applicationModules = module {
    single { UsersService(get(), get()) }
    single { UserDataApiClient(get()) }
    single { UserCommentsApiClient(get()) }
    single { defaultThirdPartyUsersHttpClient() }
}
