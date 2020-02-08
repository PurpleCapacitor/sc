import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PassParamService {

  private taskIdSource = new BehaviorSubject<string>('');
  currentTaskId = this.taskIdSource.asObservable();

  constructor() { }

  passTaskId(taskId: string) {
    this.taskIdSource.next(taskId);
  }
}
