package technical.challenge.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technical.challenge.exception.ParserException;
import technical.challenge.model.WebPage;
import technical.challenge.services.WebPageService;

@RestController
public class FormController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WebPageService webPageService;

	@RequestMapping(value = "parse", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebPage> analysePage(@RequestBody WebPage page) throws ParserException {
		logger.info("parse request has started");
		webPageService.analyzeWebPage(page);
		logger.info("parse request has ended");
		return new ResponseEntity<WebPage>(page, HttpStatus.OK);
	}

	/**
	 * get the user name information from returned authenticated url
	 * 
	 * @param principal
	 * @return
	 */
	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
		return map;
	}

}
