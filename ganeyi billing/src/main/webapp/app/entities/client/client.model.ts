import { IProduct } from 'app/entities/product/product.model';
import { IForfait } from 'app/entities/forfait/forfait.model';

export interface IClient {
  id: string;
  accountId?: string | null;
  companyName?: string | null;
  firstName?: string | null;
  address?: string | null;
  phoneNumber?: string | null;
  products?: Pick<IProduct, 'id'>[] | null;
  forfaits?: Pick<IForfait, 'id'>[] | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
