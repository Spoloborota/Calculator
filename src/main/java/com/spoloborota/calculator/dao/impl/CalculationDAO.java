package com.spoloborota.calculator.dao.impl;

import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public class CalculationDAO implements ICalculationDAO {

    @PersistenceContext
    private EntityManager entityManager;

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
        countQuery.where(cb.equal(calculation.get("date"), date));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public Long countContainsOp(String operation) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Calculation> calculation = countQuery.from(Calculation.class);
        countQuery.select(cb.count(calculation));
        countQuery.where(cb.like(calculation.get("expression"), "%" + operation + "%"));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public List<Calculation> listByDate(LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> query = cb.createQuery(Calculation.class);
        Root<Calculation> calculation = query.from(Calculation.class);
        query.where(cb.equal(calculation.get("date"), date));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Calculation> listContainsOp(String operation) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> query = cb.createQuery(Calculation.class);
        Root<Calculation> calculation = query.from(Calculation.class);
        query.where(cb.like(calculation.get("expression"), "%" + operation + "%"));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Double popularNumber() {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Calculation> query = cb.createQuery(Calculation.class);
//        Root<Calculation> calculation = query.from(Calculation.class);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Calculation.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        return null;
    }
}
