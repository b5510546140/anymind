package com.anyMind.application.wallet;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WalletController {
    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/balances/{datefrom}/{dateto}")
    public List<WalletResponce> getBalances(@PathVariable String datefrom,@PathVariable String dateto) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime dateTimeFrom = ZonedDateTime.parse(datefrom, formatter);
        ZonedDateTime dateTimeTo = ZonedDateTime.parse(dateto, formatter);
        List<Wallet> depositHistory =  walletRepository.findBydateBetween(dateTimeFrom,dateTimeTo);
        ZonedDateTime endOfHourTimeFrom  = dateTimeFrom.plusHours(1).truncatedTo(ChronoUnit.HOURS);
        ZonedDateTime endOfHourTimeTo  = dateTimeTo.plusHours(1).truncatedTo(ChronoUnit.HOURS);
        List<WalletResponce> walletResult = new ArrayList<>();
        int i =0;
        int maxSize = depositHistory.size()-1;
        Wallet w = depositHistory.get(i);
        Wallet tempWallet = new Wallet();
        tempWallet.setBalance(0.0);
        while(endOfHourTimeFrom.compareTo(endOfHourTimeTo)<= 0) {
            if (w.getDatetime().compareTo(endOfHourTimeFrom)< 0 ) {
                // This is the last index of array
                if (i != maxSize){
                    tempWallet.setBalance(w.getBalance());
                    w = depositHistory.get(++i);
                } else {
                    // last data
                    tempWallet.setBalance(w.getBalance());
                }
            }
            walletResult.add(new WalletResponce(endOfHourTimeFrom,tempWallet.getBalance()));
            endOfHourTimeFrom = endOfHourTimeFrom.plusHours(1);
        }
        return walletResult;
    }

    @PostMapping("/deposit")
    Wallet deposit(@RequestBody Wallet newDeposit) {
        Wallet wallet = walletRepository.findTopByOrderByAccountIdDesc();
        // newDepoit input is occur before last data
        Wallet temp;
        if (wallet.getDatetime().compareTo(newDeposit.getDatetime()) > 0 ){

            List<Wallet> depositHistory =  walletRepository.findBydateBetween(newDeposit.getDatetime(),wallet.getDatetime());
            double prevAmount = newDeposit.getAmount();
            double prevBalance = 0.0;
            ZonedDateTime prevDatetime = newDeposit.getDatetime();
            if (depositHistory.size() > 0) {
                prevBalance = depositHistory.get(0).getBalance() - depositHistory.get(0).getAmount();
                double tempAmount;
                ZonedDateTime tempDatetime ;
                for (Wallet w : depositHistory) {
                    tempAmount = w.getAmount();
                    tempDatetime = w.getDatetime();

                    w.setAmount(prevAmount);
                    w.setDatetime(prevDatetime);

                    w.setBalance(prevBalance + prevAmount);
                    walletRepository.save(w);

                    prevBalance = w.getBalance();
                    prevAmount = tempAmount;
                    prevDatetime = tempDatetime;
                }
            }
            Wallet lastToAdd = new Wallet();
            lastToAdd.setAmount(prevAmount);
            lastToAdd.setDatetime(prevDatetime);

            lastToAdd.setBalance(prevBalance + prevAmount);
            temp = walletRepository.save(lastToAdd);
        }
        else {
            newDeposit.setBalance(wallet.getBalance() + newDeposit.getAmount());
            temp = walletRepository.save(newDeposit);
        }
        return temp;
    }






}
