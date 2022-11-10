import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 'c442dfc5-f1e3-4a23-b568-16f6b07dcadc',
};

export const sampleWithPartialData: IProduct = {
  id: '6eb05732-e9cd-4253-953f-e3a45b5084be',
  name: 'neural intuitive SDR',
  description: 'Intelligent structure lime',
  isActive: false,
};

export const sampleWithFullData: IProduct = {
  id: '6a4568a6-3e85-4bab-9435-b43af8bb9bbc',
  name: 'copy Account orchestration',
  description: 'Health strategize Legacy',
  logo: 'harness Clothing Fantastic',
  isActive: true,
};

export const sampleWithNewData: NewProduct = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
