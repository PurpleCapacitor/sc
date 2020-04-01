package root.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
import root.demo.dto.EditionDTO;
import root.demo.dto.OrderDTO;
import root.demo.dto.PaperDTO;
import root.demo.dto.PaymentDTO;
import root.demo.dto.UserOrderDTO;
import root.demo.model.Edition;
import root.demo.model.Paper;
import root.demo.model.enums.OrderStatus;
import root.demo.model.enums.OrderType;
import root.demo.model.enums.UserType;
import root.demo.model.payment.Order;
import root.demo.model.users.User;
import root.demo.repositories.EditionRepository;
import root.demo.repositories.OrderRepository;
import root.demo.repositories.UserRepository;

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

	@Autowired
	UserRepository userRepository;

	@Autowired
	EditionRepository editionRepository;
	
	
	@PostMapping(value = "/papers")
	public ResponseEntity<BankDTO> getPaymentIdAndUrlFromBank(@RequestBody OrderDTO orderDto) {
		Order order = new Order();
		order.setMerchantOrderId(order.generateId());

		order.setMerchantId(orderDto.getMerchantId());
		order.setAmount(new BigDecimal(orderDto.getAmount()));
		order.setBuyerUsername(orderDto.getBuyerUsername());
		User user = userRepository.findByUsername(orderDto.getBuyerUsername());
		if (user.getUserType().equals(UserType.author)) { 
			order.setType(OrderType.openAccess);
		} else {
			if (orderDto.getFileName() == null) {
				order.setType(OrderType.edition);
				order.setEdition(orderDto.getEdition());
			} else {
				order.setType(OrderType.purchase);
				order.setFileName(orderDto.getFileName());
			}
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
			} else if (order.getType().equals(OrderType.edition)) {
				map = new HashMap<>();
				map.put("orderType", "edition");
				map.put("editionName", order.getEdition());
				order.setStatus(OrderStatus.success);
				orderRepository.save(order);
				return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.OK);
			} else {
				map = new HashMap<>();
				map.put("orderType", "openAccess");
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

	}

	@GetMapping(value = "completed/{username}")
	public ResponseEntity<List<UserOrderDTO>> getCompletedOrders(@PathVariable String username) {
		List<Order> orderList = orderRepository.findByBuyerUsername(username);
		List<UserOrderDTO> userOrderDTOs = new ArrayList<UserOrderDTO>();

		for (Order order : orderList) {
			UserOrderDTO userOrderDTO = new UserOrderDTO();
			userOrderDTO.setType(order.getType().toString());
			userOrderDTO.setAmount(order.getAmount().toString());
			if (order.getType().equals(OrderType.openAccess)) {
				userOrderDTOs.add(userOrderDTO);
			} else if (order.getType().equals(OrderType.edition)) {
				userOrderDTO.setEdition(order.getEdition());
				userOrderDTOs.add(userOrderDTO);
			} else {
				userOrderDTO.setFileName(order.getFileName());
				userOrderDTOs.add(userOrderDTO);
			}
		}

		return new ResponseEntity<List<UserOrderDTO>>(userOrderDTOs, HttpStatus.OK);
	}

	@GetMapping(value = "shop")
	public ResponseEntity<List<EditionDTO>> getEditions() {
		List<Edition> editions = editionRepository.findAll();
		List<EditionDTO> editionDTOs = new ArrayList<EditionDTO>();
		for (Edition edition : editions) {
			EditionDTO editionDTO = new EditionDTO(edition.getName(), edition.getPrice().toString(),
					edition.getMagazine().getName());
			editionDTOs.add(editionDTO);
		}

		return new ResponseEntity<List<EditionDTO>>(editionDTOs, HttpStatus.OK);
	}
	
	@GetMapping(value = "shop/papers/{edition}")
	public ResponseEntity<List<PaperDTO>> getPapers(@PathVariable String edition) {
		Edition e = editionRepository.findByName(edition);
		List<PaperDTO> papersList = new ArrayList<>();
		for(Paper paper : e.getPapers()) {
			PaperDTO paperDTO = new PaperDTO();
			paperDTO.setTitle(paper.getTitle());
			paperDTO.setFile(paper.getFile());
			papersList.add(paperDTO);
			
		}
		
		return new ResponseEntity<List<PaperDTO>>(papersList,HttpStatus.OK);
	}

	@GetMapping(value = "test")
	public ResponseEntity<String> testConnection() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
