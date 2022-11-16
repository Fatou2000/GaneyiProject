import { Client } from "./client-model";
import { FactureStatus } from "./facture-status.model";
import { Forfait } from "./forfait-model";
import { TypeFacturation } from "./type-facturation.model";

export class Facture{
    id? : string;
    rabais? : number | null ;
    tva?: number | null;
    sousTotal?: number | null;
    total?: number | null;
    typeFacturation?: TypeFacturation | null ;
    status?: FactureStatus | null ;
    reference?: string | null;
    date?: Date | null;
    forfait?: Forfait | null;
    client?: Client | null;
}