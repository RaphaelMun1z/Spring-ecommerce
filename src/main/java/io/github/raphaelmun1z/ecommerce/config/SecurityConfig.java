package io.github.raphaelmun1z.ecommerce.config;

import io.github.raphaelmun1z.ecommerce.security.jwt.CustomAccessDeniedHandler;
import io.github.raphaelmun1z.ecommerce.security.jwt.CustomAuthenticationEntryPoint;
import io.github.raphaelmun1z.ecommerce.security.jwt.JwtTokenFilter;
import io.github.raphaelmun1z.ecommerce.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Value("${security.password.encoder.secret}")
    private String encoderSecret;

    @Value("${security.password.encoder.iterations}")
    private Integer encoderIterations;

    @Value("${security.password.encoder.salt-length}")
    private Integer saltLength;

    public SecurityConfig(JwtTokenProvider tokenProvider, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenProvider = tokenProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(encoderSecret, saltLength, encoderIterations, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        return passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint entryPoint, CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(tokenProvider, handlerExceptionResolver);
        // @formatter:off
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(org.springframework.security.config.Customizer.withDefaults())
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()

                    // Vitrine Pública (Apenas Leitura)
                    .requestMatchers(HttpMethod.GET, "/produtos/vitrine", "/produtos/buscar", "/produtos/{id}").permitAll()
                    .requestMatchers(HttpMethod.GET, "/categorias/ativas", "/categorias/{id}").permitAll()

                    // Rotas de Admin (Gestão e Relatórios)
                    .requestMatchers("/dashboard/**", "/relatorios/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/produtos", "/categorias").hasRole("ADMIN") // Listar tudo (incluindo inativos)
                    .requestMatchers(HttpMethod.POST, "/produtos/**", "/categorias/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/produtos/**", "/categorias/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/produtos/**", "/categorias/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/produtos/**", "/categorias/**").hasRole("ADMIN")

                    // Admin - Operações Especiais
                    .requestMatchers(HttpMethod.GET, "/pedidos").hasRole("ADMIN") // Ver todos os pedidos da loja
                    .requestMatchers(HttpMethod.PATCH, "/pedidos/{id}/status").hasRole("ADMIN") // Alterar status do pedido
                    .requestMatchers(HttpMethod.PATCH, "/entregas/**").hasRole("ADMIN") // Atualizar rastreio

                    // Rotas Autenticadas (Cliente ou Admin)
                    // Inclui: Carrinho, Checkout, Meus Pedidos, Pagamentos, Endereços, Notificações
                    .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            .build();
        // @formatter:on
    }
}
