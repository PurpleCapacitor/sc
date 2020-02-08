import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScAreaComponent } from './sc-area.component';

describe('ScAreaComponent', () => {
  let component: ScAreaComponent;
  let fixture: ComponentFixture<ScAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
