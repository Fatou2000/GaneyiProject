import dayjs from 'dayjs/esm';

export interface IProductLicense {
  id: string;
  accessKey?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  isActive?: boolean | null;
}

export type NewProductLicense = Omit<IProductLicense, 'id'> & { id: null };
