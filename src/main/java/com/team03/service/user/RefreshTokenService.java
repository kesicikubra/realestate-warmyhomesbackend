package com.team03.service.user;

import com.team03.entity.user.RefreshToken;
import com.team03.entity.user.User;
import com.team03.i18n.MessageUtil;
import com.team03.repository.user.RefreshTokenRepository;
import com.team03.service.helper.RefreshTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator refreshTokenGenerator;

    @Value("${token.expiry-duration-in-millis}")
    private Long tokenValidityMillis;

    public RefreshToken createRefreshToken(User user) {

        Optional<RefreshToken> refreshTokenSaved = refreshTokenRepository.findByUserId(user.getId());
        if(refreshTokenSaved.isPresent()) {
            return refreshTokenSaved.get();
        }

        Instant expiryDate = Instant.now().plusMillis(tokenValidityMillis);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenGenerator.generateRefreshToken())
                .expiryDate(expiryDate)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(MessageUtil.getMessage ("expired.token"));
        }
        return token;
    }


    public void deleteRefreshTokenByUserId(Long userId) {

        refreshTokenRepository.deleteByUserId(userId);
    }

}
