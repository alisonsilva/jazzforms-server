package br.com.laminarsoft.jazzforms.negocio.controller.util;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.logging.types.LoggingAutenticacaoMSG;
import br.com.laminarsoft.jazzforms.logging.types.LoggingErrorMSG;
import br.com.laminarsoft.jazzforms.logging.types.MobileLoggingMSG;

@Component
public class LogController {
	
	@Autowired private PropertiesServiceController propertiesServiceController;
	
	@Resource(mappedName="java:/queue/errorQ") private Queue errorQueue;	
	@Resource(mappedName="java:/queue/loggingMobileQ") private Queue mobileQueue;
	@Resource(mappedName="java:/queue/autenticacaoQ") private Queue autenticacaoQueue;
	@Resource(mappedName="java:/ConnectionFactory") private ConnectionFactory connectionFactory;
	

	public LogController() {
	}
	
	public void enviaMensagemErro(LoggingErrorMSG mensagem) {
	    Connection connection = null;
        try {
			connection = connectionFactory.createConnection();
	        Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
	        MessageProducer producer = session.createProducer(errorQueue);
            connection.start();
            
            ObjectMessage msg = session.createObjectMessage(mensagem);
            producer.send(msg);
            session.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void enviaMensagemLogMobile(MobileLoggingMSG mensagem) {
	    Connection connection = null;
        try {
			connection = connectionFactory.createConnection();
	        Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
	        MessageProducer producer = session.createProducer(mobileQueue);
            connection.start();
            
            ObjectMessage msg = session.createObjectMessage(mensagem);
            producer.send(msg);
            session.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}		
	}   
	
	public void enviaMensagemLogAutenticacao(LoggingAutenticacaoMSG mensagem) {
	    Connection connection = null;
        try {
			connection = connectionFactory.createConnection();
	        Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
	        MessageProducer producer = session.createProducer(autenticacaoQueue);
            connection.start();
            
            ObjectMessage msg = session.createObjectMessage(mensagem);
            producer.send(msg);
            session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
