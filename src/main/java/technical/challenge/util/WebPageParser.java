package technical.challenge.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import technical.challenge.exception.ParserException;
import technical.challenge.model.HTMLVersion;
import technical.challenge.model.HyperMediaLink;
import technical.challenge.model.LinkGroup;
import technical.challenge.model.LinkType;
import technical.challenge.model.WebPage;
/**
 * 
 * This class presents a bunch of methods which provides to analyze a given url
 * 
 *
 */
public class WebPageParser {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Document doc;
	
	private static final String HEADINGS = "h1, h2, h3, h4, h5, h6";

	public WebPageParser(String url) throws ParserException {
		try {
			if (doc == null) {
				doc = Jsoup.connect(url).timeout(2000).get();
			}
		} catch (Exception e) {
			logger.error("Could not be created JSoup document "+ e.getMessage());
			throw new ParserException("Could not be created JSoup document.");
		}
	}
	public WebPageParser(Document doc){
		this.doc = doc;
	}
	
	public WebPageParser() {
		// TODO Auto-generated constructor stub
	}
	
	public String getTitle() {
		return doc.title();
	}

	public String getHTMLVersion() {

		List<Node> nods = doc.childNodes();
		for (Node node : nods) {
			if (node instanceof DocumentType) {
				DocumentType documentType = (DocumentType) node;
				if (StringUtils.isEmpty(documentType.attr("publicid")))
					return HTMLVersion.HTML5.getDescription();
				
				return HTMLVersion.HTML4.getDescription();
			}
		}

		return HTMLVersion.HTMLNo.getDescription();
	}

	/**
	 * headings grouped by heading level
	 * @return
	 */
	public  Map<String, Long> getHeadingsBylevel() {

		Elements hTags = doc.select(HEADINGS);
		return hTags.stream().collect(Collectors.groupingBy(element -> element.tagName(), Collectors.counting()));
	}


	
	/**
	 * Hypermedia links in the document grouped into internal links
	 * to the same domain and external links to the other domains
	 * @param url
	 * @return
	 * @throws ParserException 
	 */
	public Map<LinkGroup, Map<LinkType,List<HyperMediaLink>>> getHyperMediaLinks(String url) throws ParserException {
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");
		Map<LinkGroup, Map<LinkType,List<HyperMediaLink>>> groupedLinks = new HashMap<LinkGroup, Map<LinkType,List<HyperMediaLink>>>();
		List<HyperMediaLink> hyperMediaLinks = new ArrayList<>();
		try {
			for (Element src : media) {
				HyperMediaLink hyperMediaLink = new HyperMediaLink();
				hyperMediaLink.setTagName(src.tagName());
				hyperMediaLink.setUrl(src.attr("abs:src"));
				hyperMediaLink.setLinkType(LinkType.MEDIA);
				identifyGroup(url, hyperMediaLink);
				hyperMediaLinks.add(hyperMediaLink);
			}
			
			for (Element link : imports) {
				HyperMediaLink hyperMediaLink = new HyperMediaLink();
				hyperMediaLink.setTagName(link.tagName());
				hyperMediaLink.setUrl(link.attr("abs:href"));
				hyperMediaLink.setLinkType(LinkType.IMPORT);
				identifyGroup(url, hyperMediaLink);
				hyperMediaLinks.add(hyperMediaLink);
	        }
			
			for (Element link : links) {
				HyperMediaLink hyperMediaLink = new HyperMediaLink();
				hyperMediaLink.setTagName(link.tagName());
				hyperMediaLink.setUrl(link.attr("abs:href"));
				hyperMediaLink.setLinkType(LinkType.LINK);
				identifyGroup(url, hyperMediaLink);
				hyperMediaLinks.add(hyperMediaLink);
	        }
			
			groupedLinks = hyperMediaLinks.stream().collect(Collectors.groupingBy(HyperMediaLink::getLinkGroup,Collectors.groupingBy(HyperMediaLink::getLinkType)));

		} catch (URISyntaxException e) {
			logger.error("Some hypermedia links could not be parsed. Please check the url"+ e.getMessage());
			throw new ParserException("Some hypermedia links could not be parsed.");
		}
		return groupedLinks;
	}
	
	/**
	 * check is absolute
	 * @param url
	 * @return
	 * @throws URISyntaxException
	 */
	public boolean isAbsolute(String url) throws URISyntaxException{
		URI uri = new URI(url);
		return uri.isAbsolute();
	}
	
	/**
	 * get domain name
	 * @param url
	 * @return
	 * @throws URISyntaxException
	 */
	public String getDomainName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String domain = uri.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
	
	/**
	 * check domain 
	 * @param url
	 * @param hyperLink
	 * @return
	 * @throws URISyntaxException
	 */
	private boolean checkDomain(String url, HyperMediaLink hyperLink) throws URISyntaxException{
		return getDomainName(url).equals(getDomainName(hyperLink.getUrl()));
	}
	
	
	/**
	 * identify is external or internal
	 * @param url
	 * @param hyperLink
	 * @throws URISyntaxException
	 */
	private void identifyGroup(String url, HyperMediaLink hyperLink) throws URISyntaxException{
		if(isAbsolute(url) && checkDomain(url, hyperLink))
			hyperLink.setLinkGroup(LinkGroup.INTERNAL);
		else
			hyperLink.setLinkGroup(LinkGroup.EXTERNAL);	
	}
	
	/**
	 * 
	 * @param page
	 * @throws ParserException
	 */
	public void analyzeWebPage(WebPage page) throws ParserException{
		logger.info("Url analyse begin...");
		page.setPageTitle(getTitle());
		page.setVersion(getHTMLVersion());
		page.setHeadingLevels(getHeadingsBylevel());
		page.setGroupedHypermediaLinks(getHyperMediaLinks(page.getUrl()));
		logger.info("Url analyse completed...");
	}
	

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

}
