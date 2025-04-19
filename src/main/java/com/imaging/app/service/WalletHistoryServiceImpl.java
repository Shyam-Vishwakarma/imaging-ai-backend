package com.imaging.app.service;

import com.imaging.app.dto.WalletHistoryResponseDto;
import com.imaging.app.enums.CreditsChangeType;
import com.imaging.app.model.WalletHistory;
import com.imaging.app.repository.WalletHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletHistoryServiceImpl implements WalletHistoryService {
    private final WalletHistoryRepository walletHistoryRepository;
    private final UserService userService;

    @Override
    public void createWalletHistory(
            String userId, Double changeInCredits, CreditsChangeType changeType) {
        if (!userService.userExists(userId)) {
            throw new IllegalArgumentException(
                    "Couldn't create wallet-history, user does not exist");
        }
        WalletHistory walletHistory =
                WalletHistory.builder()
                        .userId(userId)
                        .changeInCredits(changeInCredits)
                        .changeType(changeType)
                        .createdAt(LocalDateTime.now())
                        .build();
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public List<WalletHistoryResponseDto> getWalletHistory(String userId) {
        List<WalletHistory> walletHistories = walletHistoryRepository.findByUserId(userId);
        return walletHistories.stream().map(WalletHistoryResponseDto.Mapper::toDto).toList();
    }

    @Override
    public List<WalletHistoryResponseDto> getWalletHistoryByType(
            String userId, CreditsChangeType changeType) {
        List<WalletHistory> walletHistories =
                walletHistoryRepository.findByUserIdAndChangeType(userId, changeType);
        return walletHistories.stream().map(WalletHistoryResponseDto.Mapper::toDto).toList();
    }
}
