import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductFormService } from './product-form.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { IRequest } from 'app/entities/request/request.model';
import { RequestService } from 'app/entities/request/service/request.service';
import { IProductLicense } from 'app/entities/product-license/product-license.model';
import { ProductLicenseService } from 'app/entities/product-license/service/product-license.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let requestService: RequestService;
  let productLicenseService: ProductLicenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    requestService = TestBed.inject(RequestService);
    productLicenseService = TestBed.inject(ProductLicenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Request query and add missing value', () => {
      const product: IProduct = { id: 'CBA' };
      const request: IRequest = { id: '92f6c08c-409a-4b5b-a70e-e071db360562' };
      product.request = request;

      const requestCollection: IRequest[] = [{ id: 'de146349-43f9-4814-8b06-5996c9cac758' }];
      jest.spyOn(requestService, 'query').mockReturnValue(of(new HttpResponse({ body: requestCollection })));
      const additionalRequests = [request];
      const expectedCollection: IRequest[] = [...additionalRequests, ...requestCollection];
      jest.spyOn(requestService, 'addRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(requestService.query).toHaveBeenCalled();
      expect(requestService.addRequestToCollectionIfMissing).toHaveBeenCalledWith(
        requestCollection,
        ...additionalRequests.map(expect.objectContaining)
      );
      expect(comp.requestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProductLicense query and add missing value', () => {
      const product: IProduct = { id: 'CBA' };
      const productLicense: IProductLicense = { id: '5815827d-07ed-49ae-aa15-1da617ac4bb6' };
      product.productLicense = productLicense;

      const productLicenseCollection: IProductLicense[] = [{ id: '84759571-54bd-4c3b-8b68-245b5b6c9739' }];
      jest.spyOn(productLicenseService, 'query').mockReturnValue(of(new HttpResponse({ body: productLicenseCollection })));
      const additionalProductLicenses = [productLicense];
      const expectedCollection: IProductLicense[] = [...additionalProductLicenses, ...productLicenseCollection];
      jest.spyOn(productLicenseService, 'addProductLicenseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productLicenseService.query).toHaveBeenCalled();
      expect(productLicenseService.addProductLicenseToCollectionIfMissing).toHaveBeenCalledWith(
        productLicenseCollection,
        ...additionalProductLicenses.map(expect.objectContaining)
      );
      expect(comp.productLicensesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 'CBA' };
      const request: IRequest = { id: 'd5a38993-46ab-4f6e-8e3c-88074f198531' };
      product.request = request;
      const productLicense: IProductLicense = { id: 'b5478baa-5781-4b6a-9320-9a2f96a7e43c' };
      product.productLicense = productLicense;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.requestsSharedCollection).toContain(request);
      expect(comp.productLicensesSharedCollection).toContain(productLicense);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 'ABC' };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 'ABC' };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 'ABC' };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRequest', () => {
      it('Should forward to requestService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(requestService, 'compareRequest');
        comp.compareRequest(entity, entity2);
        expect(requestService.compareRequest).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
