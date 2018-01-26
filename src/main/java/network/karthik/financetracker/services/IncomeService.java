package network.karthik.financetracker.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import network.karthik.financetracker.entity.Income;
import network.karthik.financetracker.repository.IIncomeRepository;

@Service
public class IncomeService {

	@Autowired
	private IIncomeRepository incomeRepository;
	
	
	public IncomeService(IIncomeRepository incomeRepository) {
		super();
		this.incomeRepository = incomeRepository;
	}

	public void add(Income income) {
		incomeRepository.save(income);
	}
	
	public float getIncome(Long id) {
		List<Income> incomes = incomeRepository.findByUserUserId(id);
		float totalIncome = 0;
		for (Income income : incomes) {
			totalIncome = totalIncome + income.getAmountReceived();
		}
		return totalIncome;
	}
	
	public List<Income> findIncomeByMonth(Long id){
		List<Income> incomeList = new ArrayList<>();
		Iterator<Income> iterator = incomeRepository.findByUserUserId(id).iterator();
		
		while(iterator.hasNext()) {
			incomeList.add(iterator.next());
		}
		
		return incomeList;
	}
}
