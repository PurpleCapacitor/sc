import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-format-review',
  templateUrl: './format-review.component.html',
  styleUrls: ['./format-review.component.css']
})
export class FormatReviewComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = localStorage.getItem("reviewPID");
  private taskId = '';
  private fileName = '';

  constructor(private router: Router,  private passParam: PassParamService, private reviewService: ReviewService) {

    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let filePath = this.reviewService.getPaperFile(this.processInstance);
    filePath.subscribe(res => {
      this.fileName = res["file"];
    });

    let formData = this.reviewService.getFields(this.processInstance);
    formData.subscribe(res => {
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

    let submit = this.reviewService.formatReview(decision, this.taskId);
    submit.subscribe(res => {
      console.log("Format review done.");
      this.router.navigate(['/editor']);
    });
  }
}
