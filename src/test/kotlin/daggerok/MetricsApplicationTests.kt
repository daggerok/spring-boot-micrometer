package daggerok

import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.util.function.Supplier
import kotlin.random.Random

class SlowService {
    fun getValue() = Random(1234).nextInt(1000, 2000)
}

@SpringBootTest
class MetricsApplicationTests {

    @Test
    fun `test meter registry`() {
        // given
        val meterRegistry = CompositeMeterRegistry()
        meterRegistry.config().commonTags("username", System.getProperty("user.name"))
        meterRegistry.counter("users").increment(3.0)
        // when
        val slowService = SlowService()
        meterRegistry.gauge("ping", slowService) { it.getValue().toDouble() }
        meterRegistry.gauge("pong", slowService) { slowService.getValue().toDouble() }
        // and
        meterRegistry.timer("processing-job").record(Duration.ofMillis(123))
        meterRegistry.timer("processing-job").record { println("Hello, World!") }
        val greeting = meterRegistry.timer("processing-job").record(Supplier {
            Thread.sleep(1234)
            "Hello"
        })
        // then
        assertThat(greeting).isEqualTo("Hello")
        // and
        val usersCounter = meterRegistry.get("users").counter()
        println("${usersCounter.id}: ${usersCounter.increment()}")
        // and
        val gauge = meterRegistry.get("ping").gauge()
        println("ping: ${gauge.id} ${gauge.id.type}")
        println("pong: ${meterRegistry.get("pong").gauge().value()}")
        println("processing-job: ${meterRegistry.get("processing-job").timers()}")
    }
}
