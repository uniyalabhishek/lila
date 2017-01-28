package lila.memo

import com.github.blemale.scaffeine.{ Cache, Scaffeine }
import scala.concurrent.duration.Duration

final class ExpireSetMemo(ttl: Duration) {

  private val cache: Cache[String, Boolean] = Scaffeine()
    .expireAfterWrite(ttl)
    .build[String, Boolean]

  private def isNotNull[A](a: A) = a != null

  def get(key: String): Boolean = isNotNull(cache.underlying getIfPresent key)

  def put(key: String) = cache.put(key, true)

  def putAll(keys: Iterable[String]) = cache putAll keys.map(_ -> true).toMap

  def remove(key: String) = cache invalidate key

  def keys: Iterable[String] = cache.asMap.keys

  def keySet: Set[String] = keys.toSet

  def count = cache.estimatedSize.toInt
}
