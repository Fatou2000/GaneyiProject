import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProductLicense } from '../product-license.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-license.test-samples';

import { ProductLicenseService, RestProductLicense } from './product-license.service';

const requireRestSample: RestProductLicense = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('ProductLicense Service', () => {
  let service: ProductLicenseService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductLicense | IProductLicense[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductLicenseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ProductLicense', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productLicense = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productLicense).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductLicense', () => {
      const productLicense = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productLicense).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductLicense', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductLicense', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductLicense', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductLicenseToCollectionIfMissing', () => {
      it('should add a ProductLicense to an empty array', () => {
        const productLicense: IProductLicense = sampleWithRequiredData;
        expectedResult = service.addProductLicenseToCollectionIfMissing([], productLicense);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productLicense);
      });

      it('should not add a ProductLicense to an array that contains it', () => {
        const productLicense: IProductLicense = sampleWithRequiredData;
        const productLicenseCollection: IProductLicense[] = [
          {
            ...productLicense,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductLicenseToCollectionIfMissing(productLicenseCollection, productLicense);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductLicense to an array that doesn't contain it", () => {
        const productLicense: IProductLicense = sampleWithRequiredData;
        const productLicenseCollection: IProductLicense[] = [sampleWithPartialData];
        expectedResult = service.addProductLicenseToCollectionIfMissing(productLicenseCollection, productLicense);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productLicense);
      });

      it('should add only unique ProductLicense to an array', () => {
        const productLicenseArray: IProductLicense[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productLicenseCollection: IProductLicense[] = [sampleWithRequiredData];
        expectedResult = service.addProductLicenseToCollectionIfMissing(productLicenseCollection, ...productLicenseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productLicense: IProductLicense = sampleWithRequiredData;
        const productLicense2: IProductLicense = sampleWithPartialData;
        expectedResult = service.addProductLicenseToCollectionIfMissing([], productLicense, productLicense2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productLicense);
        expect(expectedResult).toContain(productLicense2);
      });

      it('should accept null and undefined values', () => {
        const productLicense: IProductLicense = sampleWithRequiredData;
        expectedResult = service.addProductLicenseToCollectionIfMissing([], null, productLicense, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productLicense);
      });

      it('should return initial array if no ProductLicense is added', () => {
        const productLicenseCollection: IProductLicense[] = [sampleWithRequiredData];
        expectedResult = service.addProductLicenseToCollectionIfMissing(productLicenseCollection, undefined, null);
        expect(expectedResult).toEqual(productLicenseCollection);
      });
    });

    describe('compareProductLicense', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductLicense(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareProductLicense(entity1, entity2);
        const compareResult2 = service.compareProductLicense(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareProductLicense(entity1, entity2);
        const compareResult2 = service.compareProductLicense(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareProductLicense(entity1, entity2);
        const compareResult2 = service.compareProductLicense(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
