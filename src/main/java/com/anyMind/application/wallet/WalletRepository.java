package com.anyMind.application.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    // Might need to sort by date time.
    @Query(value = "from Wallet w where datetime BETWEEN :dateFrom AND :dateTo")
    List<Wallet> findBydateBetween(@Param("dateFrom") ZonedDateTime dateFrom, @Param("dateTo") ZonedDateTime dateTo);

    Wallet findTopByOrderByAccountIdDesc();
}
