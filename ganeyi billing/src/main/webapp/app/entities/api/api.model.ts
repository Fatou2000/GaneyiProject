import { IProduct } from 'app/entities/product/product.model';

export interface IApi {
  id: string;
  version?: string | null;
  serviceURL?: string | null;
  docURL?: string | null;
  isActice?: boolean | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewApi = Omit<IApi, 'id'> & { id: null };
