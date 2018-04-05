package services

import models.FavoritePost
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait FavoritePostService {

  def create(favoritePost: FavoritePost)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def findById(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[List[FavoritePost]]

  def findByPostId(postFavoriteId: Long)(
      implicit dbSession: DBSession = AutoSession
  ): Try[Option[FavoritePost]]

//  def findPostFavoritesByUserId(pager: Pager[User], userId: Long)(
//      implicit dbSession: DBSession = AutoSession
//  ): Try[SearchResult[User]]
//
//  def findPostFavoriteByUserId(pager: Pager[User], userId: Long)(
//      implicit dbSession: DBSession = AutoSession
//  ): Try[SearchResult[User]]

  def countByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long]

//  def countByFollowId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def deleteBy(userId: Long, postId: Long)(implicit dbSession: DBSession = AutoSession): Try[Int]

}
