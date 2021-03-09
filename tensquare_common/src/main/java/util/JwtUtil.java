package util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @program: tensquare68
 * @description:
 **/
public class JwtUtil {


    //TODO 创建jwtUtil对象，需要动态注入两个值
    @Value("${jwt.config.ttl}")
    private long ttl;
    @Value("${jwt.config.signName}")
    private String signName;

    /**
     * 签发token
     * @param userId
     * @param role
     * @return
     */
    public String createJwt(String userId, String role){
        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setId(userId)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttl))
                .signWith(SignatureAlgorithm.HS256, signName).compact();
        Claims claims = parseJwt(token);
        System.out.println(claims.toString());
        return token;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims parseJwt(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signName).parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
