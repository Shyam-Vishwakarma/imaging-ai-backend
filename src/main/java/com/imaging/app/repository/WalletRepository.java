package com.imaging.app.repository;

import com.imaging.app.model.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId")
    Optional<Wallet> findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wallet w WHERE w.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    @Query("SELECT w.credits FROM Wallet w WHERE w.userId = :userId")
    Optional<Double> getCreditByUserId(@Param("userId") String userId);
}
