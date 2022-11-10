import dayjs from 'dayjs/esm';

import { TypeFacturation } from 'app/entities/enumerations/type-facturation.model';
import { FactureStatus } from 'app/entities/enumerations/facture-status.model';

import { IFacture, NewFacture } from './facture.model';

export const sampleWithRequiredData: IFacture = {
  id: '90180b1c-765c-403b-87bc-157d362e5a63',
};

export const sampleWithPartialData: IFacture = {
  id: '4606d92d-72ab-48b1-8efd-78d418d6d56c',
  sousTotal: 63361,
  status: FactureStatus['NON_PAYE'],
  reference: 'azure Account Manor',
};

export const sampleWithFullData: IFacture = {
  id: '9ef80beb-0add-47f8-a88c-29eefdf5b75b',
  rabais: 98336,
  tva: 52576,
  sousTotal: 33310,
  total: 94443,
  typeFacturation: TypeFacturation['FACTURATION_PAR_REQUETE'],
  status: FactureStatus['PAYE'],
  reference: 'Handmade',
  date: dayjs('2022-11-09'),
};

export const sampleWithNewData: NewFacture = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
