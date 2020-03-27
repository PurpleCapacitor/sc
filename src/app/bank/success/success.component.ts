import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BankService} from '../../services/bank.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.css']
})
export class SuccessComponent implements OnInit {

  private orderAction = 'success';
  private fileName = '';
  private fileDownload = false;
  private editionName = '';
  private openAcccess = false;
  private edition = false;

  constructor(private activatedRoute: ActivatedRoute,
              private bankService: BankService,
              private router: Router,
              private userService: UserService) {
    let merchantOrderId = {'merchantOrderId': this.activatedRoute.snapshot.paramMap.get('merchantOrderId')};
    let orderInfo = this.bankService.finishOrder(this.orderAction, merchantOrderId);
    orderInfo.subscribe(res => {
      if (res.orderType === 'purchase') {
        this.fileName = res.fileName;
        this.fileDownload = true;
      } else if (res.orderType === 'edition') {
        this.editionName = res.editionName;
        this.edition = true;
      } else if (res.orderType === 'openAccess') {
        let signal = {"signal":"paymentCompleted"};
        this.openAcccess = true;
        let submit = this.userService.paymentFinish(signal);
        submit.subscribe(res1 => {
          console.log("process continued");
        });
      }

    });
  }

  ngOnInit() {
  }
}
