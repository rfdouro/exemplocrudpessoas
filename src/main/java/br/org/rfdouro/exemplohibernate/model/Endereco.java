package br.org.rfdouro.exemplohibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *
 * Classe que representa um endereço - mapeia a tabela endereco
 */
@Entity
@Table(name = "endereco")
public class Endereco {

 //PK da tabela
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String rua;
 //representa o mapeamento com a tabela pessoa
 //idpessoa é FK na tabela endereco
 @OneToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "idpessoa")
 private Pessoa morador;

 //demais métodos
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getRua() {
  return rua;
 }

 public void setRua(String rua) {
  this.rua = rua;
 }

 public Pessoa getMorador() {
  return morador;
 }

 public void setMorador(Pessoa morador) {
  this.morador = morador;
 }

}
