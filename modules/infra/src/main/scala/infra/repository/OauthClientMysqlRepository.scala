package infra.repository

import domain.{OauthClient, OauthId, OauthSecret}
import error.OauthClientRepositoryError
import repository.OauthClientRepository
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import zio.blocking.Blocking._
import zio.{IO, ZIO}

import java.util.UUID

case class OauthClientMysqlRepository(blocking: Service) extends OauthClientRepository {
  override def save(oauthClient: OauthClient): IO[OauthClientRepositoryError, Int] =
    ZIO
      .effect(DB.autoCommit[Int] { implicit session =>
        sql"""insert into oauth_client(
              `id`, `client_secret`, `redirect_uri`,  `scopes`
              )
             values
              (${oauthClient.id.uuid.toString}, ${oauthClient.clientSecret.secret}, ${oauthClient.redirectUri}, ${oauthClient.scopes})  """
          .update()
          .apply()
      })
      .mapError(OauthClientRepositoryError.ServerError)

  override def get(id: OauthId): IO[OauthClientRepositoryError, Option[OauthClient]] =
    blocking.effectBlocking {
      DB.readOnly[Option[OauthClient]] { implicit session =>
        sql"select id, client_secret, redirect_uri, scopes from oauth_client where id = ${id.uuid.toString}"
          .map(rs =>
            OauthClient(
              OauthId(UUID.fromString(rs.string("id"))),
              OauthSecret(rs.string("client_secret")),
              rs.string("redirect_uri"),
              rs.get("scopes")
            )
          )
          .single()
          .apply()
      }
    }.mapError(OauthClientRepositoryError.ServerError)

  override def update(oauthClient: OauthClient): IO[OauthClientRepositoryError, Unit] =
    ???
}
