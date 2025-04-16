package com.imaging.app.repository;

import com.imaging.app.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Wallet findByUserId(String userId);

    void deleteByUserId(String userId);

    double getCreditByUserId(String userId);
}
