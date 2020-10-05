package ing.vyasya.config

import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import com.typesafe.config.ConfigFactory
import org.asynchttpclient.DefaultAsyncHttpClientConfig

// Object responsible for resolving configs from the conf file throughout the application
object ConfigResolver {
  val appConfig = ConfigFactory.load()
  val hostUrl = appConfig.getString("endpoint")
  val chunkSize = appConfig.getInt("chunk")

  private val config = new DefaultAsyncHttpClientConfig.Builder()
    .setConnectionTtl(500)
    .setMaxConnections(300)
    .build()

  implicit val sttpBackend = AsyncHttpClientFutureBackend.usingConfig(config)

}
