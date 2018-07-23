package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface ICalculationDAOAsync extends PagingAndSortingRepository<Calculation, String> {

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT COUNT(*) FROM calculation c WHERE c.date = ?1", nativeQuery = true)
    CompletableFuture<Long> countByDate(LocalDate date);

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT COUNT(*) FROM calculation c WHERE c.expression LIKE '%?1%'", nativeQuery = true)
    CompletableFuture<Long> countContainsOp(String operation);

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT c FROM calculation c WHERE c.date = ?1", nativeQuery = true)
    CompletableFuture<Page<Calculation>> listByDate(LocalDate date, Pageable page);

    @Async("fixedThreadPoolExecutor")
    @Query(value = "SELECT c FROM calculation c WHERE c.expression LIKE '%?1%'", nativeQuery = true)
    CompletableFuture<Page<Calculation>> listContainsOp(String operation, Pageable page);

    @Async("fixedThreadPoolExecutor")
    CompletableFuture<Page<String>> findAllBy(final Pageable pageable);
}
