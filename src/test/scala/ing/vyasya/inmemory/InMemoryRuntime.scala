package ing.vyasya.inmemory

import ing.vyasya.domain.{FileOperation, LineResponse}
import zio.internal.{Platform, PlatformLive}

// In-memory runtime required for the tests
trait InMemoryRuntime extends zio.Runtime[LineResponse with FileOperation] {
  override val environment: LineResponse with FileOperation = InMemoryEnvironment
  override val platform: Platform = PlatformLive.Default
}

object InMemoryEnvironment extends LineResponse with FileOperation {
  override val response: LineResponse.Service = InMemoryLineResponseService
  override val operation: FileOperation.Service = InMemoryFileResponseService
}