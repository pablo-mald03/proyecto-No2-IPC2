import { TestBed } from '@angular/core/testing';

import { Popup } from './popup';

describe('Popup', () => {
  let service: Popup;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Popup);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
