import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ReviewService} from '../../services/review-process/review.service';
import {MagazineService} from '../../services/magazines/magazine.service';
import {UploadFileService} from '../../services/upload-file.service';
import {HttpEventType} from '@angular/common/http';

@Component({
  selector: 'app-sc-paper',
  templateUrl: './sc-paper.component.html',
  styleUrls: ['./sc-paper.component.css']
})
export class ScPaperComponent implements OnInit {

  private magName = localStorage.getItem("magazineName");
  private formFieldsDTO = null;
  private formFields = [];
  private enumValues = [];
  selectedFiles: FileList;
  currentFileUpload: File;


  constructor(private router: Router, private reviewService: ReviewService,
              private magazineService: MagazineService,
              private uploadService: UploadFileService) {
    let data = reviewService.getFields(localStorage.getItem("reviewPID"));
    data.subscribe(res => {
      this.formFieldsDTO = res;
      this.formFields = res.formFields;
    });
    let magazineScArea = magazineService.getScAreas(localStorage.getItem("magazineName"));
    magazineScArea.subscribe(res => {
      this.enumValues = res;
    });

  }

  ngOnInit() {
  }

  onSubmit(values: any) {

    let author = JSON.parse(localStorage.getItem("currentUser"));
    let magazine = localStorage.getItem("magazineName");

    let submit = new Array();
    for (let value in values) {
      if(value === "noCoAuthors") {
        localStorage.setItem("noCoAuthors", values[value]);
        localStorage.setItem("coAuthorCounter", "1");
      }
      submit.push({fieldId : value, fieldValue : values[value]});
    }

    this.currentFileUpload = this.selectedFiles.item(0);
    submit.push({fieldId: "file", fieldValue: this.currentFileUpload.name});
    submit.push({fieldId: "author", fieldValue: author.username});
    submit.push({fieldId: "magazine", fieldValue: magazine});
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      console.log('File is completely uploaded!');
    });

    let pushData = this.reviewService.addPaper(this.formFieldsDTO.taskId, submit);

    pushData.subscribe(res => {
      console.log("Paper submitted");
      this.router.navigate(['/addCoAuthors']);
    });

    this.selectedFiles = undefined;
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

}
