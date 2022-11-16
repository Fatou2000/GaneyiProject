import dayjs from 'dayjs/esm';

import { TypeFacturation } from 'app/entities/enumerations/type-facturation.model';
import { FactureStatus } from 'app/entities/enumerations/facture-status.model';

import { IFacture, NewFacture } from './facture.model';

export const sampleWithRequiredData: IFacture = {
  id: '90180b1c-765c-403b-87bc-157d362e5a63',
};

export const sampleWithPartialData: IFacture = {
  id: '606d92d7-2ab8-4b18-afd7-8d418d6d56ca',
  sousTotal: 74642,
  status: FactureStatus['NON_PAYE'],
  reference: 'Optimization',
};

export const sampleWithFullData: IFacture = {
  id: '2c7709ef-80be-4b0a-9d7f-8688c29eefdf',
  rabais: 33588,
  tva: 71290,
  sousTotal: 48267,
  total: 31715,
  typeFacturation: TypeFacturation['FACTURATION_PAR_PERIODE'],
  status: FactureStatus['NON_PAYE'],
  reference: 'throughput Books',
  date: dayjs('2022-11-13'),
  numero: 'deposit Incredible architectures',
};

export const sampleWithNewData: NewFacture = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
