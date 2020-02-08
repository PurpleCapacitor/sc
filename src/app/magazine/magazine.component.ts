import {Component, OnInit} from '@angular/core';
import {RepositoryService} from '../services/repository/repository.service';
import {UserService} from '../services/users/user.service';
import {MagazineService} from '../services/magazines/magazine.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-magazine',
  templateUrl: './magazine.component.html',
  styleUrls: ['./magazine.component.css']
})
export class MagazineComponent implements OnInit {

  private formFieldsDTO = null;
  private formFields = [];
  private enumValues = [];
  private enumValues2 = [];
  private processInstanceId = localStorage.getItem("magazinePID");

  ngOnInit(): void {
  }


  constructor(private magazineService: MagazineService, private router: Router) {
    let x = magazineService.getMagazineFields(this.processInstanceId);
    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDTO = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) => {
          if (field.type.name === 'enum') {
            if("math" in field.type.values) {
              this.enumValues = Object.keys(field.type.values);
            } else if("openAccess" in field.type.values) {
              this.enumValues2 = Object.keys(field.type.values);
            }

          }
        });
      }
    );
  }

  onSubmit(value: any) {
    let magazineInfo = new Array();
    for (let property in value) {
      if(property === "name") {
        localStorage.setItem("magazineName", value[property]);
      }
      magazineInfo.push({fieldId : property, fieldValue : value[property]});
    }

    let submit = this.magazineService.newMagazine(magazineInfo, this.formFieldsDTO.taskId);

    submit.subscribe( res => {
      console.log("Magazine created");
      this.router.navigate(['/addReviewersEditors']);
      }, error => {
      console.log("Magazine creation failed");
      }
    );

  }

}
