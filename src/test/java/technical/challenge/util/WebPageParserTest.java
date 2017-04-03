package technical.challenge.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import technical.challenge.exception.ParserException;
import technical.challenge.model.HTMLVersion;
import technical.challenge.util.WebPageParser;
/**
 * 
 * This class presents unit test methods for web page analyzing
 *
 */

public class WebPageParserTest {
	
	@Test
	public void getTitleShouldReturnGoogle() throws Exception{
		WebPageParser webPageParser = new WebPageParser("http://google.com");
		String title = webPageParser.getTitle();
		assertEquals(title,"Google");
	}
	
	@Test
	public void getTitleShouldReturnEmpty() throws Exception{
		String title = Jsoup.parse("<html><body></body></html>").title();
		assertEquals(title,"");
	}
	
	@Test
	public void getHtmlVersionShouldReturnHtml5() throws Exception{
		Response resp = Jsoup.connect("http://edition.cnn.com/").validateTLSCertificates(true).execute();
		System.out.println(resp.statusCode()+" -"+resp.statusMessage());
		WebPageParser webPageParser = new WebPageParser("http://stackoverflow.com");
		String version = webPageParser.getHTMLVersion();
		assertEquals(version,HTMLVersion.HTML5.getDescription());
	}
	
	@Test
	public void getHtmlVersionShouldReturnHtml4() throws Exception{
		Document doc = Jsoup.parse("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		WebPageParser webPageParser = new WebPageParser(doc);
		assertEquals(webPageParser.getHTMLVersion(),HTMLVersion.HTML4.getDescription());
	}
	
	@Test
	public void exceptionShouldBeTimeOut(){
		try {
			WebPageParser webPageParser = new WebPageParser("http://deelay.me/130000?http://google.com");
		} catch (Exception e) {
			System.out.println(e);
			assertEquals(true, e instanceof SocketTimeoutException);
		}
	}
	@Test
	public void getDomainNameShouldReturnStackOverflow() throws URISyntaxException{
		WebPageParser pageParser = new WebPageParser();
		assertEquals(pageParser.getDomainName("http://stackoverflow.com"), "stackoverflow.com");
	}
	
	@Test
	public void isAbsoluteShouldReturnFalse() throws URISyntaxException{
		WebPageParser pageParser = new WebPageParser();
		assertEquals(pageParser.isAbsolute("../home.html"), false);
	}
	@Test
	public void getHeadingsBylevelShouldReturnEmpty(){
		Document doc = Jsoup.parse("<html><body></body></html>");
		WebPageParser webPageParser = new WebPageParser(doc);
		assertEquals(webPageParser.getHeadingsBylevel().size(), 0);
	}
	@Test
	public void getHeadingsBylevelShouldReturnOneForH1(){
		Document doc = Jsoup.parse("<html><body><h1>\"Hello World\"</h1></body></html>");
		WebPageParser webPageParser = new WebPageParser(doc);
		assertEquals(webPageParser.getHeadingsBylevel().get("h1").longValue(), 1l);
	}
	
	@Test
	public void getHyperMediaLinksShouldReturnNotNull() throws ParserException{
		Document doc = Jsoup.parse("<html><body></body></html>");
		WebPageParser webPageParser = new WebPageParser(doc);
		assertNotNull(webPageParser.getHyperMediaLinks("http://stackoverflow.com"));
	}

}
