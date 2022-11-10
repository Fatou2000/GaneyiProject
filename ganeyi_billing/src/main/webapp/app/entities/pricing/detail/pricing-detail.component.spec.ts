import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PricingDetailComponent } from './pricing-detail.component';

describe('Pricing Management Detail Component', () => {
  let comp: PricingDetailComponent;
  let fixture: ComponentFixture<PricingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PricingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pricing: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(PricingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PricingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pricing on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pricing).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
