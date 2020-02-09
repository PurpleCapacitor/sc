import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../services/users/user.service';
import {PassParamService} from '../services/pass-param.service';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements OnInit {
  private user = JSON.parse(localStorage.getItem("currentUser"));
  private pid = localStorage.getItem("reviewPID");
  private tasks = [];

  constructor(private userService: UserService,
              private router: Router, private passParamService: PassParamService) {
    let tasks = userService.getEditorTasks(this.pid, this.user.username);
    tasks.subscribe(res => {
      this.tasks = res;
    });
  }

  ngOnInit() {
  }

  proceed(taskId, taskName) {
    let ans = this.userService.getEditorTaskFields(taskId);
    ans.subscribe(res => {
      this.passParamService.passTaskId(taskId);
      if(taskName === "Basic review by main editor") {
        this.router.navigate(['/basicReview']);
      }
      if(taskName === "Reviewing paper formatting") {
        this.router.navigate(['/formatReview']);
      }
      if(taskName === "Add comment") {
        this.router.navigate(['/comment']);
      }
      if(taskName === "Pick reviewers") {
        this.router.navigate(['/pickReviewers']);
      }
      if(taskName === "Assigned editor making a decision about paper") {
        this.router.navigate(['/paperDecision']);
      }
      if(taskName === "Pick additional reviewers") {
        this.router.navigate(['/pickAdditionalReviewers']);
      }

    });
  }
}
