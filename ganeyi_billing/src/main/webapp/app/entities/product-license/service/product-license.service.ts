import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductLicense, NewProductLicense } from '../product-license.model';

export type PartialUpdateProductLicense = Partial<IProductLicense> & Pick<IProductLicense, 'id'>;

type RestOf<T extends IProductLicense | NewProductLicense> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestProductLicense = RestOf<IProductLicense>;

export type NewRestProductLicense = RestOf<NewProductLicense>;

export type PartialUpdateRestProductLicense = RestOf<PartialUpdateProductLicense>;

export type EntityResponseType = HttpResponse<IProductLicense>;
export type EntityArrayResponseType = HttpResponse<IProductLicense[]>;

@Injectable({ providedIn: 'root' })
export class ProductLicenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-licenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productLicense: NewProductLicense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productLicense);
    return this.http
      .post<RestProductLicense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productLicense: IProductLicense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productLicense);
    return this.http
      .put<RestProductLicense>(`${this.resourceUrl}/${this.getProductLicenseIdentifier(productLicense)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productLicense: PartialUpdateProductLicense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productLicense);
    return this.http
      .patch<RestProductLicense>(`${this.resourceUrl}/${this.getProductLicenseIdentifier(productLicense)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestProductLicense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductLicense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductLicenseIdentifier(productLicense: Pick<IProductLicense, 'id'>): string {
    return productLicense.id;
  }

  compareProductLicense(o1: Pick<IProductLicense, 'id'> | null, o2: Pick<IProductLicense, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductLicenseIdentifier(o1) === this.getProductLicenseIdentifier(o2) : o1 === o2;
  }

  addProductLicenseToCollectionIfMissing<Type extends Pick<IProductLicense, 'id'>>(
    productLicenseCollection: Type[],
    ...productLicensesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productLicenses: Type[] = productLicensesToCheck.filter(isPresent);
    if (productLicenses.length > 0) {
      const productLicenseCollectionIdentifiers = productLicenseCollection.map(
        productLicenseItem => this.getProductLicenseIdentifier(productLicenseItem)!
      );
      const productLicensesToAdd = productLicenses.filter(productLicenseItem => {
        const productLicenseIdentifier = this.getProductLicenseIdentifier(productLicenseItem);
        if (productLicenseCollectionIdentifiers.includes(productLicenseIdentifier)) {
          return false;
        }
        productLicenseCollectionIdentifiers.push(productLicenseIdentifier);
        return true;
      });
      return [...productLicensesToAdd, ...productLicenseCollection];
    }
    return productLicenseCollection;
  }

  protected convertDateFromClient<T extends IProductLicense | NewProductLicense | PartialUpdateProductLicense>(
    productLicense: T
  ): RestOf<T> {
    return {
      ...productLicense,
      startDate: productLicense.startDate?.format(DATE_FORMAT) ?? null,
      endDate: productLicense.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProductLicense: RestProductLicense): IProductLicense {
    return {
      ...restProductLicense,
      startDate: restProductLicense.startDate ? dayjs(restProductLicense.startDate) : undefined,
      endDate: restProductLicense.endDate ? dayjs(restProductLicense.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductLicense>): HttpResponse<IProductLicense> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductLicense[]>): HttpResponse<IProductLicense[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
