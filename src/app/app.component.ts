import {Component} from '@angular/core';
import {User} from './user';
import {UserService} from './services/users/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  private user = JSON.parse(localStorage.getItem("currentUser"));
  private role = JSON.parse(localStorage.getItem("currentRole"));

  constructor(private router: Router) {

  }


  loggedIn() {
    if (this.user) {
      return true;
    } else {
      return false;
    }
  }

  notLoggedIn() {
    if (!this.user) {
      return true;
    } else {
      return false;
    }
  }

  logout() {
    localStorage.removeItem("currentUser");
    this.router.navigate(['/']).then( () => {
      window.location.reload();
    });
  }

  isAdmin() {
    if (this.role === 'admin') {
      return true;
    } else {
      return false;
    }
  }
}
