package br.org.rfdouro.exemplohibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;

/**
 *
 * Classe que representa uma tarefa - mapeia a tabela tarefa
 */
@Entity
@Table(name = "tarefa")
public class Tarefa {

 //PK da tabela
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String descricao;
 //campo que armazena uma data no banco de dados
 @Temporal(TemporalType.DATE)
 private LocalDate data;
 //representa o mapeamento com a tabela pessoa
 //idpessoa é FK na tabela endereco
 @ManyToOne(optional = true)
 @JoinColumn(name = "idpessoa")
 private Pessoa responsavel;

 //demais métodos
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getDescricao() {
  return descricao;
 }

 public void setDescricao(String descricao) {
  this.descricao = descricao;
 }

 public LocalDate getData() {
  return data;
 }

 public void setData(LocalDate data) {
  this.data = data;
 }

 public Pessoa getResponsavel() {
  return responsavel;
 }

 public void setResponsavel(Pessoa responsavel) {
  this.responsavel = responsavel;
 }

}
