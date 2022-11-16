import { Client } from "./client-model";

export class Forfait{
  id?: string;
  nom?: string | null;
  description?: string | null;
  numberOfQueries?: number | null;
  price?: number | null;
  periode?: string | null;
  actif?: boolean | null;
  clients?: Client | null;
}