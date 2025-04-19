package com.imaging.app.controller;

import com.imaging.app.dto.WalletHistoryResponseDto;
import com.imaging.app.enums.CreditsChangeType;
import com.imaging.app.security.AuthUtils;
import com.imaging.app.service.WalletHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet/history")
@RequiredArgsConstructor
public class WalletHistoryController {
    private final WalletHistoryService walletHistoryService;

    @GetMapping
    public ResponseEntity<?> getWalletHistory() {
        String userId = AuthUtils.getAuthenticatedUserId();
        List<WalletHistoryResponseDto> walletHistories =
                walletHistoryService.getWalletHistory(userId);
        return ResponseEntity.ok(walletHistories);
    }

    @GetMapping("/type")
    public ResponseEntity<?> getWalletHistoryByType(@RequestParam String type) {
        String userId = AuthUtils.getAuthenticatedUserId();
        CreditsChangeType parsedType = CreditsChangeType.from(type);
        List<WalletHistoryResponseDto> walletHistories =
                walletHistoryService.getWalletHistoryByType(userId, parsedType);
        return ResponseEntity.ok(walletHistories);
    }
}
