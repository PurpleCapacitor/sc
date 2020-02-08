import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewersEditorsComponent } from './reviewers-editors.component';

describe('ReviewersEditorsComponent', () => {
  let component: ReviewersEditorsComponent;
  let fixture: ComponentFixture<ReviewersEditorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewersEditorsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewersEditorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
