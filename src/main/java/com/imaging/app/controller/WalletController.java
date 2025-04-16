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
    public ResponseEntity<Wallet> getWallet(@RequestParam String userId) {
        AuthUtils.verifyUserAccess(userId);
        Wallet wallet = walletService.getWallet(userId);
        if (wallet == null) {
            throw new WalletNotFoundException(userId);
        } else return ResponseEntity.ok(wallet);
    }

    @GetMapping("/credits")
    public ResponseEntity<?> getCredits(@RequestParam String userId) {
        AuthUtils.verifyUserAccess(userId);
        double credits = walletService.getCredit(userId);
        return ResponseEntity.ok(credits);
    }
}
