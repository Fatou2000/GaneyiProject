import { IPricing, NewPricing } from './pricing.model';

export const sampleWithRequiredData: IPricing = {
  id: 'eaa594c7-c486-447a-a91f-a167f862f9e5',
};

export const sampleWithPartialData: IPricing = {
  id: 'a6ff6d18-5192-40ff-8998-9eb9649b9e83',
};

export const sampleWithFullData: IPricing = {
  id: 'c9aa1b08-565b-40b7-a5ae-7dc97d9068bf',
  value: 42749,
};

export const sampleWithNewData: NewPricing = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
