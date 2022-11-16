import { Forfait } from "./forfait-model";
import { Resultat } from "./resultat-model";

export class Request{
  id?: string;
  duration?: number | null;
  status?: string | null;
  requestDate?: Date | null;
  forfait?: Forfait | null;
  resultat?: Resultat | null;
  
}