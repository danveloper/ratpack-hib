package app.services

import app.model.Person
import javax.inject.Inject
import org.hibernate.Session
import ratpack.exec.ExecControl
import rx.Observable


import static ratpack.rx.RxRatpack.observe

class PersonDAO {

  private final ExecControl execControl
  private final Session session

  @Inject
  PersonDAO(ExecControl execControl, Session session) {
    this.execControl = execControl
    this.session = session
  }

  Observable<Long> save(Person person) {
    block {
      session.save(person)
    }
  }

  Observable<Person> all() {
    block {
      session.createCriteria(Person).list()
    }
  }

  Observable<Person> get(Long id) {
    block {
      session.get(Person, id)
    }
  }

  private Observable<Person> block(Closure clos) {
    observe(
        execControl.blocking(clos))
        .doOnCompleted({
          session.transaction.commit()
        }).doOnError({
          session.transaction.rollback()
        })
  }
}
