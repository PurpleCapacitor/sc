import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {User} from '../../user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) {
  }


  fetchUsers() {
    return this.httpClient.get('https://localhost:8080/user/fetch') as Observable<any>;
  }

  registerUser(list, taskId) {
    return this.httpClient.post('https://localhost:8080/welcome/post/'.concat(taskId), list) as Observable<any>;
  }

  approveReviewer(user, taskId, processInstanceId) {
    // tslint:disable-next-line:max-line-length
    return this.httpClient.post('https://localhost:8080/users/approveReviewer/' + processInstanceId + '/'.concat(taskId), user) as Observable<any>;
  }

  registerScAreas(scAreaList, taskId) {
    return this.httpClient.post('https://localhost:8080/welcome/scAreas/'.concat(taskId), scAreaList) as Observable<any>;
  }

  getEditorTasks(pid, username) {
    return this.httpClient.get('https://localhost:8080/users/editors/' + pid + '/'.concat(username)) as Observable<any>;
  }

  getEditorTaskFields(tid) {
    return this.httpClient.get('https://localhost:8080/users/editors/tasks/'.concat(tid)) as Observable<any>;
  }

  getAuthorTasks(pid, username) {
    return this.httpClient.get('https://localhost:8080/users/tasks/' + pid + '/'.concat(username)) as Observable<any>;
  }

  getAuthorTaskFields(tid) {
    return this.httpClient.get('https://localhost:8080/users/tasks/'.concat(tid)) as Observable<any>;
  }

  getReviewerTasks(pid: string, username: string) {
    return this.httpClient.get('https://localhost:8080/users/reviewers/' + pid + '/'.concat(username)) as Observable<any>;
  }

  getReviewerTaskFields(tid) {
    return this.httpClient.get('https://localhost:8080/users/reviewers/tasks/'.concat(tid)) as Observable<any>;
  }

  getAdminTasks(pid: string, username: string) {
    return this.httpClient.get('https://localhost:8080/users/admins/' + pid + '/'.concat(username)) as Observable<any>;
  }

  getAdminTaskFields(tid) {
    return this.httpClient.get('https://localhost:8080/users/admins/tasks/'.concat(tid)) as Observable<any>;
  }

  getCompletedOrders(username) {
    return this.httpClient.get('https://localhost:8080/orders/completed/'.concat(username)) as Observable<any>;
  }

  completePaymentRequest(taskIdObject) {
    return this.httpClient.post('https://localhost:8080/reviews/paymentRequests/', taskIdObject) as Observable<any>;
  }

  paymentFinish(signal) {
    return this.httpClient.post('https://localhost:8080/reviews/signals/', signal) as Observable<any>;
  }
}
