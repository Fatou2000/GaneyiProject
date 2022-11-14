import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ClientFormService, ClientFormGroup } from './client-form.service';
import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IForfait } from 'app/entities/forfait/forfait.model';
import { ForfaitService } from 'app/entities/forfait/service/forfait.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;
  client: IClient | null = null;

  productsSharedCollection: IProduct[] = [];
  forfaitsSharedCollection: IForfait[] = [];

  editForm: ClientFormGroup = this.clientFormService.createClientFormGroup();

  constructor(
    protected clientService: ClientService,
    protected clientFormService: ClientFormService,
    protected productService: ProductService,
    protected forfaitService: ForfaitService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareForfait = (o1: IForfait | null, o2: IForfait | null): boolean => this.forfaitService.compareForfait(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.client = client;
      if (client) {
        this.updateForm(client);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.clientFormService.getClient(this.editForm);
    if (client.id !== null) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  protected updateForm(client: IClient): void {
    this.client = client;
    this.clientFormService.resetForm(this.editForm, client);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(client.products ?? [])
    );
    this.forfaitsSharedCollection = this.forfaitService.addForfaitToCollectionIfMissing<IForfait>(
      this.forfaitsSharedCollection,
      ...(client.forfaits ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.client?.products ?? []))
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.forfaitService
      .query()
      .pipe(map((res: HttpResponse<IForfait[]>) => res.body ?? []))
      .pipe(
        map((forfaits: IForfait[]) =>
          this.forfaitService.addForfaitToCollectionIfMissing<IForfait>(forfaits, ...(this.client?.forfaits ?? []))
        )
      )
      .subscribe((forfaits: IForfait[]) => (this.forfaitsSharedCollection = forfaits));
  }
}
