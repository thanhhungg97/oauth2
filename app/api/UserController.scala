package api

import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class UserController @Inject() (controllerComponents: ControllerComponents)
    extends AbstractController(controllerComponents) {


}
