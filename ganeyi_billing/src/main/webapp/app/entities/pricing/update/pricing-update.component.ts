import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PricingFormService, PricingFormGroup } from './pricing-form.service';
import { IPricing } from '../pricing.model';
import { PricingService } from '../service/pricing.service';
import { IProductLicense } from 'app/entities/product-license/product-license.model';
import { ProductLicenseService } from 'app/entities/product-license/service/product-license.service';

@Component({
  selector: 'jhi-pricing-update',
  templateUrl: './pricing-update.component.html',
})
export class PricingUpdateComponent implements OnInit {
  isSaving = false;
  pricing: IPricing | null = null;

  productLicensesSharedCollection: IProductLicense[] = [];

  editForm: PricingFormGroup = this.pricingFormService.createPricingFormGroup();

  constructor(
    protected pricingService: PricingService,
    protected pricingFormService: PricingFormService,
    protected productLicenseService: ProductLicenseService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProductLicense = (o1: IProductLicense | null, o2: IProductLicense | null): boolean =>
    this.productLicenseService.compareProductLicense(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pricing }) => {
      this.pricing = pricing;
      if (pricing) {
        this.updateForm(pricing);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pricing = this.pricingFormService.getPricing(this.editForm);
    if (pricing.id !== null) {
      this.subscribeToSaveResponse(this.pricingService.update(pricing));
    } else {
      this.subscribeToSaveResponse(this.pricingService.create(pricing));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPricing>>): void {
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

  protected updateForm(pricing: IPricing): void {
    this.pricing = pricing;
    this.pricingFormService.resetForm(this.editForm, pricing);

    this.productLicensesSharedCollection = this.productLicenseService.addProductLicenseToCollectionIfMissing<IProductLicense>(
      this.productLicensesSharedCollection,
      pricing.productLicense
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productLicenseService
      .query()
      .pipe(map((res: HttpResponse<IProductLicense[]>) => res.body ?? []))
      .pipe(
        map((productLicenses: IProductLicense[]) =>
          this.productLicenseService.addProductLicenseToCollectionIfMissing<IProductLicense>(productLicenses, this.pricing?.productLicense)
        )
      )
      .subscribe((productLicenses: IProductLicense[]) => (this.productLicensesSharedCollection = productLicenses));
  }
}
