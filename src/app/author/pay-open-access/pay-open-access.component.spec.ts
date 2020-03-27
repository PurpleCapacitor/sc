import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PayOpenAccessComponent } from './pay-open-access.component';

describe('PayOpenAccessComponent', () => {
  let component: PayOpenAccessComponent;
  let fixture: ComponentFixture<PayOpenAccessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PayOpenAccessComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PayOpenAccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
