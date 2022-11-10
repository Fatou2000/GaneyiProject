import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-license.test-samples';

import { ProductLicenseFormService } from './product-license-form.service';

describe('ProductLicense Form Service', () => {
  let service: ProductLicenseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductLicenseFormService);
  });

  describe('Service methods', () => {
    describe('createProductLicenseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductLicenseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accessKey: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IProductLicense should create a new form with FormGroup', () => {
        const formGroup = service.createProductLicenseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accessKey: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getProductLicense', () => {
      it('should return NewProductLicense for default ProductLicense initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductLicenseFormGroup(sampleWithNewData);

        const productLicense = service.getProductLicense(formGroup) as any;

        expect(productLicense).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductLicense for empty ProductLicense initial value', () => {
        const formGroup = service.createProductLicenseFormGroup();

        const productLicense = service.getProductLicense(formGroup) as any;

        expect(productLicense).toMatchObject({});
      });

      it('should return IProductLicense', () => {
        const formGroup = service.createProductLicenseFormGroup(sampleWithRequiredData);

        const productLicense = service.getProductLicense(formGroup) as any;

        expect(productLicense).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductLicense should not enable id FormControl', () => {
        const formGroup = service.createProductLicenseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductLicense should disable id FormControl', () => {
        const formGroup = service.createProductLicenseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
