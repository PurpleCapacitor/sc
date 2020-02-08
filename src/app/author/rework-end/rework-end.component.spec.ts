import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReworkEndComponent } from './rework-end.component';

describe('ReworkEndComponent', () => {
  let component: ReworkEndComponent;
  let fixture: ComponentFixture<ReworkEndComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReworkEndComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReworkEndComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
