package ing.vyasya.ispecs

import ing.vyasya.domain.LineResponse
import ing.vyasya.http.ApiCall
import ing.vyasya.specs.LineResponseSpec

// Integration specs for live line response service
class LineResponseISpec extends LineResponseSpec {

  behavior of "LineResponse"
  override val response: LineResponse.Service = ApiCall
}
