import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperDecisionComponent } from './paper-decision.component';

describe('PaperDecisionComponent', () => {
  let component: PaperDecisionComponent;
  let fixture: ComponentFixture<PaperDecisionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaperDecisionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaperDecisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
