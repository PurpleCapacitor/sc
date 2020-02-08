import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-basic-review',
  templateUrl: './basic-review.component.html',
  styleUrls: ['./basic-review.component.css']
})
export class BasicReviewComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = localStorage.getItem("reviewPID");
  private paperInfo = [];
  private taskId = '';

  constructor(private router: Router,
              private reviewService: ReviewService,
              private passParam: PassParamService,
              private userService: UserService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let paperInfo = reviewService.getPaperInfo(this.processInstance);
    paperInfo.subscribe(nes => {
      let obj = nes;
      this.paperInfo = Object.keys(obj).map(key => ({type: key, value: obj[key]}));
    });

    let taskInfo = this.userService.getEditorTaskFields(this.taskId);
    taskInfo.subscribe(res => {
      this.formFieldsDto = res;
      this.formFields = res.formFields;
    });

  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let decision = new Array();
    for (let value in values) {
      decision.push({fieldId: value, fieldValue: values[value]});
    }
    let submit = this.reviewService.basicReview(decision, this.taskId);
    submit.subscribe(res => {
      console.log("Basic review done.");
      this.router.navigate(['/formatReview']);
    });



  }

}
