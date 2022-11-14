import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductLicenseDetailComponent } from './product-license-detail.component';

describe('ProductLicense Management Detail Component', () => {
  let comp: ProductLicenseDetailComponent;
  let fixture: ComponentFixture<ProductLicenseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductLicenseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productLicense: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ProductLicenseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductLicenseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productLicense on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productLicense).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
