import { Injectable } from '@angular/core';

import { Headers, RequestOptions, ResponseContentType } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  categories = [];
  languages = [];
  books = [];

  constructor(private httpClient: HttpClient) {

  }




  startProcess() {
    return this.httpClient.get('https://localhost:8080/welcome/formFields') as Observable<any>;
  }

  getTasks(processInstance: string) {

    return this.httpClient.get('https://localhost:8080/welcome/tasks/'.concat(processInstance)) as Observable<any>;
  }

  getUserTask(pid: string) {
    return this.httpClient.get('https://localhost:8080/welcome/tasks/users/'.concat(pid)) as Observable<any>;
  }

  claimTask(taskId) {
    return this.httpClient.post('https://localhost:8080/welcome/tasks/claim/'.concat(taskId), null) as Observable<any>;
  }

  completeTask(taskId) {
    return this.httpClient.post('https://localhost:8080/welcome/tasks/complete/'.concat(taskId), null) as Observable<any>;
  }

  scArea(pid) {
    return this.httpClient.get('https://localhost:8080/welcome/scAreas/start/'.concat(pid)) as Observable<any>;
  }

  getEditions() {
    return this.httpClient.get('https://localhost:8080/orders/shop') as Observable<any>;
  }

  getPapers(edition) {
    return this.httpClient.get('https://localhost:8080/orders/shop/papers/'.concat(edition)) as Observable<any>;
  }

}
