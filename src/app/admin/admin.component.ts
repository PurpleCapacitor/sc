import { Component, OnInit } from '@angular/core';
import {UserService} from '../services/users/user.service';
import {Router} from '@angular/router';
import {PassParamService} from '../services/pass-param.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  approveReviewer: any;

  private user = JSON.parse(localStorage.getItem("currentUser"));
  private pid = localStorage.getItem("registrationPID");
  private tasks = [];

  constructor(private userService: UserService,
              private router: Router, private passParamService: PassParamService) {
    let tasks = userService.getAdminTasks(this.pid, this.user.username);
    tasks.subscribe(res => {
      this.tasks = res;
    });
  }

  ngOnInit() {
  }

  proceed(taskId, taskName) {
    let ans = this.userService.getAdminTaskFields(taskId);
    ans.subscribe(res => {
      this.passParamService.passTaskId(taskId);
      if (taskName === "Approve reviewer") {
        this.router.navigate(['/approveReviewer']);
      }
    });
  }

}
