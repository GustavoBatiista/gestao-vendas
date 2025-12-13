package com.gvendas.gestao_vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.gvendas.gestao_vendas.entidades.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaCodigo(Long codigoCategoria);

    @Query("SELECT prod FROM Produto prod WHERE prod.codigo = :codigo AND prod.categoria.codigo = :codigoCategoria")
    Produto BuscarPorCodigo(@Param("codigo") Long codigo, @Param("codigoCategoria") Long codigoCategoria);
}
