import { IResultat, NewResultat } from './resultat.model';

export const sampleWithRequiredData: IResultat = {
  id: 'de8d86d4-bebf-439a-9413-7c69663c14ab',
};

export const sampleWithPartialData: IResultat = {
  id: 'f50cdf3c-65f6-4080-80b1-27bfb3dc4baf',
  value: '../fake-data/blob/hipster.png',
  valueContentType: 'unknown',
};

export const sampleWithFullData: IResultat = {
  id: 'b318c048-dc19-4761-b66f-283a812c402d',
  value: '../fake-data/blob/hipster.png',
  valueContentType: 'unknown',
};

export const sampleWithNewData: NewResultat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
