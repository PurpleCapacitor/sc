package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Magazine;
import root.demo.model.enums.MagazineType;
import root.demo.model.enums.OrderStatus;
import root.demo.model.enums.OrderType;
import root.demo.model.payment.Order;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.OrderRepository;
import root.demo.repositories.UserRepository;

@Service
public class CheckOpenAccessService implements JavaDelegate {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MagazineRepository magazineRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	RuntimeService runtimeService;

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		List<FormSubmissionDto> selectedMag = (List<FormSubmissionDto>) execution.getVariable("selMagazineDetails");
		User author = null;
		Magazine magazine = null;
		for(FormSubmissionDto dto : selectedMag) {
			if(dto.getFieldId().equals("username")) {
				author = userRepository.findByUsername(dto.getFieldValue());				
			} else if(dto.getFieldId().equals("magazineName")) {
				magazine = magazineRepository.findByName(dto.getFieldValue());
			}
		}
		
		if(magazine.getType().equals(MagazineType.openAccess)) {
			Order order = orderRepository.findByMerchantIdAndBuyerUsernameAndTypeAndStatus(magazine.getName(),
					author.getUsername(), OrderType.openAccess, OrderStatus.success);
			if(order == null) {
				runtimeService.setVariable(execution.getId(), "subPaid", false);
			} else {
				runtimeService.setVariable(execution.getId(), "subPaid", true);
			}
			
		}
		
		
		
	}

}
