package top.hcy.webtable.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.entity.UserDetails;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtils implements Serializable {
    private static final long serialVersionUID = -3301605591108950415L;

    private static String secret = WConstants.JWT_SECRET ;



    private static Clock clock = DefaultClock.INSTANCE;

    public static String generateToken(String str, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, str,expiration);
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject, long expiration) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate, expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private static Date calculateExpirationDate(Date createdDate, long expiration) {
        return new Date(createdDate.getTime() + expiration);
    }

    public static Boolean validateToken(String token, String str) {
        //防止因为恶意token导致异常
        try{
            final String username = getDataFromToken(token);
            return (username.equals(str)
                    && !isTokenExpired(token)
            );
        }catch (Exception e){
            log.error("token format error :" + token);
            return false;
        }

    }

    public static String getDataFromToken(String token) {

        //防止因为恶意token导致异常
        try{
            return getClaimFromToken(token, Claims::getSubject);

        }catch (Exception e){
            log.error("token format error :" + token);
            return "";
        }

    }

    private static  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    public static Boolean isTokenExpired(String token) {
        //防止因为恶意token导致异常
        try{
            final Date expiration;
            expiration = getExpirationDateFromToken(token);
            return expiration.before(clock.now());
        }catch (Exception e){
            log.error("token format error :" + token);
            return true;
        }
    }

    public static Date getExpirationDateFromToken(String token) {

        return getClaimFromToken(token, Claims::getExpiration);

    }

}
