import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductLicenseFormService } from './product-license-form.service';
import { ProductLicenseService } from '../service/product-license.service';
import { IProductLicense } from '../product-license.model';

import { ProductLicenseUpdateComponent } from './product-license-update.component';

describe('ProductLicense Management Update Component', () => {
  let comp: ProductLicenseUpdateComponent;
  let fixture: ComponentFixture<ProductLicenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productLicenseFormService: ProductLicenseFormService;
  let productLicenseService: ProductLicenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductLicenseUpdateComponent],
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
      .overrideTemplate(ProductLicenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductLicenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productLicenseFormService = TestBed.inject(ProductLicenseFormService);
    productLicenseService = TestBed.inject(ProductLicenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productLicense: IProductLicense = { id: 'CBA' };

      activatedRoute.data = of({ productLicense });
      comp.ngOnInit();

      expect(comp.productLicense).toEqual(productLicense);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductLicense>>();
      const productLicense = { id: 'ABC' };
      jest.spyOn(productLicenseFormService, 'getProductLicense').mockReturnValue(productLicense);
      jest.spyOn(productLicenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productLicense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productLicense }));
      saveSubject.complete();

      // THEN
      expect(productLicenseFormService.getProductLicense).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productLicenseService.update).toHaveBeenCalledWith(expect.objectContaining(productLicense));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductLicense>>();
      const productLicense = { id: 'ABC' };
      jest.spyOn(productLicenseFormService, 'getProductLicense').mockReturnValue({ id: null });
      jest.spyOn(productLicenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productLicense: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productLicense }));
      saveSubject.complete();

      // THEN
      expect(productLicenseFormService.getProductLicense).toHaveBeenCalled();
      expect(productLicenseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductLicense>>();
      const productLicense = { id: 'ABC' };
      jest.spyOn(productLicenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productLicense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productLicenseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
