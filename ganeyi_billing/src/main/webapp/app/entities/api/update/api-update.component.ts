import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ApiFormService, ApiFormGroup } from './api-form.service';
import { IApi } from '../api.model';
import { ApiService } from '../service/api.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-api-update',
  templateUrl: './api-update.component.html',
})
export class ApiUpdateComponent implements OnInit {
  isSaving = false;
  api: IApi | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: ApiFormGroup = this.apiFormService.createApiFormGroup();

  constructor(
    protected apiService: ApiService,
    protected apiFormService: ApiFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ api }) => {
      this.api = api;
      if (api) {
        this.updateForm(api);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const api = this.apiFormService.getApi(this.editForm);
    if (api.id !== null) {
      this.subscribeToSaveResponse(this.apiService.update(api));
    } else {
      this.subscribeToSaveResponse(this.apiService.create(api));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApi>>): void {
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

  protected updateForm(api: IApi): void {
    this.api = api;
    this.apiFormService.resetForm(this.editForm, api);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      api.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.api?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
