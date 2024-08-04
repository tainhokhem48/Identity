package com.example.SpringBoot.UserService;

import com.example.SpringBoot.dto.request.AuthenticationRequest;
import com.example.SpringBoot.dto.request.IntrospectRequest;
import com.example.SpringBoot.dto.request.LogoutRequest;
import com.example.SpringBoot.dto.request.RefreshRequest;
import com.example.SpringBoot.dto.response.AuthenticationResponse;
import com.example.SpringBoot.dto.response.IntrospectResponse;
import com.example.SpringBoot.entity.InValidateToken;
import com.example.SpringBoot.entity.User;
import com.example.SpringBoot.exception.ErrorCode;
import com.example.SpringBoot.exception.ErrorCodeException;
import com.example.SpringBoot.reponsitory.InvalidatedRepository;
import com.example.SpringBoot.reponsitory.UserReponsitory;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserReponsitory userReponsitory;
    @Autowired
    private InvalidatedRepository invalidatedRepository;
    @Value("${JWT.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean invalid = true;
        try {
            verifyToken(token);
        }catch (ErrorCodeException exception){
            invalid = false;
        }
        return IntrospectResponse.builder()
                .valid(invalid)
                .build();

    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signjwt = verifyToken(request.getToken());
        var jit = signjwt.getJWTClaimsSet().getJWTID();
        var expiryTime = signjwt.getJWTClaimsSet().getExpirationTime();
        InValidateToken inValidateToken = InValidateToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedRepository.save(inValidateToken);
        var username = signjwt.getJWTClaimsSet().getSubject();
        var user = userReponsitory.findByUsername(username).orElseThrow(()->
                new ErrorCodeException(ErrorCode.UNAUTHENTICATED));
        var token = GenerateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }
    public AuthenticationResponse Authenticate(AuthenticationRequest request){
        var user = userReponsitory.findByUsername(request.getUsername())
                .orElseThrow(()-> new ErrorCodeException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticate){
            throw new ErrorCodeException(ErrorCode.LOGIN_NOT_FOUND);

        }
        var token = GenerateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();

    }

    public void logout(LogoutRequest rquest) throws ParseException, JOSEException {
        var signToken = verifyToken(rquest.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InValidateToken inValidateToken = InValidateToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedRepository.save(inValidateToken);
    }
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expiryTime.after(new Date()))){
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
            }

        if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
    private String GenerateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("tai")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", BuildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String BuildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add( "ROLE_"+role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }

            });
        }
        return stringJoiner.toString();
    }
}
