import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../../services/repository/repository.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-edition',
  templateUrl: './edition.component.html',
  styleUrls: ['./edition.component.css']
})
export class EditionComponent implements OnInit {

  private papers = [];

  constructor(private repositoryService: RepositoryService,
              private activatedRoute: ActivatedRoute) {
    let papers = this.repositoryService.getPapers(this.activatedRoute.snapshot.paramMap.get("id"));
    papers.subscribe(res => {
      this.papers = res;
    });
  }

  ngOnInit() {
  }

  download(file: any) {
    window.location.href = "https://localhost:8080/download/".concat(file);
  }
}
