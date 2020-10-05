package ing.vyasya.specs
import ing.vyasya.domain.LineResponse
import ing.vyasya.inmemory.InMemoryLineResponseService

// Unit specs for in-memory line response service
class InMemoryLineResponseSpec extends LineResponseSpec {

  behavior of "InMemoryLineResponse"
  override val response: LineResponse.Service = InMemoryLineResponseService
}
