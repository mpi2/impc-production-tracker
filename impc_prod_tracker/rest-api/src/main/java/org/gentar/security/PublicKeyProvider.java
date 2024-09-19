/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.security;

import org.gentar.exceptions.SystemOperationFailedException;
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

/**
 * Class in charge to provide a public key to be used to verify signatures in tokens
 * provided by an authentication service.
 * @author Mauricio Martinez
 */
@Component
public class PublicKeyProvider
{
    private final RestTemplate restTemplate;
    private PublicKey publicKey;
    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";

    public PublicKeyProvider(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * Get the public key from an authentication service.
     * @param authenticationServiceUrl Url to get the public key.
     * @return The public key.
     */
    @Cacheable("publicKey")
    public PublicKey getPublicKey(String authenticationServiceUrl)
    {
        if (publicKey == null)
        {
            publicKey = buildPublicKeyFromText(requestPublicKeyText(authenticationServiceUrl));
        }
        return publicKey;
    }

    private String requestPublicKeyText(String authenticationServiceUrl)
    {
        String text;
        try
        {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(authenticationServiceUrl, String.class);
            text = response.getBody();
            if (text != null)
            {
                text = text
                        .replaceAll(BEGIN_CERT, "")
                        .replaceAll(END_CERT, "")
                        .replaceAll(System.lineSeparator(), "");
            }
        }
        catch (Exception e)
        {
            throw new SystemOperationFailedException(
                    "Error while getting public key in authentication", e.getMessage());
        }
        return text;
    }

    private PublicKey buildPublicKeyFromText(String publicKeyText)
    {
        PublicKey publicKey = null;
        Certificate cert;
        try
        {
            byte[] certder = Base64.getDecoder().decode(publicKeyText);
            InputStream certstream = new ByteArrayInputStream(certder);
            cert = CertificateFactory.getInstance("X.509").generateCertificate(certstream);
            publicKey = cert.getPublicKey();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return publicKey;
    }
}