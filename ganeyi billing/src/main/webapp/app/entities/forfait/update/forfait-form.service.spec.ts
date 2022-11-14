import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../forfait.test-samples';

import { ForfaitFormService } from './forfait-form.service';

describe('Forfait Form Service', () => {
  let service: ForfaitFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ForfaitFormService);
  });

  describe('Service methods', () => {
    describe('createForfaitFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createForfaitFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            numberOfQueries: expect.any(Object),
            price: expect.any(Object),
            periode: expect.any(Object),
            actif: expect.any(Object),
            clients: expect.any(Object),
          })
        );
      });

      it('passing IForfait should create a new form with FormGroup', () => {
        const formGroup = service.createForfaitFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            numberOfQueries: expect.any(Object),
            price: expect.any(Object),
            periode: expect.any(Object),
            actif: expect.any(Object),
            clients: expect.any(Object),
          })
        );
      });
    });

    describe('getForfait', () => {
      it('should return NewForfait for default Forfait initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createForfaitFormGroup(sampleWithNewData);

        const forfait = service.getForfait(formGroup) as any;

        expect(forfait).toMatchObject(sampleWithNewData);
      });

      it('should return NewForfait for empty Forfait initial value', () => {
        const formGroup = service.createForfaitFormGroup();

        const forfait = service.getForfait(formGroup) as any;

        expect(forfait).toMatchObject({});
      });

      it('should return IForfait', () => {
        const formGroup = service.createForfaitFormGroup(sampleWithRequiredData);

        const forfait = service.getForfait(formGroup) as any;

        expect(forfait).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IForfait should not enable id FormControl', () => {
        const formGroup = service.createForfaitFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewForfait should disable id FormControl', () => {
        const formGroup = service.createForfaitFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
