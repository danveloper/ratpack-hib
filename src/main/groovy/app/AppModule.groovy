package app

import app.model.Person
import app.services.PersonDAO
import com.google.inject.AbstractModule
import com.google.inject.Provides
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration

class AppModule extends AbstractModule {

  private SessionFactory sessionFactory

  @Override
  protected void configure() {
    def configuration = new Configuration()
        .setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ManagedSessionContext")
        .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
        .setProperty("hibernate.connection.url", "jdbc:h2:mem:test")
        .setProperty("hibernate.connection.username", "sa")
        .setProperty("hibernate.connection.password", "")
        .setProperty("hibernate.hbm2ddl.auto", "create-drop")
        .addAnnotatedClass(Person)
    def registryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.properties).build()
    sessionFactory = configuration.buildSessionFactory(registryBuilder)

    bind PersonDAO
  }

  @Provides
  Session sessionProvider() {
    def session = sessionFactory.openSession()
    if (!session.transaction.active) {
      session.beginTransaction()
    }
    session
  }
}
