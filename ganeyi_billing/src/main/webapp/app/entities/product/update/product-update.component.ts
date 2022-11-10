import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductFormService, ProductFormGroup } from './product-form.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { IRequest } from 'app/entities/request/request.model';
import { RequestService } from 'app/entities/request/service/request.service';
import { IProductLicense } from 'app/entities/product-license/product-license.model';
import { ProductLicenseService } from 'app/entities/product-license/service/product-license.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;

  requestsSharedCollection: IRequest[] = [];
  productLicensesSharedCollection: IProductLicense[] = [];

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected requestService: RequestService,
    protected productLicenseService: ProductLicenseService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRequest = (o1: IRequest | null, o2: IRequest | null): boolean => this.requestService.compareRequest(o1, o2);

  compareProductLicense = (o1: IProductLicense | null, o2: IProductLicense | null): boolean =>
    this.productLicenseService.compareProductLicense(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.requestsSharedCollection = this.requestService.addRequestToCollectionIfMissing<IRequest>(
      this.requestsSharedCollection,
      product.request
    );
    this.productLicensesSharedCollection = this.productLicenseService.addProductLicenseToCollectionIfMissing<IProductLicense>(
      this.productLicensesSharedCollection,
      product.productLicense
    );
  }

  protected loadRelationshipsOptions(): void {
    this.requestService
      .query()
      .pipe(map((res: HttpResponse<IRequest[]>) => res.body ?? []))
      .pipe(map((requests: IRequest[]) => this.requestService.addRequestToCollectionIfMissing<IRequest>(requests, this.product?.request)))
      .subscribe((requests: IRequest[]) => (this.requestsSharedCollection = requests));

    this.productLicenseService
      .query()
      .pipe(map((res: HttpResponse<IProductLicense[]>) => res.body ?? []))
      .pipe(
        map((productLicenses: IProductLicense[]) =>
          this.productLicenseService.addProductLicenseToCollectionIfMissing<IProductLicense>(productLicenses, this.product?.productLicense)
        )
      )
      .subscribe((productLicenses: IProductLicense[]) => (this.productLicensesSharedCollection = productLicenses));
  }
}
