import { Component, OnInit } from '@angular/core';
import { FactureserviceService } from '../factureservice.service';
import { Facture } from './facture.model';

@Component({
  selector: 'app-factures',
  templateUrl: './factures.component.html',
  styleUrls: ['./factures.component.css']
})
export class FacturesComponent implements OnInit {

  factures : any;
  liste_factures : any =[];
  firstName : string ="Arame"
  

  constructor(private factureservice : FactureserviceService) { }

  ngOnInit(): void {
    this.getBilling();
  }

  private getBilling(){
    this.factureservice.getBillingOfUser().subscribe(data=>{
      this.factures = data;
    });
  }

}
