package technical.challenge.services;

import java.util.List;
import java.util.Map;

import technical.challenge.exception.ParserException;
import technical.challenge.model.HyperMediaLink;
import technical.challenge.model.LinkGroup;
import technical.challenge.model.LinkType;
import technical.challenge.model.WebPage;
/**
 * 
 * It presents service layer  
 *
 */
public interface WebPageService {

	String getHTMLVersion(WebPage page) throws ParserException;

	String getPageTitle(WebPage page) throws ParserException;
	
	Map<String, Long> getHeadingLevels(WebPage page) throws ParserException;
	
	Map<LinkGroup, Map<LinkType,List<HyperMediaLink>>> getGroupedHyperMediaLinks(WebPage page) throws ParserException;
	
	void analyzeWebPage(WebPage page) throws ParserException; 

}
