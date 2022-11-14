import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IForfait, NewForfait } from '../forfait.model';

export type PartialUpdateForfait = Partial<IForfait> & Pick<IForfait, 'id'>;

export type EntityResponseType = HttpResponse<IForfait>;
export type EntityArrayResponseType = HttpResponse<IForfait[]>;

@Injectable({ providedIn: 'root' })
export class ForfaitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/forfaits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(forfait: NewForfait): Observable<EntityResponseType> {
    return this.http.post<IForfait>(this.resourceUrl, forfait, { observe: 'response' });
  }

  update(forfait: IForfait): Observable<EntityResponseType> {
    return this.http.put<IForfait>(`${this.resourceUrl}/${this.getForfaitIdentifier(forfait)}`, forfait, { observe: 'response' });
  }

  partialUpdate(forfait: PartialUpdateForfait): Observable<EntityResponseType> {
    return this.http.patch<IForfait>(`${this.resourceUrl}/${this.getForfaitIdentifier(forfait)}`, forfait, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IForfait>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IForfait[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getForfaitIdentifier(forfait: Pick<IForfait, 'id'>): string {
    return forfait.id;
  }

  compareForfait(o1: Pick<IForfait, 'id'> | null, o2: Pick<IForfait, 'id'> | null): boolean {
    return o1 && o2 ? this.getForfaitIdentifier(o1) === this.getForfaitIdentifier(o2) : o1 === o2;
  }

  addForfaitToCollectionIfMissing<Type extends Pick<IForfait, 'id'>>(
    forfaitCollection: Type[],
    ...forfaitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const forfaits: Type[] = forfaitsToCheck.filter(isPresent);
    if (forfaits.length > 0) {
      const forfaitCollectionIdentifiers = forfaitCollection.map(forfaitItem => this.getForfaitIdentifier(forfaitItem)!);
      const forfaitsToAdd = forfaits.filter(forfaitItem => {
        const forfaitIdentifier = this.getForfaitIdentifier(forfaitItem);
        if (forfaitCollectionIdentifiers.includes(forfaitIdentifier)) {
          return false;
        }
        forfaitCollectionIdentifiers.push(forfaitIdentifier);
        return true;
      });
      return [...forfaitsToAdd, ...forfaitCollection];
    }
    return forfaitCollection;
  }
}
