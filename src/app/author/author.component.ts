import { Component, OnInit } from '@angular/core';
import {UserService} from '../services/users/user.service';
import {Router} from '@angular/router';
import {PassParamService} from '../services/pass-param.service';

@Component({
  selector: 'app-author',
  templateUrl: './author.component.html',
  styleUrls: ['./author.component.css']
})
export class AuthorComponent implements OnInit {

  private user = JSON.parse(localStorage.getItem("currentUser"));
  private role = JSON.parse(localStorage.getItem("currentRole"));
  private pid = localStorage.getItem("reviewPID");
  private tasks = [];

  constructor(private userService: UserService,
              private router: Router, private passParamService: PassParamService) {
    let tasks = userService.getAuthorTasks(this.pid, this.user.username);
    tasks.subscribe(res => {
      this.tasks = res;
    });
  }

  ngOnInit() {
  }

  proceed(taskId, taskName) {
    let ans = this.userService.getAuthorTaskFields(taskId);
    ans.subscribe(res => {
      this.passParamService.passTaskId(taskId);
      if(taskName === "Rework paper") {
        this.router.navigate(['/reworkPaper']);
      }
      if(taskName === "Rework paper again") {
        this.router.navigate(['/reworkPaperAgain']);
      }

    });
  }

  IsAuthor() {
    if (this.role === 'author') {
      return true;
    } else {
      return false;
    }
  }
}
