package net.hasibix.hasicraft.discordbot.models.client.utils;

import java.security.Key;
import io.jsonwebtoken.Jwts;

public class JwtUtils {
    public static String createToken(Key secKey, String payload) {
        return Jwts.builder()
            .signWith(secKey)
            .setPayload(payload)
            .compact();
    }
}
