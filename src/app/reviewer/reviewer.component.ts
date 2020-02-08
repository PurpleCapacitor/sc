import { Component, OnInit } from '@angular/core';
import {UserService} from '../services/users/user.service';
import {Router} from '@angular/router';
import {PassParamService} from '../services/pass-param.service';

@Component({
  selector: 'app-reviewer',
  templateUrl: './reviewer.component.html',
  styleUrls: ['./reviewer.component.css']
})
export class ReviewerComponent implements OnInit {

  private user = JSON.parse(localStorage.getItem("currentUser"));
  private pid = localStorage.getItem("reviewPID");
  private tasks = [];
  constructor(private userService: UserService,
              private router: Router, private passParamService: PassParamService) {
    let tasks = userService.getReviewerTasks(this.pid, this.user.username);
    tasks.subscribe(res => {
      this.tasks = res;
    });
  }

  ngOnInit() {
  }

  proceed(taskId, taskName) {
    let ans = this.userService.getReviewerTaskFields(taskId);
    ans.subscribe(res => {
      this.passParamService.passTaskId(taskId);
      if(taskName === "Reviewers reviewing") {
        this.router.navigate(['/reviewPaper']);
      }
    });
  }

}
