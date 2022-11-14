import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ForfaitFormService } from './forfait-form.service';
import { ForfaitService } from '../service/forfait.service';
import { IForfait } from '../forfait.model';

import { ForfaitUpdateComponent } from './forfait-update.component';

describe('Forfait Management Update Component', () => {
  let comp: ForfaitUpdateComponent;
  let fixture: ComponentFixture<ForfaitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let forfaitFormService: ForfaitFormService;
  let forfaitService: ForfaitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ForfaitUpdateComponent],
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
      .overrideTemplate(ForfaitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ForfaitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    forfaitFormService = TestBed.inject(ForfaitFormService);
    forfaitService = TestBed.inject(ForfaitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const forfait: IForfait = { id: 'CBA' };

      activatedRoute.data = of({ forfait });
      comp.ngOnInit();

      expect(comp.forfait).toEqual(forfait);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IForfait>>();
      const forfait = { id: 'ABC' };
      jest.spyOn(forfaitFormService, 'getForfait').mockReturnValue(forfait);
      jest.spyOn(forfaitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forfait });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: forfait }));
      saveSubject.complete();

      // THEN
      expect(forfaitFormService.getForfait).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(forfaitService.update).toHaveBeenCalledWith(expect.objectContaining(forfait));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IForfait>>();
      const forfait = { id: 'ABC' };
      jest.spyOn(forfaitFormService, 'getForfait').mockReturnValue({ id: null });
      jest.spyOn(forfaitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forfait: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: forfait }));
      saveSubject.complete();

      // THEN
      expect(forfaitFormService.getForfait).toHaveBeenCalled();
      expect(forfaitService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IForfait>>();
      const forfait = { id: 'ABC' };
      jest.spyOn(forfaitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forfait });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(forfaitService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
