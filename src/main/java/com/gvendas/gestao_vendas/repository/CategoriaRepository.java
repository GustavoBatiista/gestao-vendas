package com.gvendas.gestao_vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gvendas.gestao_vendas.entidades.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
