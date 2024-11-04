package org.gentar.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gentar.exceptions.SystemOperationFailedException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PublicKeyProvider {

    private final RestTemplate restTemplate;
    private final Map<String, PublicKey> publicKeys = new ConcurrentHashMap<>();

    public PublicKeyProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("publicKeys")
    public PublicKey getPublicKey(String kid, String authenticationServiceUrl) {
        return publicKeys.computeIfAbsent(kid, k -> {
            String publicKeyText = requestPublicKeyText(authenticationServiceUrl, k);
            return buildPublicKeyFromText(publicKeyText);
        });
    }

    @CacheEvict(value = "publicKeys", allEntries = true)
    public void evictPublicKeys() {
        publicKeys.clear();
    }

    private String requestPublicKeyText(String authenticationServiceUrl, String kid) {
        String text = null;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(authenticationServiceUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            for (JsonNode keyNode : jsonNode.get("keys")) {
                if (keyNode.get("kid").asText().equals(kid)) {
                    text = keyNode.get("x5c").get(0).asText();
                    break;
                }
            }
            if (text != null) {
                text = text.replaceAll(System.lineSeparator(), "");
            }
        } catch (Exception e) {
            throw new SystemOperationFailedException(
                    "Error while getting public key in authentication", e.getMessage());
        }
        return text;
    }

    private PublicKey buildPublicKeyFromText(String publicKeyText) {
        PublicKey publicKey = null;
        Certificate cert;
        try {
            byte[] certder = Base64.getDecoder().decode(publicKeyText);
            InputStream certstream = new ByteArrayInputStream(certder);
            cert = CertificateFactory.getInstance("X.509").generateCertificate(certstream);
            publicKey = cert.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
