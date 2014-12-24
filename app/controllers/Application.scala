package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current
import models._
import utils._

import models.current.dao._
import models.current.dao.driver.simple._

object Application extends Controller {

  def index = DBAction { implicit request =>
    Ok(views.html.index(forms.list))
  }

  def fetchSchemaByName(name: String) = DBAction { implicit request =>
    val schema = forms.filter(_.name.toLowerCase === name.toLowerCase).map(_.schema)
    Ok(schema.list.head.get)
  }

  val formForm = Form(
    mapping(
      "id" -> optional(number),
      "name" -> text,
      "description" -> optional(text),
      "schema" -> optional(json)
    )(FormData.apply)(FormData.unapply)
  )

  def insert = DBAction { implicit request =>
    val form = formForm.bindFromRequest.get
    forms.insert(form)
    Redirect(routes.Application.index())
  }

  def createForm = DBAction(parse.json) { implicit request =>
    val json = request.body
    val name = (json \ "schema" \ "name").as[String]
    val desc = (json \ "schema" \ "description").asOpt[String]
    val form = FormData(None, name, desc, Some(json))
    forms.insert(form)
    Ok("Done")
  }

}