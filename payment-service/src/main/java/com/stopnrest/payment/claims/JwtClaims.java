package com.stopnrest.payment.claims;

import org.springframework.stereotype.Component;
import java.util.Base64;



@Component
public class JwtClaims {
    public String[] extractCredentials(String token) {
        token = token.replace("Bearer ", "");
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String headerSub = new String(decoder.decode(chunks[1]));
        String[] credentials = headerSub.split(",");
        credentials[1] = credentials[1].substring(7,credentials[1].length()-1);
        return credentials;
    }
}