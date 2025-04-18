package com.imaging.app.controller;

import com.imaging.app.exception.WalletNotFoundException;
import com.imaging.app.model.Wallet;
import com.imaging.app.security.AuthUtils;
import com.imaging.app.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<Wallet> getWallet() {
        String userId = AuthUtils.getAuthenticatedUserId();
        Wallet wallet =
                walletService
                        .getWallet(userId)
                        .orElseThrow(() -> new WalletNotFoundException(userId));
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/credits")
    public ResponseEntity<Double> getCredits() {
        String userId = AuthUtils.getAuthenticatedUserId();
        Double credits = walletService.getCredit(userId).orElse(0.0);
        return ResponseEntity.ok(credits);
    }
}
