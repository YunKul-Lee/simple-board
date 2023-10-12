package com.jake.simpleboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class SimpleBoardApplication

fun main(args: Array<String>) {
    runApplication<SimpleBoardApplication>(*args)
}
