package ie.gtludwig.pa.config;

//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = "ie.gtludwig.pa")
public class PaConfiguration /* implements WebMvcConfigurer */ {

//    @Bean
//	@Description("Thymeleaf template resolver serving HTML 5")
//	public ServletContextTemplateResolver templateResolver() {
//		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
//		templateResolver.setCacheable(false);
//		templateResolver.setTemplateMode("HTML5");
//		templateResolver.setCharacterEncoding("UTF-8");
//		templateResolver.setSuffix(".html");
//
//		return templateResolver;
//	}
//
//	@Bean
//	@Description("Thymeleaf template engine with Spring integration")
//	public SpringTemplateEngine templateEngine() {
//		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		// needed?
////		templateEngine.addDialect(new SpringSecurityDialect());
//		templateEngine.addDialect(new LayoutDialect(new GroupingStrategy()));
//		templateEngine.setTemplateResolver(templateResolver());
//
//		return templateEngine;
//	}
//
//	@Bean
//	@Description("Thymeleaf view resolver")
//	public ViewResolver viewResolver() {
//
//		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//		viewResolver.setTemplateEngine(templateEngine());
//		viewResolver.setCharacterEncoding("UTF-8");
//		viewResolver.setCache(false);
//		viewResolver.setOrder(1);
//
//		return viewResolver;
//	}
//
//	@Bean(name = "messageSource")
//	@Description("Spring message resolver")
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
////		messageSource.setBasename(MESSAGE_SOURCE);
//		messageSource.setFallbackToSystemLocale(false);
//		messageSource.setCacheSeconds(0);
//		messageSource.setDefaultEncoding("utf-8");
//		return messageSource;
//	}
//
//	@Bean(name = "localeResolver")
//	@Description("Spring locale resolver")
//	public LocaleResolver localeResolver() {
//		SessionLocaleResolver resolver = new SessionLocaleResolver();
//		resolver.setDefaultLocale(Locale.US);
//		return resolver;
//	}

}
