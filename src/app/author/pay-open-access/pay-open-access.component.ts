import { Component, OnInit } from '@angular/core';
import {BankService} from '../../services/bank.service';
import {Router} from '@angular/router';
import {PassParamService} from '../../services/pass-param.service';
import {UsersComponent} from '../../users/users.component';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-pay-open-access',
  templateUrl: './pay-open-access.component.html',
  styleUrls: ['./pay-open-access.component.css']
})
export class PayOpenAccessComponent implements OnInit {

  private user = JSON.parse(localStorage.getItem("currentUser"));
  private magName = localStorage.getItem("magazineName");
  private processInstance = localStorage.getItem("reviewPID");
  private taskId = '';

  constructor(private bankService: BankService,
              private router: Router,
              private passParamService: PassParamService,
              private userService: UserService) {
    this.passParamService.currentTaskId.subscribe(current => this.taskId = current);
  }

  ngOnInit() {
  }

  pay() {
    let order = {"merchantId": this.magName, "buyerUsername": this.user.username, "amount": "10"};

    let task = {"taskId": this.taskId};
    let finishTask = this.userService.completePaymentRequest(task);
    finishTask.subscribe( res => {
      console.log("Task completed");
    });
    let pay = this.bankService.requestPaymentResponse(order);
    pay.subscribe(res => {
      this.router.navigate([res.paymentUrl + '/' + res.paymentId]);
    });

  }
}
