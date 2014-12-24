package models

import play.api.db.slick.DB
import utils.MyPostgresDriver

/**
 * Created by markmo on 29/11/2014.
 */
class DAO(override val driver: MyPostgresDriver) extends FormsComponent {
  import driver.simple._

  val forms = TableQuery(new Forms(_))

}

object current {
  val dao = new DAO(DB(play.api.Play.current).driver.asInstanceOf[MyPostgresDriver])
}