package br.org.rfdouro.exemplohibernate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * Classe que representa uma pessoa - mapeia a tabela pessoa
 */
@Entity
@Table(name = "pessoa")
//consultas nomeadas
@NamedQueries({
 @NamedQuery(name = "nq_inicio_nome", query = "select P from Pessoa P where P.nome like :nome ")
})
public class Pessoa {

 //PK da tabela
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 //indica o nome da coluna na tabela, poderia ser outro inclusive
 @Column(name = "nome")
 private String nome;
 //representa o mapeamento com a tabela enredeco
 //morador é o atributo na classe Enredeco que representa a ligação com Pessoa
 @OneToOne(mappedBy = "morador", fetch = FetchType.LAZY, optional = true)
 private Endereco endereco;
 //representa o mapeamento com a tabela tarefa
 //responsavel é o atributo na classe Tarefa que representa a ligação com Pessoa
 @OneToMany(targetEntity = Tarefa.class, mappedBy = "responsavel")
 //lista os dados ordenados por data
 @OrderBy("data ASC")
 private List<Tarefa> tarefas;

 public Endereco getEndereco() {
  return endereco;
 }

 public void setEndereco(Endereco endereco) {
  this.endereco = endereco;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getNome() {
  return nome;
 }

 public void setNome(String nome) {
  this.nome = nome;
 }

 public List<Tarefa> getTarefas() {
  return tarefas;
 }

 public void setTarefas(List<Tarefa> tarefas) {
  this.tarefas = tarefas;
 }

}
