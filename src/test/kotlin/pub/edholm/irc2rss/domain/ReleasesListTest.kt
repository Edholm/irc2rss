package pub.edholm.irc2rss.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ReleasesListTest {

  @Test
  internal fun `create list with zero max size`() {
    val e = assertThrows<IllegalArgumentException> {
      ReleasesList(0)
    }
    assertThat(e).hasMessage("Max size must be >= 1, got 0")
  }

  @Test
  internal fun `max size is respected`() {
    val l = ReleasesList(1)
    assertThat(l).isEmpty()

    val r1 = Release()
    l.add(r1)

    assertThat(l).isNotEmpty.hasSize(1).contains(r1)

    val r2 = Release()
    l.add(r2)

    assertThat(l).isNotEmpty.hasSize(1).contains(r2).doesNotContain(r1)
  }
}