package com.gvendas.gestao_vendas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gvendas.gestao_vendas.entidades.Produto;
import com.gvendas.gestao_vendas.exception.RegraNegocioException;
import com.gvendas.gestao_vendas.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaService categoriaService;

    public List<Produto> listarTodos(Long codigoCategoria) {
        return produtoRepository.findByCategoriaCodigo(codigoCategoria);
    }

    public Produto buscarPorCodigo(Long codigo, Long codigoCategoria) {
        return produtoRepository.BuscarPorCodigo(codigo, codigoCategoria);
    }

    public Produto salvar(Produto produto) {
        validarCategoriaExiste(produto.getCategoria().getCodigo());
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

    private void validarCategoriaExiste(Long codigoCategoria) {
        if (codigoCategoria == null) {
            throw new RegraNegocioException("A categoria não pode ser nula");
        }
        if (categoriaService.buscarPorCodigo(codigoCategoria).isEmpty()) {
            throw new RegraNegocioException(
                    String.format("A categoria de código %s não existe no cadastro", codigoCategoria));
        }
    }

    private Produto validarProdutoExiste(Long codigo, Long codigoCategoria) {
        Produto produto = produtoRepository.BuscarPorCodigo(codigo, codigoCategoria);
        if (produto == null) {
            throw new RegraNegocioException("Produto não encontrado");
        }
        return produto;
    }

    private void validarProdutoDuplicado(Produto produto) {
        Optional<Produto> produtoEncontrado = produtoRepository.findByCategoriaCodigoAndDescricao(
                produto.getCategoria().getCodigo(),
                produto.getDescricao());

        if (produtoEncontrado.isPresent()) {
           
        
            if (produto.getCodigo() == null ||
                    !produtoEncontrado.get().getCodigo().equals(produto.getCodigo())) {
                throw new RegraNegocioException(
                        String.format("O produto %s já está cadastrado", produto.getDescricao()).toUpperCase());
            }
        }
    }
}