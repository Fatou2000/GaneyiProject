import { Forfait } from "./forfait-model";
import { Product } from "./product-model";

export class Client{
    id?: string;
    accountId?: string | null;
    companyName?: string | null;
    firstName?: string | null;
    address?: string | null;
    phoneNumber?: string | null;
    products?: Product | null;
    forfaits?: Forfait | null;
}