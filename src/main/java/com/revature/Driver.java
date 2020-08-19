package com.revature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.utils.ConsoleUtil;

public class Driver {
	
	private static final Logger log = LogManager.getLogger(Driver.class);
	
	public static void main(String[] args) {
		
		log.info("The application has started");
		
		ConsoleUtil cons = new ConsoleUtil();
		cons.beginApp();
		

	}

}
