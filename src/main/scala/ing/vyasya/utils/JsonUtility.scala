package ing.vyasya.utils

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.{HttpEntity, HttpRequest}
import akka.http.scaladsl.unmarshalling.{FromRequestUnmarshaller, Unmarshaller}
import akka.stream.Materializer
import org.json4s.native.Serialization.{read, write}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

// Simple Json utility class for marshalling and unmarshalling domain objects
trait JsonUtility {

  implicit val formats: Formats = DefaultFormats

  implicit def marshaller[T <: AnyRef](implicit m: Manifest[T]): ToEntityMarshaller[T] =
    Marshaller.withFixedContentType(`application/json`) { a =>
      HttpEntity(`application/json`, marshal[T](a))
    }

  implicit def unmarshaller[T <: AnyRef](implicit m: Manifest[T]): FromRequestUnmarshaller[T] =
    Unmarshaller.withMaterializer { implicit ec: ExecutionContext => implicit mat: Materializer => value: HttpRequest =>
      val json: Future[String] =
        value.entity.toStrict(300.millis).map(_.data).map(_.utf8String)
      json.map(j => unmarshal[T](j))
    }

  def unmarshal[T <: AnyRef](json: String)(implicit m: Manifest[T]): T =
    read[T](json)

  def marshal[T <: AnyRef](obj: T)(implicit m: Manifest[T]): String =
    write[T](obj)
}
