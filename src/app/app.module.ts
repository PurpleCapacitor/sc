import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';

import { RepositoryService } from './services/repository/repository.service';
import { UserService } from './services/users/user.service';

import { RegistrationComponent } from './registration/registration.component';

import {Authorized} from './guard/authorized.guard';
import {Admin} from './guard/admin.guard';
import {Notauthorized} from './guard/notauthorized.guard';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ApproveReviewerComponent } from './admin/approve-reviewer/approve-reviewer.component';
import { MagazineComponent } from './magazine/magazine.component';

const ChildRoutes =
  [
  ];

  const RepositoryChildRoutes =
  [

  ];

const Routes = [
  { path: "registrate", component: RegistrationComponent, canActivate: [Notauthorized] },
  { path: 'login', component: LoginComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'approveReviewer', component: ApproveReviewerComponent },
  { path: 'magazines', component: MagazineComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    AdminComponent,
    ApproveReviewerComponent,
    MagazineComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot(Routes),
    HttpClientModule,
    HttpModule,
    ReactiveFormsModule
  ],

  providers:  [
    Admin,
    Authorized,
    Notauthorized
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
