package org.paiglie.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun ObjectMapper.applyDefaultConfiguration() =
    registerKotlinModule()
