import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BankService} from '../../services/bank.service';

@Component({
  selector: 'app-failed',
  templateUrl: './failed.component.html',
  styleUrls: ['./failed.component.css']
})
export class FailedComponent implements OnInit {

  private orderAction = "failed";

  constructor(private activatedRoute: ActivatedRoute, private bankService: BankService) {
    let merchantOrderId = {"merchantOrderId":this.activatedRoute.snapshot.paramMap.get("merchantOrderId")};
    let orderInfo = this.bankService.finishOrder(this.orderAction, merchantOrderId);
    orderInfo.subscribe(res => {});

    // TODO mozda hendlanje koja je greska bila u pitanju
  }

  ngOnInit() {
  }

}
