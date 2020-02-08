import { Component, OnInit } from '@angular/core';
import {MagazineService} from '../../services/magazines/magazine.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-magazine-login',
  templateUrl: './magazine-login.component.html',
  styleUrls: ['./magazine-login.component.css']
})
export class MagazineLoginComponent implements OnInit {

  private formFieldsDTO = null;
  private formFields = [];
  private processInstanceId = '';


  constructor(private magazineService: MagazineService, private router: Router) {
    let x = magazineService.startProcess();
    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDTO = res;
        this.formFields = res.formFields;
        this.processInstanceId = res.processInstanceId;
        localStorage.setItem("magazinePID", this.processInstanceId);
      }
    );
  }

  ngOnInit() {
  }

  onSubmit(value) {
    let userLogin = new Array();
    for (let property in value) {
      userLogin.push({fieldId: property, fieldValue: value[property]});
    }
    let submit = this.magazineService.login(userLogin, this.formFieldsDTO.taskId);
    submit.subscribe(
      res => {
        console.log("Login successful");
        this.router.navigate(['/createMagazine']);
      }, error => {
        console.log("Login failed");
      }
    );
  }

}
