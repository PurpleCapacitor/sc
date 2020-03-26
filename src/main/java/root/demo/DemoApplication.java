package root.demo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import root.demo.model.Edition;
import root.demo.model.Magazine;
import root.demo.model.Paper;
import root.demo.repositories.EditionRepository;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.PaperRepository;
import root.demo.services.storage.StorageService;

@Configuration
@EnableWebMvc
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Resource
	StorageService storageService;
	
	@Autowired
	MagazineRepository magazineRepository;
	
	@Autowired
	EditionRepository editionRepository;
	
	@Autowired
	PaperRepository paperRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.deleteAll();
		storageService.init();

	}

	@EventListener(ApplicationReadyEvent.class)
	public void initData() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<String> request = new HttpEntity<String>("hi", headers);
		try {
			ResponseEntity<String> response = restTemplate.postForEntity("https://localhost:8081/otpBank/test", request, String.class);
			System.out.println("ok");
		} catch(RestClientException e) {
			System.out.println(e.getMessage());
		}
		if(editionRepository.findAll().isEmpty()) {
			Edition edition1 = new Edition();
			Magazine m = magazineRepository.findByName("magazine1");
			List<Paper> papers = paperRepository.findAll();
			edition1.setMagazine(m);
			edition1.setName("Izdanje prvo");
			edition1.setPrice(new BigDecimal("20.5"));
			edition1.setPapers(papers);
			
			editionRepository.save(edition1);
		}
		
		
		
		
		
	}

}
