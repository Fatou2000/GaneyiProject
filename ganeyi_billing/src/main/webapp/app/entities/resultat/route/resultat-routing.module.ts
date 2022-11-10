import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResultatComponent } from '../list/resultat.component';
import { ResultatDetailComponent } from '../detail/resultat-detail.component';
import { ResultatUpdateComponent } from '../update/resultat-update.component';
import { ResultatRoutingResolveService } from './resultat-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const resultatRoute: Routes = [
  {
    path: '',
    component: ResultatComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResultatDetailComponent,
    resolve: {
      resultat: ResultatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResultatUpdateComponent,
    resolve: {
      resultat: ResultatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResultatUpdateComponent,
    resolve: {
      resultat: ResultatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resultatRoute)],
  exports: [RouterModule],
})
export class ResultatRoutingModule {}
