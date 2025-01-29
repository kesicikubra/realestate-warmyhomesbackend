package com.team03.service.user;

import com.team03.entity.user.User;
import com.team03.entity.user.VerificationToken;
import com.team03.repository.user.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public void deleteVerificationToken(VerificationToken token) {
       verificationTokenRepository.delete(token);
    }


    @Scheduled(fixedRate = 120000)
    public void deleteVerificationToken() {

        LocalDateTime now = LocalDateTime.now();
        verificationTokenRepository.deleteByExpiryDateBefore(now);

    }

    public void deleteByUserId(Long userId) {
        verificationTokenRepository.deleteByUserId(userId);
    }
}
