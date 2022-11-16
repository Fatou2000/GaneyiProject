import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FactureFormService } from './facture-form.service';
import { FactureService } from '../service/facture.service';
import { IFacture } from '../facture.model';
import { IForfait } from 'app/entities/forfait/forfait.model';
import { ForfaitService } from 'app/entities/forfait/service/forfait.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { FactureUpdateComponent } from './facture-update.component';

describe('Facture Management Update Component', () => {
  let comp: FactureUpdateComponent;
  let fixture: ComponentFixture<FactureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factureFormService: FactureFormService;
  let factureService: FactureService;
  let forfaitService: ForfaitService;
  let clientService: ClientService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FactureUpdateComponent],
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
      .overrideTemplate(FactureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factureFormService = TestBed.inject(FactureFormService);
    factureService = TestBed.inject(FactureService);
    forfaitService = TestBed.inject(ForfaitService);
    clientService = TestBed.inject(ClientService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call forfait query and add missing value', () => {
      const facture: IFacture = { id: 'CBA' };
      const forfait: IForfait = { id: '3595e9a6-146c-4051-9a19-735569c9e0cd' };
      facture.forfait = forfait;

      const forfaitCollection: IForfait[] = [{ id: 'ab6ac3fd-5a27-4d93-bad7-517450baf4af' }];
      jest.spyOn(forfaitService, 'query').mockReturnValue(of(new HttpResponse({ body: forfaitCollection })));
      const expectedCollection: IForfait[] = [forfait, ...forfaitCollection];
      jest.spyOn(forfaitService, 'addForfaitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(forfaitService.query).toHaveBeenCalled();
      expect(forfaitService.addForfaitToCollectionIfMissing).toHaveBeenCalledWith(forfaitCollection, forfait);
      expect(comp.forfaitsCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const facture: IFacture = { id: 'CBA' };
      const client: IClient = { id: '97c44591-3375-4999-a86f-071823ba2f32' };
      facture.client = client;

      const clientCollection: IClient[] = [{ id: 'a06bec44-379f-435b-9ef3-066c9a5e30ba' }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining)
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const facture: IFacture = { id: 'CBA' };
      const manytomanies: IProduct[] = [{ id: 'ba206778-db4e-4f8e-aaea-38bd0781440b' }];
      facture.manytomanies = manytomanies;
      const products: IProduct[] = [{ id: '11770717-c4b8-4548-8393-a07deeb41ccd' }];
      facture.products = products;

      const productCollection: IProduct[] = [{ id: '654a87a7-cd1b-42e6-a668-f0755523c128' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...manytomanies, ...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const facture: IFacture = { id: 'CBA' };
      const forfait: IForfait = { id: 'd5ab0f4d-16ed-415b-a470-f320fa6ba979' };
      facture.forfait = forfait;
      const client: IClient = { id: '8afad01e-ad49-4435-957c-81a4c40c31bd' };
      facture.client = client;
      const manytomany: IProduct = { id: '5323beb5-9287-4cdd-b5d9-1a2ddb5c79e1' };
      facture.manytomanies = [manytomany];
      const product: IProduct = { id: '11824deb-abab-468b-bb76-5ca7cee446c9' };
      facture.products = [product];

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(comp.forfaitsCollection).toContain(forfait);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.productsSharedCollection).toContain(manytomany);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.facture).toEqual(facture);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacture>>();
      const facture = { id: 'ABC' };
      jest.spyOn(factureFormService, 'getFacture').mockReturnValue(facture);
      jest.spyOn(factureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facture }));
      saveSubject.complete();

      // THEN
      expect(factureFormService.getFacture).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factureService.update).toHaveBeenCalledWith(expect.objectContaining(facture));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacture>>();
      const facture = { id: 'ABC' };
      jest.spyOn(factureFormService, 'getFacture').mockReturnValue({ id: null });
      jest.spyOn(factureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facture }));
      saveSubject.complete();

      // THEN
      expect(factureFormService.getFacture).toHaveBeenCalled();
      expect(factureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacture>>();
      const facture = { id: 'ABC' };
      jest.spyOn(factureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareForfait', () => {
      it('Should forward to forfaitService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(forfaitService, 'compareForfait');
        comp.compareForfait(entity, entity2);
        expect(forfaitService.compareForfait).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
