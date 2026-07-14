package com.med.scheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MedSchedulerApplication

fun main(args: Array<String>) {
    runApplication<MedSchedulerApplication>(*args)
}
