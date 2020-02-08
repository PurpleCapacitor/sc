import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-review-paper',
  templateUrl: './review-paper.component.html',
  styleUrls: ['./review-paper.component.css']
})
export class ReviewPaperComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private taskId = '';
  private enumValues = [];
  private fileName = '';
  private user = JSON.parse(localStorage.getItem("currentUser"));
  private processInstance = localStorage.getItem("reviewPID");

  constructor(private router: Router,
              private reviewService: ReviewService,
              private passParam: PassParamService,
              private userService: UserService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let filePath = this.reviewService.getPaperFile(this.processInstance);
    filePath.subscribe(res => {
      this.fileName = res["file"];
    });

    let taskInfo = this.userService.getReviewerTaskFields(this.taskId);
    taskInfo.subscribe(res => {
      this.formFieldsDto = res;
      this.formFields = res.formFields;
      this.formFields.forEach( (field) => {
        if (field.type.name === 'enum') {
          this.enumValues = Object.keys(field.type.values);
        }
      });
    });

  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let review = [];

    for(let value in values) {
      review.push({fieldId : value, fieldValue : values[value]});
    }

    review.push({fieldId: "reviewerUsername", fieldValue: this.user.username});

    let submit = this.reviewService.submitReview(review, this.taskId);
    submit.subscribe(() => {
      console.log("Review submitted");
      this.router.navigate(['/reviewer']).then( () => {
          window.location.reload();
      });
    });
  }
}
