import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MagazineApproveComponent } from './magazine-approve.component';

describe('MagazineApproveComponent', () => {
  let component: MagazineApproveComponent;
  let fixture: ComponentFixture<MagazineApproveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MagazineApproveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MagazineApproveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
