package br.com.laminarsoft.jazzforms.persistencia.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import br.com.laminarsoft.jazzforms.persistencia.dao.ComponentTypeDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.IconDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.PaginaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.ProjetoDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.TipoEventoDao;

@Ignore
public class TesteBase {

	protected static ClassPathXmlApplicationContext springFactory = null;

	protected TipoEventoDao tipoEventoDao;
	protected ProjetoDao projetoDao;
	protected PaginaDao paginaDao;
	protected ComponentTypeDao componentTypeDao;
	protected IconDao iconDao;
	
	protected RestTemplate restTemplate; 	

	@BeforeClass
	@SuppressWarnings("all")
	public static void initClass() {
		try {
			springFactory = new ClassPathXmlApplicationContext(
					new String[] { "applicationContext-persist-test.xml" });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Before
	public void inicializaObjeto() {
		try {
			tipoEventoDao = (TipoEventoDao)springFactory.getBean("TipoEventoDao");
			projetoDao = (ProjetoDao)springFactory.getBean("ProjetoDao");
			paginaDao = (PaginaDao)springFactory.getBean("PaginaDao");
			componentTypeDao = (ComponentTypeDao)springFactory.getBean("ComponentTypeDao");
			iconDao = (IconDao)springFactory.getBean("IconDao");
			
			restTemplate = (RestTemplate)springFactory.getBean("restTemplate");
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected <T> T getTargetObject(Object proxy, Class<T> targetClass)
			throws Exception {
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			return (T) ((Advised) proxy).getTargetSource().getTarget();
		} else {
			return (T) proxy; // expected to be cglib proxy then, which is
								// simply a specialized class
		}
	}

	@AfterClass
	public static void destroyClass() {
		springFactory.close();
	}

}
