import { TestBed } from '@angular/core/testing';

import { FactureserviceService } from './factureservice.service';

describe('FactureserviceService', () => {
  let service: FactureserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactureserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
