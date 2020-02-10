import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/users/user.service';
import {RepositoryService} from '../services/repository/repository.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = '';
  private enumValues = [];
  private tasks = [];

  constructor(private userService: UserService, private repositoryService: RepositoryService,
              private router: Router) {

    let x = repositoryService.startProcess();

    x.subscribe(
      res => {
        console.log(res);
        // this.categories = res;
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        localStorage.setItem("registrationPID", res.processInstanceId);
        this.formFields.forEach((field) => {

          if (field.type.name === 'enum') {
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        console.log('Error occurred');
      }
    );
  }


  ngOnInit() {
  }

  onSubmit(value) {
    let o = new Array();

    for (let property in value) {
      o.push({fieldId: property, fieldValue: value[property]});
    }
    console.log(o);
    let x = this.userService.registerUser(o, this.formFieldsDto.taskId); // vrednosti usera

    x.subscribe(
      res => {
        console.log(res);
        this.router.navigate(['/scAreas']);
      },
      err => {
        console.log('Error occurred');
      }
    );


  }

}
