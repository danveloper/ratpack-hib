import app.AppModule
import app.model.Person
import app.services.PersonDAO
import ratpack.jackson.JacksonModule
import ratpack.launch.LaunchConfig


import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {

  bindings {
    add new JacksonModule()
    add new AppModule()

    init { LaunchConfig launchConfig, PersonDAO dao ->
      launchConfig.execController.control.fork {
        dao.save(new Person(firstName: "Kyle", lastName: "Boon")).subscribe()
      }
      println "App started port => ${launchConfig.port}"
    }
  }

  handlers {
    handler("person") { PersonDAO dao ->
      dao.all().subscribe {
        render json(it)
      }
    }
    handler("person/:id") { PersonDAO dao ->
      dao.get(Long.valueOf(pathTokens.id)).subscribe {
        render json(it)
      }
    }
  }
}