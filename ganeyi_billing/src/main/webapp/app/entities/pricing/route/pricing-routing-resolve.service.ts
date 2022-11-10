import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPricing } from '../pricing.model';
import { PricingService } from '../service/pricing.service';

@Injectable({ providedIn: 'root' })
export class PricingRoutingResolveService implements Resolve<IPricing | null> {
  constructor(protected service: PricingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPricing | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pricing: HttpResponse<IPricing>) => {
          if (pricing.body) {
            return of(pricing.body);
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
