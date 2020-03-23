package root.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import org.aspectj.weaver.patterns.HasMemberTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import root.demo.dto.BankDTO;
import root.demo.dto.OrderDTO;
import root.demo.dto.PaymentDTO;
import root.demo.model.enums.OrderStatus;
import root.demo.model.enums.OrderType;
import root.demo.model.payment.Order;
import root.demo.repositories.OrderRepository;

@CrossOrigin(origins = "/*")
@Controller
@RequestMapping(value = "/orders")
public class PaymentController {

	public static final String successUrl = "success";
	public static final String failedUrl = "failed";
	public static final String errorUrl = "error";
	public static final String bankUrl = "https://localhost:8081/otpBank/payments";

	@Autowired
	OrderRepository orderRepository;

	@PostMapping(value = "/papers")
	public ResponseEntity<BankDTO> getPaymentIdAndUrlFromBank(@RequestBody OrderDTO orderDto) {
		Order order = new Order();
		order.setMerchantOrderId(order.generateId());

		order.setMerchantId(orderDto.getMerchantId());
		order.setAmount(new BigDecimal(orderDto.getAmount()));
		order.setBuyerUsername(orderDto.getBuyerUsername());
		if (orderDto.getFileName() == null) {
			order.setType(OrderType.subscription);
		} else {
			order.setType(OrderType.purchase);
			order.setFileName(orderDto.getFileName());
		}

		order.setTimestamp(LocalDateTime.now());
		order.setStatus(OrderStatus.created);
		orderRepository.save(order);

		PaymentDTO paymentRequest = new PaymentDTO();
		paymentRequest.setAmount(order.getAmount().toString());
		paymentRequest.setErrorUrl(errorUrl);
		paymentRequest.setFailedUrl(failedUrl);
		paymentRequest.setMerchantId(order.getMerchantId());
		paymentRequest.setMerchantOrderId(order.getMerchantOrderId());
		paymentRequest.setMerchantPassword("pass");
		paymentRequest.setSuccessUrl(successUrl);
		paymentRequest.setTimestamp(order.getTimestamp().toString());

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<PaymentDTO> request = new HttpEntity<PaymentDTO>(paymentRequest, headers);
		try {
			ResponseEntity<BankDTO> response = restTemplate.postForEntity(bankUrl, request, BankDTO.class);
			System.out.println("Urls generated");
			return new ResponseEntity<BankDTO>(response.getBody(), HttpStatus.OK);
		} catch (RestClientException e) {
			if (e.getMessage().contains("401")) {
				System.out.println(e.getMessage());
				order.setStatus(OrderStatus.error);
				orderRepository.save(order);
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}

		return new ResponseEntity<BankDTO>(HttpStatus.SERVICE_UNAVAILABLE);
	}

	@PostMapping(value = "papers/{orderAction}", produces = "application/json")
	public ResponseEntity<HashMap<String, String>> finishOrder(@PathVariable String orderAction,
			@RequestBody HashMap<String, String> orderId) {
		Order order = orderRepository.findById(Long.valueOf(orderId.get("merchantOrderId"))).get();

		if (orderAction.equals("success")) {
			HashMap<String, String> map;

			if (order.getType().equals(OrderType.purchase)) {
				map = new HashMap<>();
				map.put("fileName", order.getFileName());
				map.put("orderType", "purchase");
				order.setStatus(OrderStatus.success);
				orderRepository.save(order);
				return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.OK);
			} else {
				map = new HashMap<>();				
				map.put("orderType", "subscription");
				order.setStatus(OrderStatus.success);
				orderRepository.save(order);
				return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.OK);
			}
		} else if (orderAction.equals("failed")) {
			order.setStatus(OrderStatus.failed);
			orderRepository.save(order);
			return new ResponseEntity<HashMap<String, String>>(HttpStatus.OK);
		} else {
			order.setStatus(OrderStatus.error);
			orderRepository.save(order);
			return new ResponseEntity<HashMap<String, String>>(HttpStatus.OK);
		}
		
		//TODO dodaj u listu kupljenih stvari za taj username, to ce bit kao poseban view, taj pregled i to 
	}

}
