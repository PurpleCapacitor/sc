import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReworkPaperComponent } from './rework-paper.component';

describe('ReworkPaperComponent', () => {
  let component: ReworkPaperComponent;
  let fixture: ComponentFixture<ReworkPaperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReworkPaperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReworkPaperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
