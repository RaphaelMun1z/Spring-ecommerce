package io.github.raphaelmun1z.ecommerce.services.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.FavoritoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.Favorito;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.FavoritoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public FavoritoService(FavoritoRepository favoritoRepository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.favoritoRepository = favoritoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public Page<FavoritoResponseDTO> listarPorCliente(String clienteId, Pageable pageable) {
        return favoritoRepository.findByClienteId(clienteId, pageable)
            .map(FavoritoResponseDTO::new);
    }

    @Transactional
    public FavoritoResponseDTO adicionar(String clienteId, String produtoId) {
        // Verifica se já existe para evitar duplicidade
        if (favoritoRepository.existsByClienteIdAndProdutoId(clienteId, produtoId)) {
            Favorito existente = favoritoRepository.findByClienteIdAndProdutoId(clienteId, produtoId).get();
            return new FavoritoResponseDTO(existente);
        }

        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        Favorito novoFavorito = new Favorito(cliente, produto);
        return new FavoritoResponseDTO(favoritoRepository.save(novoFavorito));
    }

    @Transactional
    public void remover(String clienteId, String produtoId) {
        Favorito favorito = favoritoRepository.findByClienteIdAndProdutoId(clienteId, produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Favorito não encontrado ou não pertence ao cliente."));

        favoritoRepository.delete(favorito);
    }

    @Transactional
    public void limparTudo(String clienteId) {
        // Verifica se o cliente existe apenas por segurança
        if (!clienteRepository.existsById(clienteId)) {
            throw new EntityNotFoundException("Cliente não encontrado.");
        }
        favoritoRepository.deleteByClienteId(clienteId);
    }
}
