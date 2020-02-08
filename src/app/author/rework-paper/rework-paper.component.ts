import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';
import {UserService} from '../../services/users/user.service';
import {UploadFileService} from '../../services/upload-file.service';

@Component({
  selector: 'app-rework-paper',
  templateUrl: './rework-paper.component.html',
  styleUrls: ['./rework-paper.component.css']
})
export class ReworkPaperComponent implements OnInit {

  private processInstance = localStorage.getItem("reviewPID");
  private taskId = '';
  private comment = '';
  selectedFiles: FileList;
  currentFileUpload: File;

  constructor(private router: Router,
              private reviewService: ReviewService,
              private passParam: PassParamService,
              private uploadService: UploadFileService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);

    let text = reviewService.getComment(this.processInstance);
    text.subscribe(res => {
      let obj = res[0];
      this.comment = obj['fieldValue'];
    });

  }

  ngOnInit() {
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    this.currentFileUpload = this.selectedFiles.item(0);
    let fileInfo = ({fieldId: "file", fieldValue: this.currentFileUpload.name});
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(ev => {
      console.log('File is completely uploaded!');
    });
    this.selectedFiles = undefined;
    let submit = this.reviewService.fileRevise(fileInfo, this.taskId, this.processInstance);
    submit.subscribe(res => {
      console.log("File resubmitted");
    });
    this.router.navigate(['/author']).then( () => {
      window.location.reload();
    });
  }

}
