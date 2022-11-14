import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 'b5d26ec1-78ed-413c-a418-9dee47137427',
};

export const sampleWithPartialData: IClient = {
  id: '403cc418-0b6c-463f-9c75-91621bc0e726',
  accountId: 'Soft',
  companyName: 'definition Synergized Brand',
  firstName: 'Kameron',
  address: 'Hat Director connect',
  phoneNumber: 'EXE Illinois Vatu',
};

export const sampleWithFullData: IClient = {
  id: '2ffde4b2-2132-4798-b369-2f042aad5039',
  accountId: 'Kids redundant Distributed',
  companyName: 'ivory Account schemas',
  firstName: 'Vilma',
  address: 'cross-platform payment Cape',
  phoneNumber: 'Borders bandwidth',
};

export const sampleWithNewData: NewClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
