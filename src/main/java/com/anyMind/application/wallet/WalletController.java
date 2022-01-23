package com.anyMind.application.wallet;

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
        while(endOfHourTimeFrom.compareTo(endOfHourTimeTo)<= 0) {
            if (w.getDatetime().compareTo(endOfHourTimeFrom)< 0 ) {
                // This is the last index of array
                if (i != maxSize){
                    w = depositHistory.get(++i);
                }
            }
            walletResult.add(new WalletResponce(endOfHourTimeFrom,w.getBalance()));
            endOfHourTimeFrom = endOfHourTimeFrom.plusHours(1);
        }
        return walletResult;
    }

    @PostMapping("/deposit")
    Wallet deposit(@RequestBody Wallet newDepoit) {
        Wallet wallet = walletRepository.findTopByOrderByAccountIdDesc();
        if (wallet.getDatetime().compareTo(newDepoit.getDatetime()) > 0 ){
    // newDepoit input is occur before last data
        }
        newDepoit.setBalance(wallet.getBalance()+newDepoit.getAmount());
        return walletRepository.save(newDepoit);
    }






}
