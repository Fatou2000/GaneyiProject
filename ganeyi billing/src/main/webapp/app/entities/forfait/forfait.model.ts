import { IClient } from 'app/entities/client/client.model';

export interface IForfait {
  id: string;
  nom?: string | null;
  description?: string | null;
  numberOfQueries?: number | null;
  price?: number | null;
  periode?: string | null;
  actif?: boolean | null;
  clients?: Pick<IClient, 'id'>[] | null;
}

export type NewForfait = Omit<IForfait, 'id'> & { id: null };
