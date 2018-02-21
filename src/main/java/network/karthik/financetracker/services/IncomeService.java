package network.karthik.financetracker.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import network.karthik.financetracker.entity.Income;
import network.karthik.financetracker.repository.IIncomeRepository;
import network.karthik.financetracker.repository.IUserRepository;
import network.karthik.financetracker.utility.DateUtility;

@Service
public class IncomeService {

	@Autowired
	private IIncomeRepository incomeRepository;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private DateUtility dateUtility;
	
	
	public IncomeService() {
		super();
	}

	/*public IncomeService(IIncomeRepository incomeRepository) {
		super();
		this.incomeRepository = incomeRepository;
	}

	public IncomeService(IUserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}*/


	public void add(Income income) {
		incomeRepository.save(income);
	}
	
	public Income getIncomeById(Long id) {
		return incomeRepository.findOne(id);
	}
	
	public float getAllIncome(Long id) {
		List<Income> incomes = incomeRepository.findByUserUserId(id);
		float totalIncome = 0;
		for (Income income : incomes) {
			totalIncome = totalIncome + income.getAmountReceived();
		}
		
		return totalIncome;
	}
	
	/**
	 * 
	 * @param id  Receives the value of User Id
	 * @return totalIncome Total income based on user id passed
	 */
	public float getIncome(Long id) {
		List<Income> incomes = incomeRepository.findByUserUserId(id);
		float totalIncome = 0;
		for (Income income : incomes) {
			if(dateUtility.compareMonth(dateUtility.returnCurrentDate(), income.getDateReceived())) {
				totalIncome = totalIncome + income.getAmountReceived();
			}
		}
		return totalIncome;
	}
	/**
	 * Method to fetch income based on the User ID for current month
	 * 
	 * @param id  Receives the value of User Id
	 * @return incomeList List of income by month based on the Use Id passed
	 */
	public List<Income> findIncomeByMonth(Long id){
		List<Income> incomeList = new ArrayList<>();
		List<Income> incomes = incomeRepository.findByUserUserId(id);
		
		for(Income income : incomes) {
			if(dateUtility.compareMonth(dateUtility.returnCurrentDate(), income.getDateReceived())) {
				incomeList.add(income);
			}
		}
		
		return incomeList;
	}
	
	public List<Income> findIncomeForChosenMonth(Long id,String month){
		String date = null;
		List<Income> incomeList = new ArrayList<>();
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
		
		List<Income> fetchedIncomeList = incomeRepository.findByUserUserId(id);
		if(fetchedIncomeList != null) {
			for(Income income : fetchedIncomeList) {
				if(dateUtility.compareMonth(income.getDateReceived(), date)) {
					incomeList.add(income);
				}
			}
		}
		
		return incomeList;
		
	}
	/**
	 * Method to Build Excel document from the values fetched from DB
	 * 
	 * @param id holds the value of ID passed
	 * @throws IOException IO Exception
	 */
	
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
	/**
	 * Method for Downloading file
	 * 
	 * @param request HttpRequest
	 * @param id User id passed
	 * @throws IOException IOException
	 */
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
