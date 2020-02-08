import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicReviewComponent } from './basic-review.component';

describe('BasicReviewComponent', () => {
  let component: BasicReviewComponent;
  let fixture: ComponentFixture<BasicReviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BasicReviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
