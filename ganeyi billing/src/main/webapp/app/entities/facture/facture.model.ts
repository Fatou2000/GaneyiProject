import dayjs from 'dayjs/esm';
import { IForfait } from 'app/entities/forfait/forfait.model';
import { IClient } from 'app/entities/client/client.model';
import { IProduct } from 'app/entities/product/product.model';
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
  numero?: string | null;
  forfait?: Pick<IForfait, 'id'> | null;
  client?: Pick<IClient, 'id'> | null;
  manytomanies?: Pick<IProduct, 'id' | 'name'>[] | null;
  products?: Pick<IProduct, 'id' | 'name'>[] | null;
}

export type NewFacture = Omit<IFacture, 'id'> & { id: null };
