import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Facture } from './factures/facture.model';
import { Observable } from 'rxjs';



@Injectable({
  providedIn: 'root'
})
export class FactureserviceService {

  private baseUrl = "http://localhost:8081/api/factures/firstname/Arame"

  constructor(private httpClient : HttpClient) {  
  }
  getBillingOfUser():Observable<Facture[]>{
    return this.httpClient.get<Facture[]>(this.baseUrl);

  }
}
