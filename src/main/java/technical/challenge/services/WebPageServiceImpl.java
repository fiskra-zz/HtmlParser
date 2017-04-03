package technical.challenge.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import technical.challenge.exception.ParserException;
import technical.challenge.model.HyperMediaLink;
import technical.challenge.model.LinkGroup;
import technical.challenge.model.LinkType;
import technical.challenge.model.WebPage;
import technical.challenge.util.WebPageParser;

/**
 * 
 * It presents the implementations of service methods.
 *
 */
@Service("WebPageService")
public class WebPageServiceImpl implements WebPageService {

	@Override
	public String getHTMLVersion(WebPage page) throws ParserException {
		WebPageParser parser = new WebPageParser(page.getUrl());
		return parser.getHTMLVersion();
	}

	@Override
	public String getPageTitle(WebPage page) throws ParserException {
		WebPageParser parser = new WebPageParser(page.getUrl());
		return parser.getTitle();
	}

	@Override
	public Map<String, Long> getHeadingLevels(WebPage page) throws ParserException {
		WebPageParser parser = new WebPageParser(page.getUrl());
		return parser.getHeadingsBylevel();
	}

	@Override
	public Map<LinkGroup, Map<LinkType, List<HyperMediaLink>>> getGroupedHyperMediaLinks(WebPage page)
			throws ParserException {
		WebPageParser parser = new WebPageParser(page.getUrl());
		return parser.getHyperMediaLinks(page.getUrl());
	}

	@Override
	public void analyzeWebPage(WebPage page) throws ParserException {
		WebPageParser parser = new WebPageParser(page.getUrl());
		parser.analyzeWebPage(page);

	}

}
