import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductLicenseComponent } from '../list/product-license.component';
import { ProductLicenseDetailComponent } from '../detail/product-license-detail.component';
import { ProductLicenseUpdateComponent } from '../update/product-license-update.component';
import { ProductLicenseRoutingResolveService } from './product-license-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productLicenseRoute: Routes = [
  {
    path: '',
    component: ProductLicenseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductLicenseDetailComponent,
    resolve: {
      productLicense: ProductLicenseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductLicenseUpdateComponent,
    resolve: {
      productLicense: ProductLicenseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductLicenseUpdateComponent,
    resolve: {
      productLicense: ProductLicenseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productLicenseRoute)],
  exports: [RouterModule],
})
export class ProductLicenseRoutingModule {}
