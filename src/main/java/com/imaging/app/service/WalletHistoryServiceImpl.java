package com.imaging.app.service;

import com.imaging.app.enums.CreditsChangeType;
import com.imaging.app.model.WalletHistory;
import com.imaging.app.repository.WalletHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletHistoryServiceImpl implements WalletHistoryService{
    private final WalletHistoryRepository walletHistoryRepository;
    private final UserService userService;
    @Override
    public void createWalletHistory(String userId, Double changeInCredits, CreditsChangeType changeType) {
        if(!userService.userExists(userId)) {
            throw new IllegalArgumentException("Couldn't create wallet-history, user does not exist");
        }
        WalletHistory walletHistory = WalletHistory.builder()
                .userId(userId)
                .changeInCredits(changeInCredits)
                .changeType(changeType)
                .createdAt(LocalDateTime.now())
                .build();
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public List<WalletHistory> getWalletHistory(String userId) {
        return walletHistoryRepository.findByUserId(userId);
    }

    @Override
    public List<WalletHistory> getWalletHistoryByType(String userId, CreditsChangeType changeType) {
        return walletHistoryRepository.findByUserIdAndChangeType(userId, changeType);
    }
}
