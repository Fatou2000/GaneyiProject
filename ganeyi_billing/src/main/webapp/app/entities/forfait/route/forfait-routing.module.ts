import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ForfaitComponent } from '../list/forfait.component';
import { ForfaitDetailComponent } from '../detail/forfait-detail.component';
import { ForfaitUpdateComponent } from '../update/forfait-update.component';
import { ForfaitRoutingResolveService } from './forfait-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const forfaitRoute: Routes = [
  {
    path: '',
    component: ForfaitComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ForfaitDetailComponent,
    resolve: {
      forfait: ForfaitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ForfaitUpdateComponent,
    resolve: {
      forfait: ForfaitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ForfaitUpdateComponent,
    resolve: {
      forfait: ForfaitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(forfaitRoute)],
  exports: [RouterModule],
})
export class ForfaitRoutingModule {}
