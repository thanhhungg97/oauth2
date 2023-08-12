package infra.module

import com.google.inject.{AbstractModule, TypeLiteral}
import infra.module.AppContext.HttpRuntime

class HttpModule extends AbstractModule {
  override def configure(): Unit = {
    bind(new TypeLiteral[HttpRuntime]() {}).toProvider(classOf[HttpRuntimeProvider])
  }
}
