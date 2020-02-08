import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../../services/review-process/review.service';

@Component({
  selector: 'app-add-coauthors',
  templateUrl: './add-coauthors.component.html',
  styleUrls: ['./add-coauthors.component.css']
})
export class AddCoauthorsComponent implements OnInit {

  private formFieldsDTO = null;
  private formFields = [];
  private repetition = JSON.parse(localStorage.getItem("noCoAuthors"));
  private counter = JSON.parse(localStorage.getItem("coAuthorCounter"));

  constructor(private router: Router, private reviewService: ReviewService) {
    let data = reviewService.getFields(localStorage.getItem("reviewPID"));
    data.subscribe(res => {
      this.formFieldsDTO = res;
      this.formFields = res.formFields;
    });
  }

  ngOnInit() {
  }

  onSubmit(values: any) {
    let array = new Array();
    for (let value in values) {
      array.push({fieldId: value, fieldValue: values[value]});
    }

    let submit = this.reviewService.addAuthor(this.formFieldsDTO.taskId, array);
    submit.subscribe(res => {
      console.log("coAuthor added");
    });

    if(this.counter === this.repetition) {
      alert("Successfully added all coauthors");
      this.router.navigate(['/author']).then( () => {
        window.location.reload();
      });
    } else {
      this.counter++;
      localStorage.setItem("coAuthorCounter", this.counter);
      window.location.reload();
    }



  }
}
