import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PickReviewersComponent } from './pick-reviewers.component';

describe('PickReviewersComponent', () => {
  let component: PickReviewersComponent;
  let fixture: ComponentFixture<PickReviewersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PickReviewersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PickReviewersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
