package com.lista.compras.controller;

import com.lista.compras.dto.ProdutoResponseDTO;
import com.lista.compras.model.Produto;
import com.lista.compras.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {


    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> findAll() {
        var listProdutos = produtoService.listaProdutos();
        return ResponseEntity.ok().body(listProdutos);
    }
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> save(@RequestBody Produto produto) {
        ProdutoResponseDTO produtoSalvo = produtoService.cadastraProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }
}
