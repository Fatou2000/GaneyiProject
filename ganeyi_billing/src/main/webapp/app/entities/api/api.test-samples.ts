import { IApi, NewApi } from './api.model';

export const sampleWithRequiredData: IApi = {
  id: '9a971edf-999a-45a2-aa36-3e6071be45d2',
};

export const sampleWithPartialData: IApi = {
  id: '5ffd238a-345f-4047-83fa-23385bb1a59a',
  version: 'Computers neural',
  serviceURL: 'Handmade',
  docURL: 'Soft plug-and-play',
};

export const sampleWithFullData: IApi = {
  id: '0d29f6b8-2a45-4fbb-882e-82f9892308db',
  version: 'Computers',
  serviceURL: 'Buckinghamshire 24/7',
  docURL: 'Secured 24/365',
  isActice: true,
};

export const sampleWithNewData: NewApi = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
