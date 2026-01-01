package io.github.raphaelmun1z.ecommerce.dtos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para cadastro ou atualização de dados do cliente")
public class ClienteRequestDTO {

    @Schema(description = "Nome completo do cliente", example = "Maria Silva")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Endereço de e-mail válido para contato e login", example = "maria@email.com")
    @NotBlank
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Senha de acesso do cliente", example = "SenhaSegura123!")
    @NotBlank
    private String senha;

    @Schema(description = "Número do CPF (Cadastro de Pessoas Físicas)", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Telefone de contato", example = "(11) 99999-9999")
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}