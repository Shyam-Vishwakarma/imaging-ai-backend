package com.imaging.app.service;

import com.imaging.app.enums.CreditsChangeType;
import com.imaging.app.model.WalletHistory;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface WalletHistoryService {
    public void createWalletHistory(
            String userId, Double changeInCredits, CreditsChangeType changeType);

    public List<WalletHistory> getWalletHistory(String userId);

    public List<WalletHistory> getWalletHistoryByType(String userId, CreditsChangeType changeType);
}
