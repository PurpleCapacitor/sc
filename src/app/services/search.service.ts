import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SimpleQuery} from '../simple-query';
import {AdvancedQuery} from '../advanced-query';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(
    private http: HttpClient
  ) { }


  searchSimple(query: SimpleQuery): Observable<any[]> {
    return this.http.post<any[]>("https://localhost:8080/search", query);
  }

  searchBoolean(query: AdvancedQuery): Observable<any[]> {
    return this.http.post<any[]>("https://localhost:8080/search/boolean", query);
  }
}
