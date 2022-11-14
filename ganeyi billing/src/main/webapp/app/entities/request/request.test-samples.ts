import dayjs from 'dayjs/esm';

import { IRequest, NewRequest } from './request.model';

export const sampleWithRequiredData: IRequest = {
  id: 'e3f0b75f-4dcd-4a52-8038-3e8f26aa67b9',
};

export const sampleWithPartialData: IRequest = {
  id: 'cb7cd6f2-5c52-47d8-a757-f155ac5b52c6',
  duration: 97439,
  status: 'compressing Computers Manager',
  requestDate: dayjs('2022-11-13'),
};

export const sampleWithFullData: IRequest = {
  id: 'd92adc3d-e03d-40d9-8bcc-9213950c7b56',
  duration: 36053,
  status: 'feed',
  requestDate: dayjs('2022-11-14'),
};

export const sampleWithNewData: NewRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
