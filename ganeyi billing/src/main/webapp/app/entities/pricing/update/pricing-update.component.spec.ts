import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PricingFormService } from './pricing-form.service';
import { PricingService } from '../service/pricing.service';
import { IPricing } from '../pricing.model';
import { IProductLicense } from 'app/entities/product-license/product-license.model';
import { ProductLicenseService } from 'app/entities/product-license/service/product-license.service';

import { PricingUpdateComponent } from './pricing-update.component';

describe('Pricing Management Update Component', () => {
  let comp: PricingUpdateComponent;
  let fixture: ComponentFixture<PricingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pricingFormService: PricingFormService;
  let pricingService: PricingService;
  let productLicenseService: ProductLicenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PricingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PricingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PricingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pricingFormService = TestBed.inject(PricingFormService);
    pricingService = TestBed.inject(PricingService);
    productLicenseService = TestBed.inject(ProductLicenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProductLicense query and add missing value', () => {
      const pricing: IPricing = { id: 'CBA' };
      const productLicense: IProductLicense = { id: '9de4a7a5-f0e5-4ff2-bb47-3ae5e8a4f2d8' };
      pricing.productLicense = productLicense;

      const productLicenseCollection: IProductLicense[] = [{ id: '5be416d4-3f07-44cc-9901-e83c01159851' }];
      jest.spyOn(productLicenseService, 'query').mockReturnValue(of(new HttpResponse({ body: productLicenseCollection })));
      const additionalProductLicenses = [productLicense];
      const expectedCollection: IProductLicense[] = [...additionalProductLicenses, ...productLicenseCollection];
      jest.spyOn(productLicenseService, 'addProductLicenseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      expect(productLicenseService.query).toHaveBeenCalled();
      expect(productLicenseService.addProductLicenseToCollectionIfMissing).toHaveBeenCalledWith(
        productLicenseCollection,
        ...additionalProductLicenses.map(expect.objectContaining)
      );
      expect(comp.productLicensesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pricing: IPricing = { id: 'CBA' };
      const productLicense: IProductLicense = { id: '0d58c2aa-b5a4-4075-ae27-f9a4c7c9329a' };
      pricing.productLicense = productLicense;

      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      expect(comp.productLicensesSharedCollection).toContain(productLicense);
      expect(comp.pricing).toEqual(pricing);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPricing>>();
      const pricing = { id: 'ABC' };
      jest.spyOn(pricingFormService, 'getPricing').mockReturnValue(pricing);
      jest.spyOn(pricingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pricing }));
      saveSubject.complete();

      // THEN
      expect(pricingFormService.getPricing).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pricingService.update).toHaveBeenCalledWith(expect.objectContaining(pricing));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPricing>>();
      const pricing = { id: 'ABC' };
      jest.spyOn(pricingFormService, 'getPricing').mockReturnValue({ id: null });
      jest.spyOn(pricingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pricing: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pricing }));
      saveSubject.complete();

      // THEN
      expect(pricingFormService.getPricing).toHaveBeenCalled();
      expect(pricingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPricing>>();
      const pricing = { id: 'ABC' };
      jest.spyOn(pricingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pricingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductLicense', () => {
      it('Should forward to productLicenseService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(productLicenseService, 'compareProductLicense');
        comp.compareProductLicense(entity, entity2);
        expect(productLicenseService.compareProductLicense).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
