package io.github.raphaelmun1z.ecommerce.repositories.specs;

import io.github.raphaelmun1z.ecommerce.dtos.req.catalogo.ProdutoFiltroDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdutoSpecs {

    public static Specification<Produto> comFiltros(ProdutoFiltroDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro por Termo (Título ou Descrição)
            if (StringUtils.hasText(filtro.getTermo())) {
                String termoLike = "%" + filtro.getTermo().toLowerCase() + "%";
                Predicate tituloLike = cb.like(cb.lower(root.get("titulo")), termoLike);
                Predicate descricaoLike = cb.like(cb.lower(root.get("descricao")), termoLike);
                predicates.add(cb.or(tituloLike, descricaoLike));
            }

            // 2. Filtro por Categoria
            if (StringUtils.hasText(filtro.getCategoriaId())) {
                predicates.add(cb.equal(root.get("categoria").get("id"), filtro.getCategoriaId()));
            }

            // 3. Filtro por Status (Ativo)
            if (Boolean.TRUE.equals(filtro.getApenasAtivos())) {
                predicates.add(cb.isTrue(root.get("ativo")));
            }

            // 4. Filtro de Preço (Considerando Promoção vs Preço Cheio)
            if (filtro.getPrecoMin() != null || filtro.getPrecoMax() != null) {
                // Tipagem explícita para garantir que o CriteriaBuilder saiba que é BigDecimal
                Expression<BigDecimal> precoPromocional = root.get("precoPromocional");
                Expression<BigDecimal> preco = root.get("preco");

                // Expressão: COALESCE(preco_promocional, preco)
                Expression<BigDecimal> precoEfetivo = cb.coalesce(precoPromocional, preco);

                if (filtro.getPrecoMin() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(precoEfetivo, filtro.getPrecoMin()));
                }
                if (filtro.getPrecoMax() != null) {
                    predicates.add(cb.lessThanOrEqualTo(precoEfetivo, filtro.getPrecoMax()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}