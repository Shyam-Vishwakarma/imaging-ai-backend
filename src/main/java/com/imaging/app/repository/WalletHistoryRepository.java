package com.imaging.app.repository;

import com.imaging.app.enums.CreditsChangeType;
import com.imaging.app.model.WalletHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, String> {
    @Query("SELECT wh FROM WalletHistory wh WHERE wh.userId = :userId")
    List<WalletHistory> findByUserId(@Param("userId") String userId);

    @Query(
            "SELECT wh FROM WalletHistory wh WHERE wh.userId = :userId AND wh.changeType = :changeType")
    List<WalletHistory> findByUserIdAndChangeType(
            @Param("userId") String userId, @Param("changeType") CreditsChangeType changeType);
}
