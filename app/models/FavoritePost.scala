package models

import java.time.ZonedDateTime

import jp.t2v.lab.play2.pager.{ OrderType, Sortable }
import scalikejdbc._
import skinny.orm._

case class FavoritePost(id: Option[Long],
                        userId: Long,
                        postId: Long,
                        createAt: ZonedDateTime = ZonedDateTime.now(),
                        updateAt: ZonedDateTime = ZonedDateTime.now(),
                        user: Option[User] = None,
                        favoritePost: Option[FavoritePost] = None)

object FavoritePost extends SkinnyCRUDMapper[FavoritePost] {

  override def defaultAlias: Alias[FavoritePost] = createAlias("fp")

  override def tableName = "favorite_post"

  override def extract(rs: WrappedResultSet, n: ResultName[FavoritePost]): FavoritePost =
    autoConstruct(rs, n, "user", "favoritePost")

  private def toNamedValues(record: FavoritePost): Seq[(Symbol, Any)] = Seq(
    'userId   -> record.userId,
    'postId   -> record.postId,
    'createAt -> record.createAt,
    'updateAt -> record.updateAt
  )

  implicit object sortable extends Sortable[MicroPost] {
    override val default: (String, OrderType) = ("id", OrderType.Descending)
    override val defaultPageSize: Int         = 10
    override val acceptableKeys: Set[String]  = Set("id")
  }

  def create(favoritePost: FavoritePost)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(favoritePost): _*)
}
