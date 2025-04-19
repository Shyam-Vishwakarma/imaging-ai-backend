package com.imaging.app.dto;

import com.imaging.app.enums.CreditsChangeType;
import com.imaging.app.model.WalletHistory;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class WalletHistoryResponseDto {
    private String id;
    private Double changeInCredits;
    private CreditsChangeType changeType;
    private LocalDateTime createdAt;

    public static class Mapper {
        public static WalletHistoryResponseDto toDto(WalletHistory walletHistory) {
            WalletHistoryResponseDto dto = new WalletHistoryResponseDto();
            dto.setId(walletHistory.getId());
            dto.setChangeType(walletHistory.getChangeType());
            dto.setChangeInCredits(walletHistory.getChangeInCredits());
            dto.setCreatedAt(walletHistory.getCreatedAt());
            return dto;
        }
    }
}
