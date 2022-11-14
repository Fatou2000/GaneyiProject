import { IProductLicense } from 'app/entities/product-license/product-license.model';

export interface IPricing {
  id: string;
  value?: number | null;
  productLicense?: Pick<IProductLicense, 'id'> | null;
}

export type NewPricing = Omit<IPricing, 'id'> & { id: null };
