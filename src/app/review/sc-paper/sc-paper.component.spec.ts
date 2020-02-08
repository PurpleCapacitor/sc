import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScPaperComponent } from './sc-paper.component';

describe('ScPaperComponent', () => {
  let component: ScPaperComponent;
  let fixture: ComponentFixture<ScPaperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScPaperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScPaperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
