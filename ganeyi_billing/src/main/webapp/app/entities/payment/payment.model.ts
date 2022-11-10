import dayjs from 'dayjs/esm';
import { IFacture } from 'app/entities/facture/facture.model';

export interface IPayment {
  id: string;
  reference?: string | null;
  amount?: string | null;
  type?: string | null;
  status?: string | null;
  paidAt?: dayjs.Dayjs | null;
  facture?: Pick<IFacture, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
