package pub.edholm.irc2rss.domain

import java.util.*

class ReleasesList(private val maxSize: Int = 100) : List<Release> {
  private val backingStore: LinkedList<Release> = LinkedList()

  init {
    if (maxSize < 1) {
      throw IllegalArgumentException("Max size must be >= 1, got $maxSize")
    }
  }

  fun add(r: Release) {
    if (backingStore.size == maxSize) {
      backingStore.removeAt(0)
    }
    backingStore.add(r)
  }

  override fun contains(element: Release): Boolean = backingStore.contains(element)

  override fun containsAll(elements: Collection<Release>): Boolean = backingStore.containsAll(elements)

  override fun get(index: Int): Release = backingStore[index]

  override fun indexOf(element: Release): Int = backingStore.indexOf(element)

  override fun isEmpty(): Boolean = backingStore.isEmpty()

  override fun iterator(): Iterator<Release> = backingStore.iterator()

  override fun lastIndexOf(element: Release): Int = backingStore.lastIndexOf(element)

  override fun listIterator(): ListIterator<Release> = backingStore.listIterator()

  override fun listIterator(index: Int): ListIterator<Release> = backingStore.listIterator(index)

  override fun subList(fromIndex: Int, toIndex: Int): List<Release> = backingStore.subList(fromIndex, toIndex)

  override val size: Int
    get() = backingStore.size
}