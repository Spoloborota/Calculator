package com.spoloborota.calculator.dao.impl;

import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.spoloborota.calculator.common.Constants.*;

@Repository
public class CalculationDAO implements ICalculationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void addCalculation(Calculation calc) {
        entityManager.persist(calc);
    }

    @Override
    public Long countByDate(LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Calculation> calculation = countQuery.from(Calculation.class);
        countQuery.select(cb.count(calculation));
        countQuery.where(cb.equal(calculation.get(DATE_COLUMN), date));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public Long countContainsOp(String operation) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Calculation> calculation = countQuery.from(Calculation.class);
        countQuery.select(cb.count(calculation));
        countQuery.where(cb.like(calculation.get(EXPRESSION_COLUMN), ANY_STRING + operation + ANY_STRING));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public Page<Calculation> listByDate(LocalDate date, Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> selectQuery = cb.createQuery(Calculation.class);
        Root<Calculation> calculation = selectQuery.from(Calculation.class);
        selectQuery.where(cb.equal(calculation.get(DATE_COLUMN), date));

        TypedQuery<Calculation> pagedQuery = entityManager.createQuery(selectQuery);
        pagedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());
        pagedQuery.setMaxResults(page.getPageSize());
        return new PageImpl<>(pagedQuery.getResultList());
    }

    @Override
    public Page<Calculation> listContainsOp(String operation, Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> criteriaQuery = cb.createQuery(Calculation.class);
        Root<Calculation> from = criteriaQuery.from(Calculation.class);
        criteriaQuery.where(cb.like(from.get(EXPRESSION_COLUMN), ANY_STRING + operation + ANY_STRING));

        TypedQuery<Calculation> pagedQuery = entityManager.createQuery(criteriaQuery);
        pagedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());
        pagedQuery.setMaxResults(page.getPageSize());
        return new PageImpl<>(pagedQuery.getResultList());
    }

    @Override
    public Page<String> getAllExpressions(Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = cb.createQuery(String.class);
        Root<Calculation> from = criteriaQuery.from(Calculation.class);

        TypedQuery<String> pagedQuery = entityManager.createQuery(criteriaQuery);
        pagedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());
        pagedQuery.setMaxResults(page.getPageSize());
        return new PageImpl<>(pagedQuery.getResultList());
    }
}
