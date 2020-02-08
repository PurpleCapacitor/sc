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

  private repeated_password = '';
  private categories = [];
  private formFieldsDto = null;
  private formFields = [];
  private choosen_category = -1;
  private processInstance = '';
  private enumValues = [];
  private tasks = [];

  constructor(private userService: UserService, private repositoryService: RepositoryService,
              private router: Router) {

    let x = repositoryService.startProcess();
    // let y = repositoryService.scArea();

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

   /* y.subscribe(
      res => {
        this.scAreas = res;
      },
      err => {
        console.log('Error in scAreas occurred');
      }
    );*/
  }


  ngOnInit() {
  }

  onSubmit(value) {
    let o = new Array();

    for (let property in value) {
      o.push({fieldId: property, fieldValue: value[property]});
    }

    console.log(o);


    // let y = this.userService.registerScAreas(scAreasList, this.formFieldsDto.taskId);
    let x = this.userService.registerUser(o, this.formFieldsDto.taskId); // vrednosti usera

   /* y.subscribe(
      res => {
        console.log(res);
      }
    );*/

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

  getTasks() {
    let x = this.repositoryService.getTasks(this.processInstance);

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
      },
      err => {
        console.log('Error occurred');
      }
    );
  }

  claim(taskId) {
    let x = this.repositoryService.claimTask(taskId);

    x.subscribe(
      res => {
        console.log(res);
      },
      err => {
        console.log('Error occured');
      }
    );
  }

  complete(taskId) {
    let x = this.repositoryService.completeTask(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
      },
      err => {
        console.log('Error occured');
      }
    );
  }

}
