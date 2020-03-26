import { Component, OnInit } from '@angular/core';
import {BankService} from '../services/bank.service';
import {error} from 'util';

@Component({
  selector: 'app-redirect-test',
  templateUrl: './redirect-test.component.html',
  styleUrls: ['./redirect-test.component.css']
})
export class RedirectTestComponent implements OnInit {

  constructor(private bankService: BankService) {
    let testSc = bankService.testSc();
    testSc.subscribe(res => {
      console.log("Sc ok");
    }, error1 => {
      console.log("Sc error");
    });

    let testOtpBank = bankService.testOtpBank();
    testOtpBank.subscribe(res => {
      console.log("OtpBank ok");
    }, error2 => {
      console.log("OtpBank error");
    });
  }

  ngOnInit() {
  }

}
