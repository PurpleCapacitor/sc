import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = localStorage.getItem("reviewPID");

  constructor(private router: Router, private reviewService: ReviewService) {
    let formData = this.reviewService.getFields(this.processInstance);
    formData.subscribe(res => {
      this.formFieldsDto = res;
      this.formFields = res.formFields;
    });
  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let comment = new Array();
    for (let value in values) {
      comment.push({fieldId: value, fieldValue: values[value]});
    }

    let submit = this.reviewService.addComment(comment, this.formFieldsDto.taskId);
    submit.subscribe(res => {
      console.log("Comment added");
      this.router.navigate(['/editor']);
    });
  }
}
