package network.karthik.financetracker.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtility {

	
	
	public DateUtility() {
		super();
	}

	
	public boolean getMonthOfDate(String date) {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		String formattedDate =  formatter.format(currentDate);
		String monthOfFormattedDate = formattedDate.substring(5, 7);
		String monthOfReceivedDate = date.substring(5, 7);

		if(monthOfFormattedDate.matches(monthOfReceivedDate)) {
			return true;
		}
		
		return false;
	}
	
}
