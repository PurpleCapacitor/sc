import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BankService {

  constructor(private httpClient: HttpClient) {
  }

  requestPaymentResponse(item) {
    return this.httpClient.post('https://localhost:8080/orders/papers/', item) as Observable<any>;
  }

  submitCC(ccInfo) {
    return this.httpClient.post('https://localhost:8081/otpBank/cc/', ccInfo) as Observable<any>;
  }

  finishOrder(orderAction, merchantOrderId) {
    return this.httpClient.post('https://localhost:8080/orders/papers/' + orderAction, merchantOrderId) as Observable<any>;
  }

  testSc() {
    return this.httpClient.get('https://localhost:8080/orders/test/') as Observable<any>;
  }

  testOtpBank() {
    return this.httpClient.get('https://localhost:8081/otpBank/test/') as Observable<any>;
  }

}
