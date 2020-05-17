package run.qontract.core.pattern

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import run.qontract.core.Resolver
import run.qontract.core.Result
import run.qontract.core.shouldNotMatch
import run.qontract.core.value.NullValue

internal class ListPatternTest {
    @Test
    fun `should generate a list of patterns each of which is a list pattern`() {
        val patterns = ListPattern(NumberTypePattern).newBasedOn(Row(), Resolver())

        for(pattern in patterns) {
            assertTrue(pattern is ListPattern)
        }
    }

    @Test
    fun `should fail to match nulls gracefully`() {
        NullValue shouldNotMatch ListPattern(StringPattern)
    }

    @Test
    fun `should encompass itself`() {
        val type = ListPattern(NumberTypePattern)
        assertThat(type.encompasses2(type, Resolver(), Resolver())).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `list of nullable type should encompass another list the same non-nullable type`() {
        val bigger = ListPattern(parsedPattern("""(number?)"""))
        val smallerWithNumber = ListPattern(parsedPattern("""(number)"""))
        val smallerWithNull = ListPattern(parsedPattern("""(number)"""))
        assertThat(bigger.encompasses2(smallerWithNumber, Resolver(), Resolver())).isInstanceOf(Result.Success::class.java)
        assertThat(bigger.encompasses2(smallerWithNull, Resolver(), Resolver())).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `should not encompass another list with different type`() {
        val numberPattern = ListPattern(parsedPattern("""(number?)"""))
        val stringPattern = ListPattern(parsedPattern("""(string)"""))
        assertThat(numberPattern.encompasses2(stringPattern, Resolver(), Resolver())).isInstanceOf(Result.Failure::class.java)
    }
}
