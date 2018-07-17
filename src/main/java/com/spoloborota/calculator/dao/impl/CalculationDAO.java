package com.spoloborota.calculator.dao.impl;

import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
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
    public Integer countByDate(LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> query = cb.createQuery(Calculation.class);
        Root<Calculation> calculation = query.from(Calculation.class);
        query.where(cb.equal(calculation.get("date"), date));
        return entityManager.createQuery(query).getResultList().size();
    }

    @Override
    public Integer countContainsOp(String operation) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> query = cb.createQuery(Calculation.class);
        Root<Calculation> calculation = query.from(Calculation.class);
        query.where(cb.like(calculation.get("expression"), "%" + operation + "%"));
        return entityManager.createQuery(query).getResultList().size();
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
        return null;
    }
}
