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
import { ScAreaComponent } from './registration/sc-area/sc-area.component';
import { MagazineLoginComponent } from './magazine/magazine-login/magazine-login.component';
import { RedirectTestComponent } from './redirect-test/redirect-test.component';
import { ReviewersEditorsComponent } from './magazine/reviewers-editors/reviewers-editors.component';
import { MagazineApproveComponent } from './magazine/magazine-approve/magazine-approve.component';
import { LoginUsersComponent } from './login/login-users/login-users.component';
import { ReviewComponent } from './review/review.component';
import { ScPaperComponent } from './review/sc-paper/sc-paper.component';
import {UploadFileService} from './services/upload-file.service';
import { AddCoauthorsComponent } from './review/sc-paper/add-coauthors/add-coauthors.component';
import {UsersComponent} from './users/users.component';
import { EditorComponent } from './editor/editor.component';
import { FormatReviewComponent } from './editor/format-review/format-review.component';
import { BasicReviewComponent } from './editor/basic-review/basic-review.component';
import { CommentComponent } from './editor/comment/comment.component';
import { AuthorComponent } from './author/author.component';
import { ReworkPaperComponent } from './author/rework-paper/rework-paper.component';
import { PickReviewersComponent } from './editor/pick-reviewers/pick-reviewers.component';
import { ReviewerComponent } from './reviewer/reviewer.component';
import { ReviewPaperComponent } from './reviewer/review-paper/review-paper.component';
import { PaperDecisionComponent } from './editor/paper-decision/paper-decision.component';
import { ReworkEndComponent } from './author/rework-end/rework-end.component';
import { PickAdditionalReviewersComponent } from './editor/pick-additional-reviewers/pick-additional-reviewers.component';
import { SearchComponent } from './search/search.component';
import { ReaderComponent } from './reader/reader.component';
import { BankComponent } from './bank/bank.component';
import { SuccessComponent } from './bank/success/success.component';
import { FailedComponent } from './bank/failed/failed.component';
import { ShopComponent } from './shop/shop.component';
import { EditionComponent } from './bank/success/edition/edition.component';
import { PayOpenAccessComponent } from './author/pay-open-access/pay-open-access.component';

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
  { path: 'magazines', component: MagazineLoginComponent },
  { path: 'scAreas', component: ScAreaComponent },
  { path: 'createMagazine', component: MagazineComponent },
  { path: 'redirectOk', component: RedirectTestComponent },
  { path: 'addReviewersEditors', component: ReviewersEditorsComponent },
  { path: 'approveMagazine', component: MagazineApproveComponent },
  { path: 'review', component: ReviewComponent },
  { path: 'scPaper', component: ScPaperComponent },
  { path: 'addCoAuthors', component: AddCoauthorsComponent },
  { path: 'editor', component: EditorComponent },
  { path: 'basicReview', component: BasicReviewComponent },
  { path: 'formatReview', component: FormatReviewComponent },
  { path: 'comment', component: CommentComponent },
  { path: 'author', component: AuthorComponent },
  { path: 'reworkPaper', component: ReworkPaperComponent },
  { path: 'pickReviewers', component: PickReviewersComponent },
  { path: 'reviewer', component: ReviewerComponent },
  { path: 'reviewPaper', component: ReviewPaperComponent },
  { path: 'paperDecision', component: PaperDecisionComponent },
  { path: 'reworkPaperAgain', component: ReworkEndComponent },
  { path: 'pickAdditionalReviewers', component: PickAdditionalReviewersComponent },
  { path: 'search', component: SearchComponent },
  { path: 'reader', component: ReaderComponent },
  { path: 'otpBank/:url/:paymentId', component: BankComponent },
  { path: 'success/:merchantOrderId', component: SuccessComponent },
  { path: 'failed/:merchantOrderId', component: FailedComponent },
  { path: 'shop', component: ShopComponent },
  { path: 'editions/:id', component: EditionComponent },
  { path: 'payOpenAccess', component: PayOpenAccessComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    AdminComponent,
    ApproveReviewerComponent,
    MagazineComponent,
    ScAreaComponent,
    MagazineLoginComponent,
    RedirectTestComponent,
    ReviewersEditorsComponent,
    MagazineApproveComponent,
    LoginUsersComponent,
    ReviewComponent,
    ScPaperComponent,
    AddCoauthorsComponent,
    UsersComponent,
    EditorComponent,
    FormatReviewComponent,
    BasicReviewComponent,
    CommentComponent,
    AuthorComponent,
    ReworkPaperComponent,
    PickReviewersComponent,
    ReviewerComponent,
    ReviewPaperComponent,
    PaperDecisionComponent,
    ReworkEndComponent,
    PickAdditionalReviewersComponent,
    SearchComponent,
    ReaderComponent,
    BankComponent,
    SuccessComponent,
    FailedComponent,
    ShopComponent,
    EditionComponent,
    PayOpenAccessComponent,
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
    Notauthorized,
    UploadFileService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
