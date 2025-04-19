package com.imaging.app.service;

import com.imaging.app.dto.WalletHistoryResponseDto;
import com.imaging.app.enums.CreditsChangeType;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface WalletHistoryService {
    public void createWalletHistory(
            String userId, Double changeInCredits, CreditsChangeType changeType);

    public List<WalletHistoryResponseDto> getWalletHistory(String userId);

    public List<WalletHistoryResponseDto> getWalletHistoryByType(
            String userId, CreditsChangeType changeType);
}
