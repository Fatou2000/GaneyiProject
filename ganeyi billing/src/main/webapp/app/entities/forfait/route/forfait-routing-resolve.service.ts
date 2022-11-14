import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IForfait } from '../forfait.model';
import { ForfaitService } from '../service/forfait.service';

@Injectable({ providedIn: 'root' })
export class ForfaitRoutingResolveService implements Resolve<IForfait | null> {
  constructor(protected service: ForfaitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IForfait | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((forfait: HttpResponse<IForfait>) => {
          if (forfait.body) {
            return of(forfait.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
