package br.com.laminarsoft.jazzforms.persistencia.test.crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupCrawlerTest {
	private Map<String, Boolean> linkVisitado = new HashMap<String, Boolean>();
	
	@Test
	public void teste1() throws Exception {
		String url = "http://www.oab.org.br/";

		recuperaLink(url, 1);
	}

	private void recuperaLink(String url, int nivel) throws IOException {
		print("Fetching %s...", url);

		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");
		Elements article = doc.select("article");

//		print("\nMedia: (%d)", media.size());
//		for (Element src : media) {
//			if (src.tagName().equals("img"))
//				print(" * %s: <%s> %sx%s (%s)", src.tagName(),
//						src.attr("abs:src"), src.attr("width"),
//						src.attr("height"), trim(src.attr("alt"), 20));
//			else
//				print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//		}

//		print("\nImports: (%d)", imports.size());
//		for (Element link : imports) {
//			print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"),
//					link.attr("rel"));
//		}

		print("\nLinks: (%d)", links.size());
		if(nivel == 2) {			
			print("\nArticles: (%d)", article.size());
			for(Element element : article) {
				print(" titulo: <%s>", element.getElementsByTag("h1").text());
				print(" imagem: <%s>", element.getElementsByTag("img").attr("abs:src"));
				Elements contents = element.getElementsByClass("content");
				for(Element content : contents) {
					if(content.getElementsByTag("p") != null &&
							content.getElementsByTag("p").size() > 0){
						Elements paragraphs = content.getElementsByTag("p");
						for(Element para : paragraphs) {
							print(" paragrafo: <%s>", para.text());
						}
					}
				}
			}
			return;
		}
		for (Element link : links) {
			if(StringUtils.isNotEmpty(link.text()) && recuperaConteudoLink(link.attr("abs:href"))) {
				//print(" * a: <%s>  (%s)", link.attr("abs:href"), link.text());
			}
		}
		
		System.out.println("--------------------------------------------");
		for(String link : linkVisitado.keySet().toArray(new String[linkVisitado.keySet().size()])) {
			recuperaLink(link, 2);
		}
	}

	private boolean recuperaConteudoLink(String link) {
		boolean ret = false;
		if(link.startsWith("http://www.oab.org.br/noticia") && !linkVisitado.containsKey(link)) {
			ret = true;			
			linkVisitado.put(link, true);
		}
		return ret;
	}
	
	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
