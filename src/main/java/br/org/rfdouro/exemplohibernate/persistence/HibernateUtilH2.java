package br.org.rfdouro.exemplohibernate.persistence;

import br.org.rfdouro.exemplohibernate.model.Endereco;
import br.org.rfdouro.exemplohibernate.model.Pessoa;
import br.org.rfdouro.exemplohibernate.model.Tarefa;
import jakarta.persistence.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 *
 * @author rfdouro
 *
 * classe de utilidades para gerar Session Hibernate que são gerenciadores de entidades
 * (EntityManager)
 */
public class HibernateUtilH2 {

 //fábrica de sessões do hibernate
 private static SessionFactory sessionFactory;
 //sessão do hibernate usada na manipulação de dados
 //usa com ThreadLocal para poder habilitar concorrência
 //essa estratégia é comum em arquiteturas com ampla concorrência a recursos
 private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
 //url do banco de dados
 //private static String urlDB = "jdbc:h2:file:./testdb;AUTO_SERVER=TRUE";
 private static String urlDB = "jdbc:h2:file:./testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE";
 

 //execução estática - é executada no momento em que o programa é carregado
 static {

  sessionFactory = null;

  //propriedades de defininção para o JPA
  Map<String, Object> settings = new HashMap<>();
  //drive do banco
  settings.put("hibernate.connection.driver_class", "org.h2.Driver");
  //dialeto do banco
  settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
  //url do banco
  settings.put("hibernate.connection.url", urlDB);
  //usuário do banco
  settings.put("hibernate.connection.username", "root");
  //senha do usuário
  settings.put("hibernate.connection.password", "secret");
  //contexto da sessão
  settings.put("hibernate.current_session_context_class", "thread");
  //mostra os comandos SQL ?
  settings.put("hibernate.show_sql", "false");
  //formata os comandos SQL exibidos ?
  settings.put("hibernate.format_sql", "true");
  /*
  hbm2ddl é a chave que indica como serão executadas as instruções DDL para
  definição do modelo de dados da base em questão
  os possíveis valores são os que seguem abaixo:
  
   validate: valida o esquema sem fazer mudanças no banco.
   update: atualiza o esquema do banco.
   create: destrói dados anteriores e cria o esquema do banco.
   create-drop: exclui o esquema ao término da sessão.
  
  esses valores podem desde criar todas as tabelas em relação às classes modelo ou apenas verificar a existência das mesmas
  é imprescindível que as classes definidas na aplicação estejam em concordância com o estabelecido no banco de dados
   */
  settings.put("hibernate.hbm2ddl.auto", "update");

  Properties properties = new Properties();
  properties.putAll(settings);

  try {
   //adiciona as classes do modelo para verificação do Hibernate / JPA
   //devem estar indicadas aqui todas as classes do sistema que são entidades
   Configuration configuration = new Configuration();
   Set<Class> classes = getEntityClasses("br.org.rfdouro.exemplohibernate");
   classes.forEach(c -> {
    configuration.addAnnotatedClass(c);
   });
   //indica as propriedades utilizadas
   configuration.setProperties(properties);
   //cria a fábrica de sessões
   sessionFactory = configuration.buildSessionFactory();

  } catch (Throwable ex) {
   throw new ExceptionInInitializerError(ex);
  }
 }

 /**
  * método que retorna uma Session de acesso ao banco de dados
  */
 public static Session getInstance() {
  Session session = (Session) threadLocal.get();
  //cria uma sessão numa thread e a abre para ser utilizada
  session = sessionFactory.openSession();
  threadLocal.set(session);
  return session;
 }

 /**
  * método para retornar as classes da aplicação que são marcadas como Entity
  */
 public static Set<Class> getEntityClasses(String packageName) {
  Reflections reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(c -> true));
  return reflections.getSubTypesOf(Object.class)
          .stream()
          .filter(c -> {
           return c.isAnnotationPresent(Entity.class);
          })
          .collect(Collectors.toSet());
 }
}
