import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResultat, NewResultat } from '../resultat.model';

export type PartialUpdateResultat = Partial<IResultat> & Pick<IResultat, 'id'>;

export type EntityResponseType = HttpResponse<IResultat>;
export type EntityArrayResponseType = HttpResponse<IResultat[]>;

@Injectable({ providedIn: 'root' })
export class ResultatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resultats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resultat: NewResultat): Observable<EntityResponseType> {
    return this.http.post<IResultat>(this.resourceUrl, resultat, { observe: 'response' });
  }

  update(resultat: IResultat): Observable<EntityResponseType> {
    return this.http.put<IResultat>(`${this.resourceUrl}/${this.getResultatIdentifier(resultat)}`, resultat, { observe: 'response' });
  }

  partialUpdate(resultat: PartialUpdateResultat): Observable<EntityResponseType> {
    return this.http.patch<IResultat>(`${this.resourceUrl}/${this.getResultatIdentifier(resultat)}`, resultat, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IResultat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResultatIdentifier(resultat: Pick<IResultat, 'id'>): string {
    return resultat.id;
  }

  compareResultat(o1: Pick<IResultat, 'id'> | null, o2: Pick<IResultat, 'id'> | null): boolean {
    return o1 && o2 ? this.getResultatIdentifier(o1) === this.getResultatIdentifier(o2) : o1 === o2;
  }

  addResultatToCollectionIfMissing<Type extends Pick<IResultat, 'id'>>(
    resultatCollection: Type[],
    ...resultatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resultats: Type[] = resultatsToCheck.filter(isPresent);
    if (resultats.length > 0) {
      const resultatCollectionIdentifiers = resultatCollection.map(resultatItem => this.getResultatIdentifier(resultatItem)!);
      const resultatsToAdd = resultats.filter(resultatItem => {
        const resultatIdentifier = this.getResultatIdentifier(resultatItem);
        if (resultatCollectionIdentifiers.includes(resultatIdentifier)) {
          return false;
        }
        resultatCollectionIdentifiers.push(resultatIdentifier);
        return true;
      });
      return [...resultatsToAdd, ...resultatCollection];
    }
    return resultatCollection;
  }
}
