package network.karthik.financetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import network.karthik.financetracker.entity.Expense;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense, Long>{

	public List<Expense> findByCategoryAndUserUserId(String category,Long id);
	public List<Expense> findByUserUserId(Long id);
}
