package com.lista.compras.service;

import com.lista.compras.model.Produto;
import com.lista.compras.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastraProduto(Produto produto) {

        if (produto.getNome() == null || produto.getNome().isBlank()) {

            throw new IllegalArgumentException("O campo é obrigatório e deve ser preenchido.");
        }
        if (produto.getQuantidade() == null || produto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade é obrigatória" +
                    " e deve ser maior que zero.");
        }
        if (produto.getPrecoUnitario() == null ||
                produto.getPrecoUnitario().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException("O preço é obrigatório e deve ser maior que zero.");
        }

        return produtoRepository.save(produto);
    }

    public List<Produto> listaProdutos() {
        return produtoRepository.findAll();
    }


}
