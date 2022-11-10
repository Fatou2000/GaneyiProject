import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PricingComponent } from '../list/pricing.component';
import { PricingDetailComponent } from '../detail/pricing-detail.component';
import { PricingUpdateComponent } from '../update/pricing-update.component';
import { PricingRoutingResolveService } from './pricing-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pricingRoute: Routes = [
  {
    path: '',
    component: PricingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PricingDetailComponent,
    resolve: {
      pricing: PricingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PricingUpdateComponent,
    resolve: {
      pricing: PricingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PricingUpdateComponent,
    resolve: {
      pricing: PricingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pricingRoute)],
  exports: [RouterModule],
})
export class PricingRoutingModule {}
