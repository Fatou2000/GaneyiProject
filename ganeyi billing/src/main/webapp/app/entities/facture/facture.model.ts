import dayjs from 'dayjs/esm';
import { IForfait } from 'app/entities/forfait/forfait.model';
import { IClient } from 'app/entities/client/client.model';
import { TypeFacturation } from 'app/entities/enumerations/type-facturation.model';
import { FactureStatus } from 'app/entities/enumerations/facture-status.model';

export interface IFacture {
  id: string;
  rabais?: number | null;
  tva?: number | null;
  sousTotal?: number | null;
  total?: number | null;
  typeFacturation?: TypeFacturation | null;
  status?: FactureStatus | null;
  reference?: string | null;
  date?: dayjs.Dayjs | null;
  forfait?: Pick<IForfait, 'id'> | null;
  client?: Pick<IClient, 'id'> | null;
}

export type NewFacture = Omit<IFacture, 'id'> & { id: null };
