import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormatReviewComponent } from './format-review.component';

describe('FormatReviewComponent', () => {
  let component: FormatReviewComponent;
  let fixture: ComponentFixture<FormatReviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormatReviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormatReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
