import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';

@Component({
  selector: 'app-pick-additional-reviewers',
  templateUrl: './pick-additional-reviewers.component.html',
  styleUrls: ['./pick-additional-reviewers.component.css']
})
export class PickAdditionalReviewersComponent implements OnInit {

  private processInstance = localStorage.getItem("reviewPID");
  private taskId = '';
  private reviewers = [];
  private number;

  constructor(private router: Router, private reviewService: ReviewService, private passParam: PassParamService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let reviewersList = this.reviewService.getAdditionalReviewers(this.processInstance);
    reviewersList.subscribe(res => {
      this.reviewers = res;
    });

    let n = this.reviewService.getUnfinishedReviewsNumber(this.processInstance);
    n.subscribe( res => {
      this.number = res["numberOfUnfinishedReviews"];
    });
  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let chosenReviewers = [];
    for(let value in values) {
      if(values[value] !== "") {
        chosenReviewers.push({username : value});
      }
    }

    let submit = this.reviewService.readdReviewers(this.taskId, this.processInstance, chosenReviewers);
    submit.subscribe(() => {
      console.log("Reviewers readded");
      this.router.navigate(['/editor']).then( () => {
        window.location.reload();
      });
    });

  }

}
