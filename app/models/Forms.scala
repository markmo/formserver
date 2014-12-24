package models

import play.api.libs.json.JsValue
import utils.WithMyDriver

/**
 * Created by markmo on 29/11/2014.
 */

case class FormData(id: Option[Int], name: String, description: Option[String], schema: Option[JsValue])

trait FormsComponent extends WithMyDriver {
  import driver.simple._

  class Forms(tag: Tag) extends Table[FormData](tag, "Form") {
    def id = column[Option[Int]]("form_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("form_name")
    def description = column[Option[String]]("description")
    def schema = column[Option[JsValue]]("schema")

    def * = (id, name, description, schema) <> (FormData.tupled, FormData.unapply)
  }

}
