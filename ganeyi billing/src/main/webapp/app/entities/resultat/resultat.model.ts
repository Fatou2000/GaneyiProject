export interface IResultat {
  id: string;
  value?: string | null;
  valueContentType?: string | null;
}

export type NewResultat = Omit<IResultat, 'id'> & { id: null };
