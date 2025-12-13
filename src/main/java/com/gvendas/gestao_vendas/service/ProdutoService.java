package com.gvendas.gestao_vendas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gvendas.gestao_vendas.entidades.Produto;
import com.gvendas.gestao_vendas.exception.RegraNegocioException;
import com.gvendas.gestao_vendas.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos(Long codigoCategoria) {
        return produtoRepository.findByCategoriaCodigo(codigoCategoria);
    }

    public Produto buscarPorCodigo(Long codigo, Long codigoCategoria) {
        return produtoRepository.BuscarPorCodigo(codigo, codigoCategoria);
    }

    public Produto salvar(Produto produto) {
        validarProdutoDuplicado(produto);
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long codigo, Produto produto) {
        Produto produtoSalvar = validarProdutoExiste(codigo, produto.getCategoria().getCodigo());
        validarProdutoDuplicado(produto);
        BeanUtils.copyProperties(produto, produtoSalvar, "codigo");
        return produtoRepository.save(produtoSalvar);
    }

    public void deletar(Long codigo) {
        produtoRepository.deleteById(codigo);
    }

    private Produto validarProdutoExiste(Long codigo, Long codigoCategoria) {
        Produto produto = produtoRepository.BuscarPorCodigo(codigo, codigoCategoria);
        if (produto == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return produto;
    }

    private void validarProdutoDuplicado(Produto produto) {
        Produto produtoEncontrado = produtoRepository.BuscarPorCodigo(produto.getCodigo(), produto.getCategoria().getCodigo());
        if (produtoEncontrado != null && produtoEncontrado.getCodigo() != produto.getCodigo()) {
            throw new RegraNegocioException(
                    String.format("O produto %s já está cadastrada", produto.getDescricao()).toUpperCase());
        }
    }
}
