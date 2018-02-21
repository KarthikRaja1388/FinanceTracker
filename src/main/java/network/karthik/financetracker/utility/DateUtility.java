package network.karthik.financetracker.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtility {

	
	
	public DateUtility() {
		super();
	}

	public boolean compareMonth(String date1, String date2) {

		String monthOfDate1 = date1.substring(5, 7);
		String monthOfDate2 = date2.substring(5, 7);
		
		if(monthOfDate1.matches(monthOfDate2))
			return true;
		else
			return false;
	}
	
	public boolean getMonthOfDate(String date) {
		String monthOfFormattedDate = returnCurrentDate().substring(5, 7);
		String monthOfReceivedDate = date.substring(5, 7);

		if(monthOfFormattedDate.matches(monthOfReceivedDate)) {
			return true;
		}
		
		return false;
	}
	
	public String returnCurrentDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		String formattedDate =  formatter.format(date);
		return formattedDate;
	}
	
	public String returnMonthInAlphabet() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MMM-dd");
		String formattedDate = formatter.format(date);
		return formattedDate.substring(5, 8);
	}
}
