import { TestBed, inject } from '@angular/core/testing';

import { PassParamService } from './pass-param.service';

describe('PassParamService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PassParamService]
    });
  });

  it('should be created', inject([PassParamService], (service: PassParamService) => {
    expect(service).toBeTruthy();
  }));
});
