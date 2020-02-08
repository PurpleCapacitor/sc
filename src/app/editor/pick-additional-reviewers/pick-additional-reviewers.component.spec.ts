import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PickAdditionalReviewersComponent } from './pick-additional-reviewers.component';

describe('PickAdditionalReviewersComponent', () => {
  let component: PickAdditionalReviewersComponent;
  let fixture: ComponentFixture<PickAdditionalReviewersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PickAdditionalReviewersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PickAdditionalReviewersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
