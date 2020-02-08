import { Component, OnInit } from '@angular/core';
import {User} from "../user";
import {FormControl, FormGroup} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {UserService} from '../services/users/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public user: User;
  private result: User;

  credentials = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  constructor(private httpClient: HttpClient, private router: Router, private userService: UserService) {}

  ngOnInit() {
  }

  onSubmit() {
    this.user = new User();
    this.user.username = this.credentials.get('username').value;
    this.user.password = this.credentials.get('password').value;
    console.log(this.user.username + ' ' + this.user.password);
    let x = this.httpClient.post("http://localhost:8080/users/login", this.user, {responseType: 'text'});
    x.subscribe(
      res => {
        this.user = JSON.parse(res);
        localStorage.setItem("currentUser", JSON.stringify(this.user));
        localStorage.setItem("currentRole", JSON.stringify(this.user.role));
        if(this.user.role === 'admin') {
          this.router.navigate(['/admin']);
        }
        if(this.user.role === 'editor') {
          this.router.navigate(['/editor']).then(
            () => {
              window.location.reload();
            }
          );
        }
        if(this.user.role === 'reviewer') {
          this.router.navigate(['/reviewer']).then(
            () => {
                window.location.reload();
            }
          );
        }
        if(this.user.role === 'author') {
          this.router.navigate(['/author'])
            .then( () => {
            window.location.reload();
          });

        }

      }
    );
  }
}
