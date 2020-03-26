import { Component, OnInit } from '@angular/core';
import {UsersComponent} from '../users/users.component';
import {UserService} from '../services/users/user.service';

@Component({
  selector: 'app-reader',
  templateUrl: './reader.component.html',
  styleUrls: ['./reader.component.css']
})
export class ReaderComponent implements OnInit {

  private user = JSON.parse(localStorage.getItem("currentUser"));
  private orders = [];

  constructor(private userService: UserService) {
    let orders = this.userService.getCompletedOrders(this.user.username);
    orders.subscribe(res => {
      this.orders = res;
    });
  }

  ngOnInit() {
  }

}
