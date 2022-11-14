import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProductLicenseFormService, ProductLicenseFormGroup } from './product-license-form.service';
import { IProductLicense } from '../product-license.model';
import { ProductLicenseService } from '../service/product-license.service';

@Component({
  selector: 'jhi-product-license-update',
  templateUrl: './product-license-update.component.html',
})
export class ProductLicenseUpdateComponent implements OnInit {
  isSaving = false;
  productLicense: IProductLicense | null = null;

  editForm: ProductLicenseFormGroup = this.productLicenseFormService.createProductLicenseFormGroup();

  constructor(
    protected productLicenseService: ProductLicenseService,
    protected productLicenseFormService: ProductLicenseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productLicense }) => {
      this.productLicense = productLicense;
      if (productLicense) {
        this.updateForm(productLicense);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productLicense = this.productLicenseFormService.getProductLicense(this.editForm);
    if (productLicense.id !== null) {
      this.subscribeToSaveResponse(this.productLicenseService.update(productLicense));
    } else {
      this.subscribeToSaveResponse(this.productLicenseService.create(productLicense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductLicense>>): void {
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

  protected updateForm(productLicense: IProductLicense): void {
    this.productLicense = productLicense;
    this.productLicenseFormService.resetForm(this.editForm, productLicense);
  }
}
