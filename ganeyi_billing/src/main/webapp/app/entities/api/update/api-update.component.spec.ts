import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApiFormService } from './api-form.service';
import { ApiService } from '../service/api.service';
import { IApi } from '../api.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ApiUpdateComponent } from './api-update.component';

describe('Api Management Update Component', () => {
  let comp: ApiUpdateComponent;
  let fixture: ComponentFixture<ApiUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apiFormService: ApiFormService;
  let apiService: ApiService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApiUpdateComponent],
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
      .overrideTemplate(ApiUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApiUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apiFormService = TestBed.inject(ApiFormService);
    apiService = TestBed.inject(ApiService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const api: IApi = { id: 'CBA' };
      const product: IProduct = { id: '59c9b9b1-b09f-4e0e-b589-be713560ce8c' };
      api.product = product;

      const productCollection: IProduct[] = [{ id: '1022a480-5e59-4a15-be93-e27edf77a77f' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ api });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const api: IApi = { id: 'CBA' };
      const product: IProduct = { id: '54ade0f0-e775-4370-b661-fc3da965392d' };
      api.product = product;

      activatedRoute.data = of({ api });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.api).toEqual(api);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApi>>();
      const api = { id: 'ABC' };
      jest.spyOn(apiFormService, 'getApi').mockReturnValue(api);
      jest.spyOn(apiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ api });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: api }));
      saveSubject.complete();

      // THEN
      expect(apiFormService.getApi).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apiService.update).toHaveBeenCalledWith(expect.objectContaining(api));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApi>>();
      const api = { id: 'ABC' };
      jest.spyOn(apiFormService, 'getApi').mockReturnValue({ id: null });
      jest.spyOn(apiService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ api: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: api }));
      saveSubject.complete();

      // THEN
      expect(apiFormService.getApi).toHaveBeenCalled();
      expect(apiService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApi>>();
      const api = { id: 'ABC' };
      jest.spyOn(apiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ api });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apiService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
