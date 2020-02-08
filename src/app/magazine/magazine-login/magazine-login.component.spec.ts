import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MagazineLoginComponent } from './magazine-login.component';

describe('MagazineLoginComponent', () => {
  let component: MagazineLoginComponent;
  let fixture: ComponentFixture<MagazineLoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MagazineLoginComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MagazineLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
