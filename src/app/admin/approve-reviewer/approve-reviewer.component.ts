import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../services/repository/repository.service';
import {UserService} from '../../services/users/user.service';

@Component({
  selector: 'app-approve-reviewer',
  templateUrl: './approve-reviewer.component.html',
  styleUrls: ['./approve-reviewer.component.css']
})
export class ApproveReviewerComponent implements OnInit {

  private formFieldsDTO = null;
  private formFields = [];
  private processInstanceId = localStorage.getItem("registrationPID");

  constructor(private repositoryService: RepositoryService, private userService: UserService) {
    let x = repositoryService.getUserTask(this.processInstanceId);
    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDTO = res;
        this.formFields = res.formFields;
      }
    );
  }


  ngOnInit() {
  }

  onSubmit(value, form) {
    let array = new Array();
    for (let property in value) {
      array.push({fieldId : property, fieldValue : value[property]});
    }


    let x = this.userService.approveReviewer(array, this.formFieldsDTO.taskId, this.processInstanceId);

    x.subscribe(
      res => {
        console.log(res);

        alert("You approved successfully!");
      },
      err => {
        console.log("Error occured");
      }
    );
  }

}
