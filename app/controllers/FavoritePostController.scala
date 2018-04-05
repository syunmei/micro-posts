package controllers

import java.time.ZonedDateTime
import javax.inject._

import jp.t2v.lab.play2.auth.AuthenticationElement
import jp.t2v.lab.play2.pager.Pager
import models.FavoritePost
import play.api.Logger
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import services.{ FavoritePostService, UserService }

@Singleton
class FavoritePostController @Inject()(val favoritePostService: FavoritePostService,
                                       val userService: UserService,
                                       val messagesApi: MessagesApi)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with AuthenticationElement {

  def favorite(postId: Long): Action[AnyContent] = StackAction { implicit request =>
    val currentUser  = loggedIn
    val now          = ZonedDateTime.now()
    val favoritePost = FavoritePost(None, currentUser.id.get, postId, now, now)
    favoritePostService
      .create(favoritePost)
      .map { _ =>
        Redirect(routes.HomeController.index(Pager.default))
      }
      .recover {
        case e: Exception =>
          Logger.error("occurred error", e)
          Redirect(routes.HomeController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

  def unFavorite(postId: Long): Action[AnyContent] = StackAction { implicit request =>
    val currentUser = loggedIn
    favoritePostService
      .deleteBy(currentUser.id.get, postId)
      .map { _ =>
        Redirect(routes.HomeController.index(Pager.default))
      }
      .recover {
        case e: Exception =>
          Logger.error("occurred error", e)
          Redirect(routes.HomeController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

}
