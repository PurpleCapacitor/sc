import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';

@Component({
  selector: 'app-pick-reviewers',
  templateUrl: './pick-reviewers.component.html',
  styleUrls: ['./pick-reviewers.component.css']
})
export class PickReviewersComponent implements OnInit {

  private processInstance = localStorage.getItem("reviewPID");
  private taskId = '';
  private reviewers = [];

  constructor(private router: Router, private reviewService: ReviewService, private passParam: PassParamService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let reviewersList = this.reviewService.getReviewers(this.processInstance);
    reviewersList.subscribe(res => {
      this.reviewers = res;
    });
  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let chosenReviewers = new Array();
    for(let value in values) {
      if(values[value] !== "") {
        chosenReviewers.push({username : value});
      }
    }

    let submit = this.reviewService.addReviewers(this.taskId, this.processInstance, chosenReviewers);
    submit.subscribe(res => {
      console.log("Reviewers added");
    this.router.navigate(['/editor']).then( () => {
        window.location.reload();
      });
    });

  }
}
