package com.imaging.app.service;

import com.imaging.app.model.Wallet;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    public void createWallet(String userId);

    public Optional<Wallet> getWallet(String userId);

    public void updateWallet(String userId, Wallet wallet);

    public void deleteWallet(String userId);

    public Optional<Double> getCredit(String userId);
}
