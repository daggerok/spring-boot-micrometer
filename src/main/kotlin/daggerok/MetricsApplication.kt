package daggerok

import io.micrometer.core.instrument.Counter
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

    @GetMapping("/api/failed/{name}")
    fun fail(@PathVariable name: String) =
        Counter.builder("$name-fail")
            .baseUnit("none")
            .description("Failed counter for $name")
            .tags("status", "failed")
            .register(meterRegistry) // job-1-failed, job-2-failed, ...
            .increment()

    @GetMapping("/api/succeeded/{name}")
    fun success(@PathVariable name: String) =
        meterRegistry.counter("$name-success", "status", "succeeded") // job-1-success
            .apply { increment() }
            .count()
}
