package utils

import java.util.UUID

import com.github.tminglei.slickpg.PgRangeSupportUtils
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.io.{WKTReader, WKTWriter}
import org.joda.time.LocalDateTime
import play.api.data.FormError
import play.api.data.format.{Formats, Formatter}
import play.api.libs.json._
import scala.util.control.Exception.allCatch

/**
 * Created by markmo on 29/11/2014.
 */
object MyFormats {

  def jsonFormat: Formatter[JsValue] = new Formatter[JsValue] {

    override val format = Some(("format.json", Nil))

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], JsValue] =
      parsing(Json.parse, "error.json", Nil)(key, data)

    override def unbind(key: String, value: JsValue): Map[String, String] =
      Map(key -> Json.stringify(value))
  }

  def jodaDateTimeFormat: Formatter[LocalDateTime] = new Formatter[LocalDateTime] {

    override val format = Some(("format.datetime", Nil))

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], LocalDateTime] =
      parsing(LocalDateTime.parse, "error.datetime", Nil)(key, data)

    override def unbind(key: String, value: LocalDateTime) =
      Map(key -> value.toString)
  }

  def uuidFormat: Formatter[UUID] = new Formatter[UUID] {

    override val format = Some(("format.uuid", Nil))

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], UUID] =
      parsing(UUID.fromString, "error.uuid", Nil)(key, data)

    override def unbind(key: String, value: UUID): Map[String, String] =
      Map(key -> value.toString)
  }

  def rangeFormat[T](parseFn: (String => T)): Formatter[Range[T]] = new Formatter[Range[T]] {

    override val format = Some(("format.range", Nil))

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Range[T]] =
      parsing(PgRangeSupportUtils.mkRangeFn(parseFn), "error.range", Nil)(key, data)

    override def unbind(key: String, value: Range[T]): Map[String, String] =
      Map(key -> value.toString())
  }

  def strMapFormat: Formatter[Map[String, String]] = new Formatter[Map[String, String]] {

    override val format = Some(("format.jsonmap", Seq("{key1:value1, key2:value2, ...}")))

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Map[String, String]] =
      parsing(fromJsonStr(_).getOrElse(Map.empty[String, String]), "error.jsonmap", Nil)(key, data)

    override def unbind(key: String, value: Map[String, String]): Map[String, String] =
      Map(key -> toJsonStr(value))
  }

  implicit private val mapReads = Reads.mapReads[String]

  implicit private val mapWrites = Writes.mapWrites[String]

  def toJsonStr(v: Map[String, String]): String = Json.stringify(Json.toJson(v))

  def fromJsonStr(s: String): Option[Map[String, String]] = Option(Json.fromJson(Json.parse(s)).get)

  def geometryFormat[T <: Geometry]: Formatter[T] = new Formatter[T] {

    override val format = Some(("format.geometry", Nil))

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], T] = parsing(fromWKT[T], "error.geometry", Nil)(key, data)

    override def unbind(key: String, value: T): Map[String, String] = Map(key -> toWKT(value))
  }

  private val wktWriterHolder = new ThreadLocal[WKTWriter]

  private val wktReaderHolder = new ThreadLocal[WKTReader]

  private def toWKT(geom: Geometry): String = {
    if (wktWriterHolder.get == null) wktWriterHolder.set(new WKTWriter())
    wktWriterHolder.get.write(geom)
  }

  private def fromWKT[T](wkt: String): T = {
    if (wktReaderHolder.get == null) wktReaderHolder.set(new WKTReader())
    wktReaderHolder.get.read(wkt).asInstanceOf[T]
  }

  /**
   * Helper for formatter binders
   * (copy from [[play.api.data.format.Formats#parsing]])
   *
   * @param parse Function parsing a String value into a T value, throwing an exception in case of failure
   * @param errMsg Error to set in case of parsing failure
   * @param errArgs Arguments for error message
   * @param key Key name of the field to parse
   * @param data Field data
   * @tparam T
   * @return
   */
  private def parsing[T](parse: String => T, errMsg: String, errArgs: Seq[Any])
                        (key: String, data: Map[String, String]): Either[Seq[FormError], T] = {
    Formats.stringFormat.bind(key, data).right.flatMap { s =>
      allCatch[T]
        .either(parse(s))
        .left.map(e => Seq(FormError(key, errMsg ,errArgs)))
    }
  }

}
