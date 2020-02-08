import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/users/user.service';
import {RepositoryService} from '../../services/repository/repository.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-sc-area',
  templateUrl: './sc-area.component.html',
  styleUrls: ['./sc-area.component.css']
})
export class ScAreaComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private processInstance = '';
  private tasks = [];
  private pid = localStorage.getItem("registrationPID");

  constructor(private userService: UserService, private repositoryService: RepositoryService, private router: Router) {
    let x = repositoryService.scArea(this.pid);

    x.subscribe(
      res => {
        this.formFields = res.formFields;
        this.formFieldsDto = res;
      }, err => {
        console.log('Error occurred');
      });
  }

  ngOnInit() {
  }

  onSubmit(value: any) {
    let array = new Array();
    for(let v in value) {
      array.push({fieldId: v, fieldValue: value[v]});
    }

    let submit = this.userService.registerScAreas(array, this.formFieldsDto.taskId);

    submit.subscribe(res => {
      console.log("Areas succeeded");
      this.router.navigate(['/']);
    }, err => {
      console.log("ScAreas error");
    });
  }
}
