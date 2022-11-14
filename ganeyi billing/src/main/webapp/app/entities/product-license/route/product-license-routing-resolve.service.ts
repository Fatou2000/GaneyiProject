import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductLicense } from '../product-license.model';
import { ProductLicenseService } from '../service/product-license.service';

@Injectable({ providedIn: 'root' })
export class ProductLicenseRoutingResolveService implements Resolve<IProductLicense | null> {
  constructor(protected service: ProductLicenseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductLicense | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productLicense: HttpResponse<IProductLicense>) => {
          if (productLicense.body) {
            return of(productLicense.body);
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
