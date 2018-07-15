package com.spoloborota.calculator.dao.impl;

import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Calculation> getAllCalculations() {
        String hql = "FROM Calculation AS calc ORDER BY calc.date";
        return (List<Calculation>) entityManager.createQuery(hql).getResultList();
    }
}
