import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  constructor(private http: HttpClient) { }

  startProcess() {
    return this.http.get("http://localhost:8080/magazines/initializeTask") as Observable<any>;
  }

  login(userLogin, taskId) {
    return this.http.post('http://localhost:8080/magazines/login/'.concat(taskId), userLogin) as Observable<any>;
  }

  getMagazineFields(pid) {
    return this.http.get('http://localhost:8080/magazines/fields/'.concat(pid)) as Observable<any>;
  }

  newMagazine(data, taskId) {
    return this.http.post('http://localhost:8080/magazines/'.concat(taskId), data) as Observable<any>;
  }

  getPersonnelFields(pid) {
    return this.http.get('http://localhost:8080/magazines/fields/personnel/'.concat(pid)) as Observable<any>;
  }

  addMagazinePersonnel(data, taskId, magName) {
    return this.http.post('http://localhost:8080/magazines/submitPersonnel/' + magName + '/'.concat(taskId), data) as Observable<any>;
  }

  getMagazineApprove(pid: string) {
    return this.http.get('http://localhost:8080/magazines/approve/'.concat(pid)) as Observable<any>;
  }

  approveMagazine(decision, taskId) {
    return this.http.post('http://localhost:8080/magazines/decisions/'.concat(taskId), decision) as Observable<any>;
  }

  getScAreas(magazineName) {
    return this.http.get("http://localhost:8080/magazines/scAreas/".concat(magazineName)) as Observable<any>;
  }
}
