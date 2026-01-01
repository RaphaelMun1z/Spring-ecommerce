package io.github.raphaelmun1z.ecommerce.services.analitico;

import io.github.raphaelmun1z.ecommerce.entities.Notificacao;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEnvio;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoCanal;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.repositories.analitico.NotificacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoService.class);
    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Notificacao> listarPorCliente(String clienteId, Pageable pageable) {
        return repository.findByClienteId(clienteId, pageable);
    }

    @Async
    @Transactional
    public void enviarBoasVindas(Cliente cliente) {
        String titulo = "Bem-vindo à ConstrutoraPro!";
        String corpo = "Olá " + cliente.getNome() + ", seu cadastro foi realizado com sucesso. Aproveite nossas ofertas!";

        enviarEmailSimulado(cliente, titulo, corpo);
    }

    @Async
    @Transactional
    public void notificarAtualizacaoPedido(Pedido pedido) {
        if (pedido.getCliente() == null) return;

        String titulo = "Atualização do Pedido #" + pedido.getId();
        String corpo = "Olá " + pedido.getCliente().getNome() + ", seu pedido mudou para: " + pedido.getStatus();

        enviarEmailSimulado(pedido.getCliente(), titulo, corpo);
    }

    private void enviarEmailSimulado(Cliente cliente, String titulo, String mensagem) {
        try {
            // Aqui entraria a lógica real: JavaMailSender.send(...)
            logger.info(" Enviando E-mail para: {} | Assunto: {}", cliente.getEmail(), titulo);

            // Persiste o log de sucesso
            Notificacao notificacao = new Notificacao(
                cliente,
                cliente.getEmail(),
                titulo,
                mensagem,
                TipoCanal.EMAIL,
                StatusEnvio.ENVIADO
            );
            repository.save(notificacao);

        } catch (Exception e) {
            logger.error("Erro ao enviar notificação: {}", e.getMessage());

            // Persiste o log de falha
            Notificacao falha = new Notificacao(
                cliente,
                cliente.getEmail(),
                titulo,
                mensagem,
                TipoCanal.EMAIL,
                StatusEnvio.FALHA
            );
            repository.save(falha);
        }
    }
}
