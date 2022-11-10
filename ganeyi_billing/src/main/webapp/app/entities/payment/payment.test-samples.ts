import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: '7bb75e76-458a-47ee-97c2-272eb2852f49',
};

export const sampleWithPartialData: IPayment = {
  id: '9e89be33-7efa-46a0-9709-ce2dc746aa30',
  reference: 'Money Soft empower',
  amount: 'Turkish Small',
  type: 'Dalasi turquoise Card',
  paidAt: dayjs('2022-11-09'),
};

export const sampleWithFullData: IPayment = {
  id: 'dbbb21e5-0aa7-478d-b5e6-b03e265fa7a5',
  reference: 'azure program plum',
  amount: 'foreground even-keeled',
  type: 'Specialist Money violet',
  status: 'withdrawal experiences Rubber',
  paidAt: dayjs('2022-11-09'),
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
