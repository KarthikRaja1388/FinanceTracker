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
import network.karthik.financetracker.entity.Income;
import network.karthik.financetracker.repository.IIncomeRepository;
import network.karthik.financetracker.repository.IUserRepository;

@Service
public class IncomeService {

	@Autowired
	private IIncomeRepository incomeRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
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
	
public void buildExcelDocument(Long id) throws IOException {
		
		List<Income> list = incomeRepository.findByUserUserId(id);
		String userName = userRepository.findOne(id).getFirstName();
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Expense Report");
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Amount");
		header.createCell(1).setCellValue("Date");
		header.createCell(2).setCellValue("Source");		
		
		int rowNum = 1;
		
		for(Income income : list) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(income.getAmountReceived());
			row.createCell(1).setCellValue(income.getDateReceived());
			row.createCell(2).setCellValue(income.getSource());		
		}
		
		FileOutputStream out = new FileOutputStream(new File("S:\\STS\\FinanceTracker\\src\\main\\resources\\static\\excel\\"+userName+"_income.xls"));
		workBook.write(out);
		out.close();
		workBook.close();
		
	}
	
	 public void downloadFile(HttpServletRequest request, HttpServletResponse response,Long id) throws IOException {
			
		 String userName = userRepository.findOne(id).getFirstName();
		 File file = new File("S:\\STS\\FinanceTracker\\src\\main\\resources\\static\\excel\\"+userName+"_income.xls");
		 
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
