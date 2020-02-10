import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../services/repository/repository.service';
import {UserService} from '../../services/users/user.service';
import {PassParamService} from '../../services/pass-param.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-approve-reviewer',
  templateUrl: './approve-reviewer.component.html',
  styleUrls: ['./approve-reviewer.component.css']
})
export class ApproveReviewerComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstanceId = localStorage.getItem("registrationPID");
  private taskId = '';

  constructor(private repositoryService: RepositoryService,
              private userService: UserService,
              private passParam: PassParamService,
              private router: Router) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let taskInfo = this.userService.getAdminTaskFields(this.taskId);
    taskInfo.subscribe(res => {
      this.formFieldsDto = res;
      this.formFields = res.formFields;
    });
  }


  ngOnInit() {
  }

  onSubmit(value) {
    let array = new Array();
    for (let property in value) {
      array.push({fieldId : property, fieldValue : value[property]});
    }


    let x = this.userService.approveReviewer(array, this.taskId, this.processInstanceId);

    x.subscribe(
      res => {
        console.log(res);
        alert("You approved successfully!");
        this.router.navigate(['/admin']);
      },
      err => {
        console.log("Error occured");
      }
    );
  }

}
