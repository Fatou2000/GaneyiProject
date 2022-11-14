import dayjs from 'dayjs/esm';

import { IProductLicense, NewProductLicense } from './product-license.model';

export const sampleWithRequiredData: IProductLicense = {
  id: '62de8d6b-feff-41b8-af98-f196bde2b9ee',
};

export const sampleWithPartialData: IProductLicense = {
  id: '14120b33-a828-44d4-b4e6-2b1355384b95',
  endDate: dayjs('2022-11-14'),
  isActive: true,
};

export const sampleWithFullData: IProductLicense = {
  id: '33428198-fc22-4401-ba48-337c7ac1a67f',
  accessKey: 'Rubber Associate',
  startDate: dayjs('2022-11-13'),
  endDate: dayjs('2022-11-13'),
  isActive: false,
};

export const sampleWithNewData: NewProductLicense = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
