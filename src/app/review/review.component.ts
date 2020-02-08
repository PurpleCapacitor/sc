import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../services/review-process/review.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {

  private formFieldsDTO = null;
  private formFields = [];
  private magazines = [];
  private processInstanceId = '';

  constructor(private router: Router, private reviewService: ReviewService) {
    let data = reviewService.int();
    data.subscribe(res => {
      this.formFieldsDTO = res;
      this.formFields = res.formFields;
      this.processInstanceId = res.processInstanceId;
      localStorage.setItem("reviewPID", res.processInstanceId);
    });

    let mag = reviewService.getMagazines();
    mag.subscribe(res => {
      this.magazines = res;
    });

  }

  ngOnInit() {
  }

  onSubmit(values) {
    let select = new Array();
    for(let property in values) {
      select.push({fieldId : property, fieldValue : values[property]});
      localStorage.setItem("magazineName", values[property]);
    }
    let user = JSON.parse(localStorage.getItem("currentUser"));
    select.push({fieldId : 'username', fieldValue : user.username});
    let submit = this.reviewService.selectMag(this.formFieldsDTO.taskId, select);

    submit.subscribe(res => {
      console.log("ok");
      this.router.navigate(['/scPaper']);
    });
  }
}
