package daggerok

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry

object dsl {

    data class CountDsl(
        var name: String = "",
        var baseUnit: String = "amount",
        var description: String = "",
        var tags: List<String> = listOf(),
        var amount: Number = 1,
    )

    data class TagDsl(
        var key: String = "",
        var value: String = "",
    )

    /**
     * Usage:
     *
     *  meterRegistry.count {
     *      // ...skipped
     *      tag {
     *          key = "tag-key"
     *          value = "tag-value"
     *      }
     *  }
     */
    fun CountDsl.tag(definitionOf: TagDsl.() -> Unit = {}) =
        TagDsl()
            .apply { definitionOf(this) }
            .apply { tags += listOf(key, value) }

    /**
     * Usage:
     *
     *  meterRegistry.count {
     *      // ...skipped
     *      with {
     *          println("Incrementing metric...")
     *          // anything can be executed here (consumer)
     *      }
     *  }
     */
    fun CountDsl.with(f: (Any) -> Unit = {}) =
        f.invoke(this)

    /**
     * Usage:
     *
     *  val myMetric = meterRegistry.count {
     *      amount = 1
     *      name = "my.metric.name"
     *      description = "Metric description"
     *      tag {
     *          key = "tag-key"
     *          value = "tag-value"
     *      }
     *      with {
     *          // anything can be run here (consumer)
     *      }
     *  }
     */
    fun MeterRegistry.count(patch: CountDsl.() -> Unit = {}) =
        CountDsl()
            .apply { patch(this) }
            .let {
                val (name, baseUnit, description, tags, amount) = it
                if (name.isBlank()) throw RuntimeException("Metric name is required")
                val validParsedTags = if (tags.size % 2 != 0) arrayOf() else tags.toTypedArray()
                Counter.builder(name)
                    .baseUnit(baseUnit)
                    .description(description)
                    .tags(*validParsedTags)
                    .register(this)
                    .apply { increment(amount.toDouble()) }
            }
}
