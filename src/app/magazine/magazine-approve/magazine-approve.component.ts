import { Component, OnInit } from '@angular/core';
import {MagazineService} from '../../services/magazines/magazine.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-magazine-approve',
  templateUrl: './magazine-approve.component.html',
  styleUrls: ['./magazine-approve.component.css']
})
export class MagazineApproveComponent implements OnInit {
  private formFieldsDTO = null;
  private formFields = [];
  private processInstanceId = localStorage.getItem("magazinePID");

  constructor(private magazineService: MagazineService, private router: Router) {
    let x = magazineService.getMagazineApprove(this.processInstanceId);
    x.subscribe(
      res => {
        this.formFieldsDTO = res;
        this.formFields = res.formFields;
      }
    );
  }

  ngOnInit() {
  }

  onSubmit(value) {
    let decision = new Array();
    for (let property in value) {
      decision.push({fieldId : property, fieldValue : value[property]});
    }

    let submit = this.magazineService.approveMagazine(decision, this.formFieldsDTO.taskId);

    submit.subscribe(
      res => {
        console.log("Finished");
      });

  }
}
