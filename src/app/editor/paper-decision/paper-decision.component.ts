import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-paper-decision',
  templateUrl: './paper-decision.component.html',
  styleUrls: ['./paper-decision.component.css']
})
export class PaperDecisionComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private taskId = '';
  private enumValues = [];
  private reviews = [];

  constructor(private router: Router,
              private reviewService: ReviewService,
              private passParam: PassParamService,
              private userService: UserService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let taskInfo = this.userService.getEditorTaskFields(this.taskId);
    taskInfo.subscribe(res => {
      this.formFieldsDto = res;
      this.formFields = res.formFields;
      this.formFields.forEach( (field) => {
        if (field.type.name === 'enum') {
          this.enumValues = Object.keys(field.type.values);
        }
      });
    });

    let reviews = this.reviewService.getReviews(this.taskId);
    reviews.subscribe(res => {
      this.reviews = res;
    });
  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let decision = new Array();
    for(let value in values) {
      decision.push({fieldId : value, fieldValue : values[value]});
    }

    let submit = this.reviewService.decision(decision, this.taskId);
    submit.subscribe(() => {
      this.router.navigate(['/editor']).then( () => {
        window.location.reload();
      });
    });

  }
}
