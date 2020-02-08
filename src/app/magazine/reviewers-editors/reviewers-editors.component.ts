import { Component, OnInit } from '@angular/core';
import {MagazineService} from '../../services/magazines/magazine.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-reviewers-editors',
  templateUrl: './reviewers-editors.component.html',
  styleUrls: ['./reviewers-editors.component.css']
})
export class ReviewersEditorsComponent implements OnInit {

  private formFieldsDTO = null;
  private formFields = [];
  private processInstanceId = localStorage.getItem("magazinePID");
  private magazineName = localStorage.getItem("magazineName");

  constructor(private magazineService: MagazineService, private router: Router) {
    let data = magazineService.getPersonnelFields(this.processInstanceId);

    data.subscribe(
      res => {
      this.formFieldsDTO = res;
      this.formFields = res.formFields;
    });
  }

  ngOnInit() {
  }

  onSubmit(values) {
    let submit = new Array();
    for (let property in values) {
      submit.push({fieldId : property, fieldValue : values[property]});
    }
    console.log(submit);
    let data = this.magazineService.addMagazinePersonnel(submit, this.formFieldsDTO.taskId, this.magazineName);
    data.subscribe(
      res => {
        console.log("Personnel added");
        this.router.navigate(['/approveMagazine']);
      }, error => {
        console.log("Error");
      }
    );
  }
}
