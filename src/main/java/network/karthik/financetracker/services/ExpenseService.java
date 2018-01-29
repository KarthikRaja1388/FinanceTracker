package network.karthik.financetracker.services;

import java.awt.PageAttributes.MediaType;
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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;

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

	public ExpenseService(IExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}
	
	
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
	
	public float getExpense(Long id) {
		List<Expense> expenses = expenseRepository.findByUserUserId(id);
		float totalExpense = 0;
		for (Expense expense : expenses) {
			if(dateUtility.getMonthOfDate(expense.getDateSpent())) {
				totalExpense = totalExpense + expense.getAmountSpent();
			}
		}
		return totalExpense;
	}
	
	public float getExpenseByCategory(String category,Long id) {
		float foodExpense = 0;
		List<Expense> foodExpenseList = expenseRepository.findByCategoryAndUserUserId(category, id);
		
		for (Expense expense : foodExpenseList) {
			foodExpense = foodExpense + expense.getAmountSpent();
		}
		return foodExpense;
	}
	
	public List<Expense> findExpenseByMonth(Long id){
		List<Expense> expenseList = new ArrayList<>();
		Iterator<Expense> iterator = expenseRepository.findByUserUserId(id).iterator();
		
		while(iterator.hasNext()) {
			expenseList.add(iterator.next());
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
