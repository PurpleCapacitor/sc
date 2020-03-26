import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BankService} from '../services/bank.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.css']
})
export class BankComponent implements OnInit {

  private paymentId = "";

  constructor(private bankService: BankService, private router: Router, private activatedRoute: ActivatedRoute) {
  }

  formModel = {
    pan: null,
    cardHolderName: null,
    ccv: null,
    validThru: null
  };

  ngOnInit() {
    this.paymentId = this.activatedRoute.snapshot.paramMap.get("paymentId");
  }

  onSubmit(paymentForm: NgForm) {
    if (paymentForm.valid === true) {
      // TODO sredi datum format
      alert(this.paymentId);
      let submittedData = paymentForm.form.value;
      submittedData["paymentId"] = this.paymentId;
      let submit = this.bankService.submitCC(submittedData);
      submit.subscribe(res => {
        this.router.navigate([res.url + '/' + res.merchantOrderId]);
      }, error => {
        this.router.navigate([error.error.url + '/' + error.error.merchantOrderId]);
      });
    }
  }

}
