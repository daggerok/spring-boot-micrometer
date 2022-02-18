package daggerok

import daggerok.dsl.count
import daggerok.dsl.tag
import daggerok.dsl.with
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class MetricsApplication

fun main(args: Array<String>) {
    runApplication<MetricsApplication>(*args)
}

@RestController
class MetricsResource(private val meterRegistry: MeterRegistry) {

    @GetMapping("/api/failed/{metric}")
    fun fail(@PathVariable metric: String) =
        meterRegistry.count {
            amount = 1
            name = "$metric-fail"
            description = "Failed counter for $metric"
            tag {
                key = "status"
                value = "failed"
            }
            with {
                println("Oops...")
            }
        }

    @GetMapping("/api/succeeded/{metric}")
    fun success(@PathVariable metric: String) =
        meterRegistry
            .count {
                name = "$metric-success"
                tag {
                    key = "status"
                    value = "succeeded"
                }
                with {
                    println("Good...")
                }
            }
            .count()
}
