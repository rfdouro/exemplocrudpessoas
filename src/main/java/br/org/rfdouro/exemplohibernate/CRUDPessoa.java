package br.org.rfdouro.exemplohibernate;

import br.org.rfdouro.exemplohibernate.model.Pessoa;
import br.org.rfdouro.exemplohibernate.model.Tarefa;
import br.org.rfdouro.exemplohibernate.persistence.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;

/**
 *
 * classe que executa o programa 
 */
public class CRUDPessoa {

 public static void main(String[] args) {
  Session s = HibernateUtil.getInstance();
  Scanner sc = new Scanner(System.in);
  int op = 0;
  //menu de execução
  do {
   System.out.println("o que quer fazer?"
           + "\n1 - inserir pessoa"
           + "\n2 - listar pessoas"
           + "\n3 - alterar pessoa"
           + "\n4 - excluir pessoa"
           + "\n5 - pesquisar pessoas por nome"
           + "\n6 - listar pessoas com join"
           + "\n7 - consulta nativa com join"
           + "\n0 - sair");
   op = sc.nextInt();
   switch (op) {
    case 1:
     EntityManager em = HibernateUtil.getInstance(); // recupera um gerenciador de entidades
     try {
      System.out.println("digite o nome da pessoa");
      String nome = sc.next();
      em.getTransaction().begin(); // inicializa a transação
      Pessoa p = new Pessoa(); // cria uma entidade
      p.setNome(nome);
      em.persist(p); // insere no banco
      em.getTransaction().commit(); // comita a transação
     } catch (Exception ex) {
      em.getTransaction().rollback(); // invalida a transação
     }
     em.close(); // fecha o gerenciador
     break;
    case 2:
     //lista pessoas usando query JPQL
     //deve-se indicar o tipo a ser retornaro = Pessoa.class
     List<Pessoa> listaPessoas = s.createQuery("select P from Pessoa P order by P.nome", Pessoa.class).getResultList();
     //percorre a lista de pesoas
     for (Pessoa pl : listaPessoas) {
      System.out.println(pl.getId() + " = " + pl.getNome());
      //se tiver endereço, exibe a informação
      if (pl.getEndereco() != null) {
       System.out.println("endereço = " + pl.getEndereco().getRua());
      }
      //obtém a lista de tarefas da pessoa
      List<Tarefa> listaTarefas = pl.getTarefas();
      //se tiver tarefas, exibe a informação
      if (listaTarefas != null && listaTarefas.size() > 0) {
       System.out.println("tarefas:");
       for (Tarefa tl : listaTarefas) {
        System.out.println("\t* " + tl.getData() + " - " + tl.getDescricao());
       }
      }
     }
     break;
    case 3:
     s.getTransaction().begin();//inicia a transação
     System.out.println("digite o id da pessoa");
     Long id = sc.nextLong();
     //seleciona uma pessoa pelo id
     //deve ser indicada a classe para pesquisa - Pessoa.class
     Pessoa pa = s.find(Pessoa.class, id);
     System.out.println("digite um novo nome");
     String nomea = sc.next();
     pa.setNome(nomea);
     //salva novamente no banco de dados
     //como o objeto possui id válido
     //executa um update e não um insert
     s.persist(pa);
     s.getTransaction().commit();//confirma a transação
     break;
    case 4:
     s.getTransaction().begin();//inicia a transação
     System.out.println("digite o id da pessoa");
     Long ide = sc.nextLong();
     //seleciona uma pessoa pelo id
     //indicada a classe para pesquisa - Pessoa.class
     Pessoa pe = s.find(Pessoa.class, ide);
     s.remove(pe);//exclui no banco
     s.getTransaction().commit();//confirma a transação
     break;
    case 5:
     System.out.println("digite a pesquisa");
     String inicio = sc.next();
     //cria uma Query com parâmetro
     //observe que o parâmetro é iniciado com dois pontos ':'
     Query q = s.createQuery("select P from Pessoa P where P.nome like :nome order by P.nome", Pessoa.class);
     //indica nominalmente o valor do parâmetro antes da execução da instrução SQL
     q.setParameter("nome", inicio + "%");
     //executa a instrução SQL
     List<Pessoa> listaPessoasc = q.getResultList();
     //percorre a lista de pessoas
     for (Pessoa pl : listaPessoasc) {
      System.out.println(pl.getId() + " = " + pl.getNome());
      //se tiver endereço, exibe a informação
      if (pl.getEndereco() != null) {
       System.out.println("endereço = " + pl.getEndereco().getRua());
      }
      //obtém a lista de tarefas da pessoa
      List<Tarefa> listaTarefas = pl.getTarefas();
      //se tiver tarefas, exibe a informação
      if (listaTarefas != null && listaTarefas.size() > 0) {
       System.out.println("tarefas:");
       for (Tarefa tl : listaTarefas) {
        System.out.println("\t* " + tl.getData() + " - " + tl.getDescricao());
       }
      }
     }
     break;
    case 6:
     //executa uma consulta JPQL usando JOIN
     //a consulta aqui é feita através dos atributos das classes (observe T.responsavel=P) 
     //aqui a classe de retorno é um array de objetos (Object[])
     List<Object[]> listaobjetos = s.createQuery("select P.nome, T.data, T.descricao from Pessoa P join Tarefa T on T.responsavel=P order by P.nome", Object[].class).getResultList();
     for (Object[] ob : listaobjetos) {
      System.out.println(ob[0] + " - " + ob[1] + ", " + ob[2]);
     }
     break;
    case 7:
     //executa uma consulta NATIVA usando JOIN
     //a consulta aqui é feita através dos campos das tabelas (observe T.idpessoa=P.id) 
     //aqui a classe de retorno é um array de objetos (Object[])
     List<Object[]> listaobjetosN = s.createNativeQuery("select P.nome, T.data, T.descricao from Pessoa P inner join Tarefa T on T.idpessoa=P.id order by P.nome", Object[].class).getResultList();
     for (Object[] ob : listaobjetosN) {
      System.out.println(ob[0] + " - " + ob[1] + ", " + ob[2]);
     }
     break;
   }
  } while (op != 0);
 }
}
