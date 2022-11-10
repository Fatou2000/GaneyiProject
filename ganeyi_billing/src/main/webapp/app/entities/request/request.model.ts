import dayjs from 'dayjs/esm';
import { IResultat } from 'app/entities/resultat/resultat.model';
import { IForfait } from 'app/entities/forfait/forfait.model';

export interface IRequest {
  id: string;
  duration?: number | null;
  status?: string | null;
  requestDate?: dayjs.Dayjs | null;
  resultat?: Pick<IResultat, 'id'> | null;
  forfait?: Pick<IForfait, 'id'> | null;
}

export type NewRequest = Omit<IRequest, 'id'> & { id: null };
