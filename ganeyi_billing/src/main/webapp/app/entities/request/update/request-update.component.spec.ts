import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RequestFormService } from './request-form.service';
import { RequestService } from '../service/request.service';
import { IRequest } from '../request.model';
import { IResultat } from 'app/entities/resultat/resultat.model';
import { ResultatService } from 'app/entities/resultat/service/resultat.service';
import { IForfait } from 'app/entities/forfait/forfait.model';
import { ForfaitService } from 'app/entities/forfait/service/forfait.service';

import { RequestUpdateComponent } from './request-update.component';

describe('Request Management Update Component', () => {
  let comp: RequestUpdateComponent;
  let fixture: ComponentFixture<RequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let requestFormService: RequestFormService;
  let requestService: RequestService;
  let resultatService: ResultatService;
  let forfaitService: ForfaitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RequestUpdateComponent],
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
      .overrideTemplate(RequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestFormService = TestBed.inject(RequestFormService);
    requestService = TestBed.inject(RequestService);
    resultatService = TestBed.inject(ResultatService);
    forfaitService = TestBed.inject(ForfaitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call resultat query and add missing value', () => {
      const request: IRequest = { id: 'CBA' };
      const resultat: IResultat = { id: '695f9788-34d9-4a74-ad63-5cd4416cde8a' };
      request.resultat = resultat;

      const resultatCollection: IResultat[] = [{ id: '79c73346-104f-4647-84f6-51cb44a9407a' }];
      jest.spyOn(resultatService, 'query').mockReturnValue(of(new HttpResponse({ body: resultatCollection })));
      const expectedCollection: IResultat[] = [resultat, ...resultatCollection];
      jest.spyOn(resultatService, 'addResultatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(resultatService.query).toHaveBeenCalled();
      expect(resultatService.addResultatToCollectionIfMissing).toHaveBeenCalledWith(resultatCollection, resultat);
      expect(comp.resultatsCollection).toEqual(expectedCollection);
    });

    it('Should call Forfait query and add missing value', () => {
      const request: IRequest = { id: 'CBA' };
      const forfait: IForfait = { id: '85a6da7b-b881-4c27-8365-4c3475010ea0' };
      request.forfait = forfait;

      const forfaitCollection: IForfait[] = [{ id: '9126fa15-76f3-46f6-981d-8b3986685fd2' }];
      jest.spyOn(forfaitService, 'query').mockReturnValue(of(new HttpResponse({ body: forfaitCollection })));
      const additionalForfaits = [forfait];
      const expectedCollection: IForfait[] = [...additionalForfaits, ...forfaitCollection];
      jest.spyOn(forfaitService, 'addForfaitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(forfaitService.query).toHaveBeenCalled();
      expect(forfaitService.addForfaitToCollectionIfMissing).toHaveBeenCalledWith(
        forfaitCollection,
        ...additionalForfaits.map(expect.objectContaining)
      );
      expect(comp.forfaitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const request: IRequest = { id: 'CBA' };
      const resultat: IResultat = { id: '72ee8fe2-5d5e-49f2-8110-15a901379d3d' };
      request.resultat = resultat;
      const forfait: IForfait = { id: 'fd2e50c0-34ae-4e63-80c8-2037d1c9052b' };
      request.forfait = forfait;

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(comp.resultatsCollection).toContain(resultat);
      expect(comp.forfaitsSharedCollection).toContain(forfait);
      expect(comp.request).toEqual(request);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 'ABC' };
      jest.spyOn(requestFormService, 'getRequest').mockReturnValue(request);
      jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: request }));
      saveSubject.complete();

      // THEN
      expect(requestFormService.getRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestService.update).toHaveBeenCalledWith(expect.objectContaining(request));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 'ABC' };
      jest.spyOn(requestFormService, 'getRequest').mockReturnValue({ id: null });
      jest.spyOn(requestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: request }));
      saveSubject.complete();

      // THEN
      expect(requestFormService.getRequest).toHaveBeenCalled();
      expect(requestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 'ABC' };
      jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResultat', () => {
      it('Should forward to resultatService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(resultatService, 'compareResultat');
        comp.compareResultat(entity, entity2);
        expect(resultatService.compareResultat).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareForfait', () => {
      it('Should forward to forfaitService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(forfaitService, 'compareForfait');
        comp.compareForfait(entity, entity2);
        expect(forfaitService.compareForfait).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
