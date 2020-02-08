import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {PassParamService} from '../../services/pass-param.service';
import {UploadFileService} from '../../services/upload-file.service';

@Component({
  selector: 'app-rework-end',
  templateUrl: './rework-end.component.html',
  styleUrls: ['./rework-end.component.css']
})
export class ReworkEndComponent implements OnInit {

  private processInstance = localStorage.getItem("reviewPID");
  private taskId = '';
  private comment = '';
  selectedFiles: FileList;
  currentFileUpload: File;
  private reviews = [];

  constructor(private router: Router,
              private reviewService: ReviewService,
              private passParam: PassParamService,
              private uploadService: UploadFileService) {
    this.passParam.currentTaskId.subscribe(current => this.taskId = current);


    let reviews = this.reviewService.getReviews(this.taskId);
    reviews.subscribe(res => {
      this.reviews = res;
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
    submit.subscribe(() => {
      console.log("File resubmitted");
    });
    this.router.navigate(['/author']).then( () => {
      window.location.reload();
    });
  }

}
