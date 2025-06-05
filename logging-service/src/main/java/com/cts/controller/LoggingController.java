package com.cts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/logging")
public class LoggingController {
	private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);
	
	@PostMapping("/post")
	public ResponseEntity<Void> logMessage(@RequestParam String level, @RequestBody String logMessage) {
	    switch (level.toUpperCase()) {
	        case "ERROR":
	            logger.error(logMessage);
	            break;
	        case "WARN":
	            logger.warn(logMessage);
	            break;
	        default:
	            logger.info(logMessage);
	            break;
	    }
	    return ResponseEntity.ok().build();
	}

	

}

