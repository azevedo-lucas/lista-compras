package com.lista.compras.service;

import com.lista.compras.dto.ProdutoResponseDTO;
import com.lista.compras.model.Produto;
import com.lista.compras.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    public BigDecimal calcularTotal() {
        return listaProdutos()
                .stream()
                .map(ProdutoResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ProdutoResponseDTO produtoAtualizado(String nomeAtual, Produto produto) {
        List<Produto> produtosEncontrados = produtoRepository.findByNomeIgnoreCase(nomeAtual);
        if (produtosEncontrados.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        Produto produtoExistente = produtosEncontrados.get(0);
        List<Produto> produtosComNovoNome = produtoRepository.findByNomeIgnoreCase(produto.getNome());

        boolean nomeJaExiste = produtosComNovoNome.stream().
                anyMatch(p -> !p.getId().equals(produtoExistente.getId()));
        if (nomeJaExiste) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um produto com esse nome");
        }

        produtoExistente.setNome(produto.getNome());
        produtoExistente.setQuantidade(produto.getQuantidade());
        produtoExistente.setPrecoUnitario(produto.getPrecoUnitario());

        Produto produtoSalvo = produtoRepository.save(produtoExistente);
        return converterParaDTO(produtoSalvo);

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

    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

}
