import { IRequest } from 'app/entities/request/request.model';
import { IProductLicense } from 'app/entities/product-license/product-license.model';
import { IClient } from 'app/entities/client/client.model';

export interface IProduct {
  id: string;
  name?: string | null;
  description?: string | null;
  logo?: string | null;
  isActive?: boolean | null;
  request?: Pick<IRequest, 'id'> | null;
  productLicense?: Pick<IProductLicense, 'id'> | null;
  clients?: Pick<IClient, 'id'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
