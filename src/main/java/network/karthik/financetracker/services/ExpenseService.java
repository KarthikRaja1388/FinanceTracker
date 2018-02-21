package network.karthik.financetracker.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import network.karthik.financetracker.entity.Expense;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.repository.IExpenseRepository;
import network.karthik.financetracker.repository.IUserRepository;
import network.karthik.financetracker.utility.DateUtility;

@Service
public class ExpenseService{
	
	@Autowired
	private IExpenseRepository expenseRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private DateUtility dateUtility;

	
	public ExpenseService() {
		super();
	}

	/*public ExpenseService(IExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}
	
	public ExpenseService(IUserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public ExpenseService(DateUtility dateUtility) {
		super();
		this.dateUtility = dateUtility;
	}*/


	public void add(Expense expense) {
		expenseRepository.save(expense);
	}
	
	public void update(Expense expense,User user) {
		Expense expenseById = expenseRepository.findOne(expense.getExpenseId());
		
		expenseById.setAmountSpent(expense.getAmountSpent());
		expenseById.setDateSpent(expense.getDateSpent());
		expenseById.setPlaceSpent(expense.getPlaceSpent());
		expenseById.setUser(user);
		
		expenseRepository.save(expenseById);
	}
	
	public void delete(Long expenseId) {
		expenseRepository.delete(expenseId);
	}
	
	public Expense getExpenseById(Long id) {
		return expenseRepository.findOne(id);
	}
	
	public float getAllExpense(Long id) {
		List<Expense> expenses = expenseRepository.findByUserUserId(id);
		float totalExpense = 0;
		for (Expense expense : expenses) {
				totalExpense = totalExpense + expense.getAmountSpent();
		}
		return totalExpense;
	}
	/**
	 * Method to return the total expense of Current month
	 * @param id holds the value of current user
	 * @return float Total of expense
	 */
	
	public float getExpense(Long id) {
		List<Expense> expenses = expenseRepository.findByUserUserId(id);

		float totalExpense = 0;
		if(!expenses.isEmpty()) {
			for (Expense expense : expenses) {
				if (dateUtility.compareMonth(dateUtility.returnCurrentDate(), expense.getDateSpent())) {
					totalExpense = totalExpense + expense.getAmountSpent();
				}
			}
		}
		return totalExpense;
	}
	
	public float getExpenseByCategory(String category,Long id) {
		float categoryExpense = 0;
		List<Expense> expenseList = expenseRepository.findByCategoryAndUserUserId(category, id);
		if(!expenseList.isEmpty()) {
			for (Expense expense : expenseList) {
				if(dateUtility.compareMonth(dateUtility.returnCurrentDate(), expense.getDateSpent())) {
					categoryExpense = categoryExpense + expense.getAmountSpent();
				}
			}
		}
		return categoryExpense;
	}
	
	public List<Expense> findExpenseForCurrentMonth(Long id){
		List<Expense> expenseList = new ArrayList<>();

		List<Expense> expenses = expenseRepository.findByUserUserId(id);
		
		for(Expense expense : expenses) {
			if(dateUtility.compareMonth(dateUtility.returnCurrentDate(), expense.getDateSpent())) {
				expenseList.add(expense);
			}
		}
		return expenseList;
		
	}
	
	public List<Expense> findAllExpense(Long id){
		List<Expense> expenseList = new ArrayList<>();
		Iterator<Expense> iterator = expenseRepository.findByUserUserId(id).iterator();
		
		while(iterator.hasNext()) {
			expenseList.add(iterator.next());
		}
		
		return expenseList;
	}
	
	public List<Expense> findExpenseForChosenMonth(Long id,String month){
		String date = null;
		List<Expense> expenseList = new ArrayList<>();
		if(month.equalsIgnoreCase("Jan"))date = "2018-01-01";
		if(month.equalsIgnoreCase("Feb"))date = "2018-02-01";
		if(month.equalsIgnoreCase("Mar"))date = "2018-03-01";		
		if(month.equalsIgnoreCase("Apr"))date = "2018-04-01";		
		if(month.equalsIgnoreCase("May"))date = "2018-05-01";		
		if(month.equalsIgnoreCase("Jun"))date = "2018-06-01";		
		if(month.equalsIgnoreCase("Jul"))date = "2018-07-01";		
		if(month.equalsIgnoreCase("Aug"))date = "2018-08-01";		
		if(month.equalsIgnoreCase("Sep"))date = "2018-09-01";		
		if(month.equalsIgnoreCase("Oct"))date = "2018-10-01";		
		if(month.equalsIgnoreCase("Nov"))date = "2018-11-01";		
		if(month.equalsIgnoreCase("Dec"))date = "2018-12-01";	
		
		List<Expense> fetchedExpenseList = expenseRepository.findByUserUserId(id);
			for(Expense expense : fetchedExpenseList) {
				if(dateUtility.compareMonth(date, expense.getDateSpent())) {
					expenseList.add(expense);
				}
			}
		
		return expenseList;
		
	}
	
	public void buildExcelDocument(Long id) throws IOException {
		
		List<Expense> list = expenseRepository.findByUserUserId(id);
		String userName = userRepository.findOne(id).getFirstName();
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Expense Report");
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Amount");
		header.createCell(1).setCellValue("Date");
		header.createCell(2).setCellValue("Place");		
		header.createCell(3).setCellValue("Category");
		
		int rowNum = 1;
		
		for(Expense expense : list) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(expense.getAmountSpent());
			row.createCell(1).setCellValue(expense.getDateSpent());
			row.createCell(2).setCellValue(expense.getPlaceSpent());		
			row.createCell(3).setCellValue(expense.getCategory());
		}
		
		FileOutputStream out = new FileOutputStream(new File("S:\\STS\\FinanceTracker\\src\\main\\resources\\static\\excel\\"+userName+"_expense.xls"));
		workBook.write(out);
		out.close();
		workBook.close();
		
	}
	
	 public void downloadFile(HttpServletRequest request, HttpServletResponse response,Long id) throws IOException {
			
		 String userName = userRepository.findOne(id).getFirstName();
		 File file = new File("S:\\STS\\FinanceTracker\\src\\main\\resources\\static\\excel\\"+userName+"_expense.xls");
		 
		 InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		 String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
		 
		 if(mimeType == null) {
			 mimeType = "application/octet-stream";
		 }
		 
		 response.setContentType(mimeType);
		 response.setContentLength((int) file.length());
		 response.setHeader("content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));
		 
		 FileCopyUtils.copy(inputStream, response.getOutputStream());
	 }
	      
	
}
