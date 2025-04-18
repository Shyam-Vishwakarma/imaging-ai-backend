package com.imaging.app.service;

import com.imaging.app.exception.WalletNotFoundException;
import com.imaging.app.model.Wallet;
import com.imaging.app.repository.WalletRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {
    @Value("${app.wallet.initial-credits}")
    private double initialCredits;

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void createWallet(String userId) {
        Wallet wallet =
                Wallet.builder()
                        .userId(userId)
                        .credits(initialCredits)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> getWallet(String userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void updateWallet(String userId, Wallet wallet) {
        Wallet existingWallet =
                walletRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new WalletNotFoundException(userId));
        if (userId.equals(existingWallet.getUserId())) {
            walletRepository.save(wallet);
        }
    }

    @Override
    public void deleteWallet(String userId) {
        walletRepository.deleteByUserId(userId);
    }

    @Override
    public Optional<Double> getCredit(String userId) {
        return walletRepository.getCreditByUserId(userId);
    }
}
