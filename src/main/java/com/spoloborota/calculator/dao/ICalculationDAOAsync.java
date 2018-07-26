package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface ICalculationDAOAsync extends PagingAndSortingRepository<Calculation, String> {

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT COUNT(*) FROM calculation c WHERE c.date = ?1", nativeQuery = true)
    CompletableFuture<BigInteger> countByDate(String date);

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT COUNT(*) FROM calculation c WHERE c.expression LIKE %?1%", nativeQuery = true)
    CompletableFuture<BigInteger> countContainsOp(String operation);

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT * FROM calculation c WHERE c.date = ?1", nativeQuery = true)
    CompletableFuture<Page<Calculation>> listByDate(String date, Pageable page);

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT * FROM calculation c WHERE c.expression LIKE %?1%", nativeQuery = true)
    CompletableFuture<Page<Calculation>> listContainsOp(String operation, Pageable page);
}
