package io.github.raphaelmun1z.ecommerce.dtos.res.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com dados do endereço")
public class EnderecoResponseDTO {
    public String id;
    public String apelido;
    public String cep;
    public String logradouro;
    public String numero;
    public String complemento;
    public String bairro;
    public String cidade;
    public String uf;
    public boolean principal;

    public EnderecoResponseDTO(Endereco entity) {
        this.id = entity.getId();
        this.apelido = entity.getApelido();
        this.cep = entity.getCep();
        this.logradouro = entity.getLogradouro();
        this.numero = entity.getNumero();
        this.complemento = entity.getComplemento();
        this.bairro = entity.getBairro();
        this.cidade = entity.getCidade();
        this.uf = entity.getUf();
        this.principal = entity.isPrincipal();
    }
}