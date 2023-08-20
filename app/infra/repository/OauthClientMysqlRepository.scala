package infra.repository

import domain.domain.{OauthClient, OauthId, OauthSecret}
import domain.error.DatabaseAccessError
import domain.repository.OauthClientRepository
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import zio.{IO, ZIO}

import java.util.UUID

case class OauthClientMysqlRepository() extends OauthClientRepository {
  override def save(oauthClient: OauthClient): IO[DatabaseAccessError, Int] =
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
      .mapError(e => DatabaseAccessError(e.toString))

  override def get(id: OauthId): IO[DatabaseAccessError, Option[OauthClient]] =
    ZIO
      .fromOption(DB readOnly { implicit session =>
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
      })
      .mapBoth(e => DatabaseAccessError(e.toString), Some(_))

  override def update(oauthClient: OauthClient): IO[DatabaseAccessError, Unit] =
    ???
}
