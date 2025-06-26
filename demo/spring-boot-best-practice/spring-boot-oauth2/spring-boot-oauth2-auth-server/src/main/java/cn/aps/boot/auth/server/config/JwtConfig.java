package cn.aps.boot.auth.server.config;

import org.springframework.context.annotation.Configuration;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * @Description : 主要用于生成和管理用于 JWT 签名的 RSA 密钥对
 * @Author : lishirui
 * @Date ：2025/6/25 10:08
 */
@Configuration
public class JwtConfig {
    /**
     * 配置 JWKSource
     * JWKSource 提供了用于签名 JWT 的 JSON Web Key (JWK)
     * 在这里我们生成一个 RSA 密钥对，并将其包装为 JWKSet
     *
     * @return JWKSource 实例
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // 生成 RSA 密钥对
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 构建 RSAKey 对象，包含公钥、私钥和密钥ID (kid)
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString()) // 随机生成一个密钥ID
                .build();
        // 将 RSAKey 放入 JWKSet 中
        JWKSet jwkSet = new JWKSet(rsaKey);
        // 返回 ImmutableJWKSet，作为 JWKSource 的实现
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成 RSA 密钥对
     * 用于 JWT 的签名和验证
     *
     * @return RSA 密钥对
     */
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 密钥长度为 2048 位
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex); // 抛出运行时异常
        }
    }
}
