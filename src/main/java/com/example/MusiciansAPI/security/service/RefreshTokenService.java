package com.example.MusiciansAPI.security.service;

import com.example.MusiciansAPI.exception.TokenRefreshException;
import com.example.MusiciansAPI.model.RefreshToken;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.TokenRefreshRequest;
import com.example.MusiciansAPI.repository.RefreshTokenRepository;
import com.example.MusiciansAPI.repository.UserRepository;
import com.example.MusiciansAPI.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Optional<RefreshToken> findByToken(String token) {
        //verify that suck token exists in DB
        verifyThatExists(token);
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    /**
     * Create new refresh token
     * @param userId
     * @return Refresh token ***object*** (not String)
     */
    public RefreshToken createRefreshToken(Long userId) {
        var refreshToken = new RefreshToken();

        //Set refresh token object data
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        //Save to repo
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    /**
     * Check provided refresh token and return a new access token upon success
     * @param request should include a valid, non-expired refresh token
     * @return new access token
     */
    public String refreshAccessToken(TokenRefreshRequest request) {
        //Verify token exists & not expired
        var refreshToken = findByToken(request.getRefreshToken()).get();
        verifyExpiration(refreshToken.getToken());

        //Generate new access token
        var user = refreshToken.getUser();
        return jwtTokenProvider.generateToken(user.getId());
    }

    /**
     * Helper method: verifies that a token exists
     */
    private void verifyThatExists(String token) {
        if (refreshTokenRepository.findByToken(token).isEmpty()) {
            throw new TokenRefreshException(token, "This Refresh token " +
                    " isn't assigned to a user. Please login and try again using new refresh access token");
        }
    }

    /**
     * Helper method: verifies whether a token is expired
     */
    private void verifyExpiration(String token) {
        RefreshToken refreshToken = findByToken(token).get();
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(refreshToken.getToken(), "Token expired," +
                    " please login again and use new refresh token");
        }
    }

    /**
     * Delete a token by a user id
     * @param userId
     */
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
