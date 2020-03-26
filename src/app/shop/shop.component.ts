import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../services/repository/repository.service';
import {BankService} from '../services/bank.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  private editions = [];
  private user = JSON.parse(localStorage.getItem("currentUser"));

  constructor(private repositoryService: RepositoryService,
              private bankService: BankService,
              private router: Router) {
    let editions = repositoryService.getEditions();
    editions.subscribe(res => {
      this.editions = res;
    });
  }

  ngOnInit() {
  }

  buy(name, magazineName, price) {
    let item = {"amount": price, "merchantId": magazineName, "buyerUsername": this.user.username, "edition": name};
    let submit = this.bankService.requestPaymentResponse(item);
    submit.subscribe(res => {
      this.router.navigate([res.paymentUrl + '/' + res.paymentId]);
    });
  }
}
