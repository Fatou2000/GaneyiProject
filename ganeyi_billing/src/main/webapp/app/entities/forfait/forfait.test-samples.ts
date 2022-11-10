import { IForfait, NewForfait } from './forfait.model';

export const sampleWithRequiredData: IForfait = {
  id: '8b8ab12c-ad6e-4e5e-ab84-37adfa54ed13',
};

export const sampleWithPartialData: IForfait = {
  id: '9b14b97f-40f6-4f92-8a69-70b69f1f61bf',
  nom: 'Right-sized alliance generate',
  numberOfQueries: 78370,
  price: 17717,
  actif: true,
};

export const sampleWithFullData: IForfait = {
  id: 'd2ddd40c-ab6c-4672-b4b5-30a4209e04d3',
  nom: 'Pants HTTP',
  description: 'cross-media',
  numberOfQueries: 7657,
  price: 52061,
  periode: 'Operative Brooks (E.M.U.-6)',
  actif: false,
};

export const sampleWithNewData: NewForfait = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
