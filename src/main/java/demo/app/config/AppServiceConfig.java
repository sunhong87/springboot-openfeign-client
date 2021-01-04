package demo.app.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.app.client.PostClient;
import demo.app.service.AsynchronousService;
import demo.app.service.InputParserFactory;
import demo.app.service.impl.PostAsynchronousService;

@Configuration
public class AppServiceConfig {

	@Bean("parserFactory")
	public FactoryBean<Object> inputParserFactory() {
		ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
		factoryBean.setServiceLocatorInterface(InputParserFactory.class);
		return factoryBean;
	}
	
	@Bean
	public AsynchronousService asynchronousService(PostClient postClient) {
		return new PostAsynchronousService(postClient);
	}
}
