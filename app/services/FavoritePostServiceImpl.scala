package services

import javax.inject.Singleton

import models.{ FavoritePost }
import scalikejdbc._

import scala.util.Try

@Singleton
class FavoritePostServiceImpl extends FavoritePostService {

  override def create(favoritePost: FavoritePost)(implicit dbSession: DBSession): Try[Long] = Try {
    FavoritePost.create(favoritePost)
  }

  override def findById(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[List[FavoritePost]] = Try {
    FavoritePost.where('userId -> userId).apply()
  }

  override def findByPostId(postId: Long)(implicit dbSession: DBSession = AutoSession): Try[Option[FavoritePost]] =
    Try {
      FavoritePost.where('postId -> postId).apply().headOption
    }

  override def countByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long] = Try {
    FavoritePost.countBy(sqls.eq(FavoritePost.defaultAlias.userId, userId))
  }

  override def deleteBy(userId: Long, postId: Long)(implicit dbSession: DBSession = AutoSession): Try[Int] = Try {
    val c     = FavoritePost.column
    val count = FavoritePost.countBy(sqls.eq(c.userId, userId).and.eq(c.postId, postId))
    if (count == 1) {
      FavoritePost.deleteBy(
        sqls
          .eq(FavoritePost.column.userId, userId)
          .and(sqls.eq(FavoritePost.column.postId, postId))
      )
    } else 0
  }

}
