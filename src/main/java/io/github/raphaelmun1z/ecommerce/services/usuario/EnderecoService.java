package io.github.raphaelmun1z.ecommerce.services.usuario;

import io.github.raphaelmun1z.ecommerce.dtos.req.usuario.EnderecoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.usuario.EnderecoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Endereco;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, ClienteRepository clienteRepository) {
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> listarPorCliente(String clienteId) {
        return enderecoRepository.findByClienteId(clienteId).stream()
            .map(EnderecoResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public EnderecoResponseDTO adicionar(String clienteId, EnderecoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        if (dto.principal()) {
            enderecoRepository.resetarPrincipal(clienteId);
        }

        Endereco endereco = new Endereco();
        copiarDtoParaEntidade(dto, endereco);
        endereco.setCliente(cliente);

        // Se for o primeiro endereço, força ser principal
        if (enderecoRepository.findByClienteId(clienteId).isEmpty()) {
            endereco.setPrincipal(true);
        }

        return new EnderecoResponseDTO(enderecoRepository.save(endereco));
    }

    @Transactional
    public EnderecoResponseDTO atualizar(String id, EnderecoRequestDTO dto) {
        Endereco endereco = enderecoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        if (dto.principal() && !endereco.isPrincipal()) {
            enderecoRepository.resetarPrincipal(endereco.getCliente().getId());
        }

        copiarDtoParaEntidade(dto, endereco);
        return new EnderecoResponseDTO(enderecoRepository.save(endereco));
    }

    @Transactional
    public void deletar(String id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado");
        }
        enderecoRepository.deleteById(id);
    }

    @Transactional
    public void definirComoPrincipal(String id) {
        Endereco endereco = enderecoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        enderecoRepository.resetarPrincipal(endereco.getCliente().getId());
        endereco.setPrincipal(true);
        enderecoRepository.save(endereco);
    }

    private void copiarDtoParaEntidade(EnderecoRequestDTO dto, Endereco entity) {
        entity.setApelido(dto.apelido());
        entity.setCep(dto.cep());
        entity.setLogradouro(dto.logradouro());
        entity.setNumero(dto.numero());
        entity.setComplemento(dto.complemento());
        entity.setBairro(dto.bairro());
        entity.setCidade(dto.cidade());
        entity.setUf(dto.uf());
        entity.setPrincipal(dto.principal());
    }
}