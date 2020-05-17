package run.qontract.core.pattern

import run.qontract.core.Resolver
import run.qontract.core.Result
import run.qontract.core.mismatchResult
import run.qontract.core.value.Value

data class ExactValuePattern(override val pattern: Value) : Pattern {
    override fun matches(sampleData: Value?, resolver: Resolver): Result {
        return when (pattern == sampleData) {
            true -> Result.Success()
            else -> mismatchResult(pattern, sampleData)
        }
    }

    override fun encompasses2(otherPattern: Pattern, thisResolver: Resolver, otherResolver: Resolver): Result {
        if(otherPattern !is ExactValuePattern || this.pattern != otherPattern.pattern)
            return Result.Failure("Expected ${this.typeName}, got ${otherPattern.typeName}")

        return Result.Success()
    }

    override fun generate(resolver: Resolver) = pattern
    override fun newBasedOn(row: Row, resolver: Resolver): List<Pattern> = listOf(this)
    override fun parse(value: String, resolver: Resolver): Value = pattern.type().parse(value, resolver)
    override fun encompasses(otherPattern: Pattern, resolver: Resolver): Boolean =
            otherPattern is ExactValuePattern && this.pattern == otherPattern.pattern

    override val typeName: String = pattern.displayableValue()

    override fun toString(): String = pattern.toStringValue()
}
