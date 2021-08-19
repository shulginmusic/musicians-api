package com.example.MusiciansAPI.repository;

import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

    Optional<RefreshToken> findByToken(String token);


}
