package com.lista.compras.service;

import com.lista.compras.dto.ProdutoResponseDTO;
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

    public ProdutoResponseDTO cadastraProduto(Produto produto) {

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
        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaDTO(produtoSalvo);
    }

    public List<ProdutoResponseDTO> listaProdutos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }
    public ProdutoResponseDTO converterParaDTO(Produto produto) {
        BigDecimal subtotal = produto.calcularSubtotal();
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade(),
                produto.getPrecoUnitario(),
                subtotal
        );
    }

}
