import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BankService} from '../../services/bank.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.css']
})
export class SuccessComponent implements OnInit {

  private orderAction = "success";
  private fileName = "";
  private fileDownload = false;

  constructor(private activatedRoute: ActivatedRoute, private bankService: BankService,
              private httpClient: HttpClient) {
    let merchantOrderId = {"merchantOrderId":this.activatedRoute.snapshot.paramMap.get("merchantOrderId")};
    let orderInfo = this.bankService.finishOrder(this.orderAction, merchantOrderId);
    orderInfo.subscribe(res => {
      if(res.orderType === "purchase") {
        this.fileName = res.fileName;
        this.fileDownload = true;
      }

      //TODO napravi dugme samo umesto linka da ne ide nazad

    });
  }

  ngOnInit() {
  }

  download() {
    return this.httpClient.get('https://localhost:8080/download/'.concat(this.fileName)) as Observable<any>;
  }
}
