import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCoauthorsComponent } from './add-coauthors.component';

describe('AddCoauthorsComponent', () => {
  let component: AddCoauthorsComponent;
  let fixture: ComponentFixture<AddCoauthorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddCoauthorsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCoauthorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
