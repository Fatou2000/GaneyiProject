import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FactureFormService, FactureFormGroup } from './facture-form.service';
import { IFacture } from '../facture.model';
import { FactureService } from '../service/facture.service';
import { IForfait } from 'app/entities/forfait/forfait.model';
import { ForfaitService } from 'app/entities/forfait/service/forfait.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { TypeFacturation } from 'app/entities/enumerations/type-facturation.model';
import { FactureStatus } from 'app/entities/enumerations/facture-status.model';

@Component({
  selector: 'jhi-facture-update',
  templateUrl: './facture-update.component.html',
})
export class FactureUpdateComponent implements OnInit {
  isSaving = false;
  facture: IFacture | null = null;
  typeFacturationValues = Object.keys(TypeFacturation);
  factureStatusValues = Object.keys(FactureStatus);

  forfaitsCollection: IForfait[] = [];
  clientsSharedCollection: IClient[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: FactureFormGroup = this.factureFormService.createFactureFormGroup();

  constructor(
    protected factureService: FactureService,
    protected factureFormService: FactureFormService,
    protected forfaitService: ForfaitService,
    protected clientService: ClientService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareForfait = (o1: IForfait | null, o2: IForfait | null): boolean => this.forfaitService.compareForfait(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facture }) => {
      this.facture = facture;
      if (facture) {
        this.updateForm(facture);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facture = this.factureFormService.getFacture(this.editForm);
    if (facture.id !== null) {
      this.subscribeToSaveResponse(this.factureService.update(facture));
    } else {
      this.subscribeToSaveResponse(this.factureService.create(facture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacture>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(facture: IFacture): void {
    this.facture = facture;
    this.factureFormService.resetForm(this.editForm, facture);

    this.forfaitsCollection = this.forfaitService.addForfaitToCollectionIfMissing<IForfait>(this.forfaitsCollection, facture.forfait);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, facture.client);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(facture.manytomanies ?? []),
      ...(facture.products ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.forfaitService
      .query({ filter: 'facture-is-null' })
      .pipe(map((res: HttpResponse<IForfait[]>) => res.body ?? []))
      .pipe(map((forfaits: IForfait[]) => this.forfaitService.addForfaitToCollectionIfMissing<IForfait>(forfaits, this.facture?.forfait)))
      .subscribe((forfaits: IForfait[]) => (this.forfaitsCollection = forfaits));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.facture?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(
            products,
            ...(this.facture?.manytomanies ?? []),
            ...(this.facture?.products ?? [])
          )
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
