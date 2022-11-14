import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductLicense } from '../product-license.model';

@Component({
  selector: 'jhi-product-license-detail',
  templateUrl: './product-license-detail.component.html',
})
export class ProductLicenseDetailComponent implements OnInit {
  productLicense: IProductLicense | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productLicense }) => {
      this.productLicense = productLicense;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
