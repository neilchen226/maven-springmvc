package net.kingtrans;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {// 1

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 注解启动spring容器
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		// 设置spring容器的配置类为 MyMvcConfig.java
		ctx.register(MyMvcConfig.class);// 
		// 
		ctx.setServletContext(servletContext); // 2

		Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx)); // 3
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);
		servlet.setAsyncSupported(true);// 1

	}

}
