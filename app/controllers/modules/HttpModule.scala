package controllers.modules

import com.google.inject.{AbstractModule, TypeLiteral}
import infra.modules.AppContext.HttpRuntime

class HttpModule extends AbstractModule {
  override def configure(): Unit =
    bind(new TypeLiteral[HttpRuntime]() {})
      .toProvider(classOf[HttpRuntimeProvider])
}
