import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPricing } from '../pricing.model';

@Component({
  selector: 'jhi-pricing-detail',
  templateUrl: './pricing-detail.component.html',
})
export class PricingDetailComponent implements OnInit {
  pricing: IPricing | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pricing }) => {
      this.pricing = pricing;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
