package network.karthik.financetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import network.karthik.financetracker.entity.Income;

@Repository
public interface IIncomeRepository extends JpaRepository<Income, Long>{

	public List<Income> findByUserUserId(Long id);
}
