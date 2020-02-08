import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }

  int() {
    return this.http.get('http://localhost:8080/reviews/initProcesses/') as Observable<any>;
  }

  getMagazines() {
    return this.http.get('http://localhost:8080/reviews/initProcesses/magazines') as Observable<any>;
  }

  selectMag(taskId, data) {
    return this.http.post('http://localhost:8080/reviews/magazineChoices/'.concat(taskId), data) as Observable<any>;
  }

  /*getPaperFields(pid) {
    return this.http.get('http://localhost:8080/reviews/scPaperFields/'.concat(pid)) as Observable<any>;
  }*/

  addPaper(taskId, data) {
    return this.http.post('http://localhost:8080/reviews/papers/'.concat(taskId), data) as Observable<any>;
  }

  getFields(pid) {
    return this.http.get('http://localhost:8080/reviews/'.concat(pid)) as Observable<any>;
  }

  addAuthor(taskId, data) {
    return this.http.post('http://localhost:8080/reviews/coauthors/'.concat(taskId), data) as Observable<any>;
  }

  getPaperInfo(pid) {
    return this.http.get('http://localhost:8080/reviews/papers/details/'.concat(pid)) as Observable<any>;
  }

  basicReview(data, taskId) {
    return this.http.post('http://localhost:8080/reviews/papers/decisions/'.concat(taskId), data) as Observable<any>;
  }

  getPaperFile(pid) {
    return this.http.get('http://localhost:8080/reviews/papers/details/files/'.concat(pid)) as Observable<any>;
  }

  formatReview(data, taskId) {
    return this.http.post('http://localhost:8080/reviews/papers/formats/'.concat(taskId), data) as Observable<any>;
  }

  addComment(comment, taskId) {
    return this.http.post('http://localhost:8080/reviews/papers/comments/'.concat(taskId), comment) as Observable<any>;
  }

  getComment(pid) {
    return this.http.get('http://localhost:8080/reviews/papers/comments/'.concat(pid)) as Observable<any>;
  }

  fileRevise(data, taskId, pid) {
    return this.http.post('http://localhost:8080/reviews/papers/resubmits/'+ pid +'/'.concat(taskId), data) as Observable<any>;
  }

  getReviewers(pid: string) {
    return this.http.get('http://localhost:8080/reviews/reviewers/'.concat(pid)) as Observable<any>;
  }

  addReviewers(taskId, pid, data) {
    return this.http.post('http://localhost:8080/reviews/reviewers/' + pid + '/'.concat(taskId), data) as Observable<any>;
  }

  submitReview(data, taskId) {
    return this.http.post('http://localhost:8080/reviews/reviewers/decisions/'.concat(taskId), data) as Observable<any>;
  }

  decision(decision: any[], taskId: string) {
    return this.http.post('http://localhost:8080/reviews/editors/decisions/'.concat(taskId), decision) as Observable<any>;
  }

  getReviews(taskId: string) {
    return this.http.get('http://localhost:8080/reviews/finished/'.concat(taskId)) as Observable<any>;
  }

  getAdditionalReviewers(processInstance: string) {
    return this.http.get('http://localhost:8080/reviews/reviewers/reelections/'.concat(processInstance)) as Observable<any>;
  }

  getUnfinishedReviewsNumber(processInstance: string) {
    return this.http.get('http://localhost:8080/reviews/unfinishedReviews/'.concat(processInstance)) as Observable<any>;
  }

  readdReviewers(taskId: string, pid: string, data: any[]) {
    return this.http.post('http://localhost:8080/reviews/reviewers/reelections/' + pid + '/'.concat(taskId), data) as Observable<any>;
  }
}
