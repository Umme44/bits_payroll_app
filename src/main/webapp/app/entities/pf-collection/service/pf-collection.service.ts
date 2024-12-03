import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfCollection, NewPfCollection } from '../pf-collection.model';

export type PartialUpdatePfCollection = Partial<IPfCollection> & Pick<IPfCollection, 'id'>;

type RestOf<T extends IPfCollection | NewPfCollection> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

export type RestPfCollection = RestOf<IPfCollection>;

export type NewRestPfCollection = RestOf<NewPfCollection>;

export type PartialUpdateRestPfCollection = RestOf<PartialUpdatePfCollection>;

export type EntityResponseType = HttpResponse<IPfCollection>;
export type EntityArrayResponseType = HttpResponse<IPfCollection[]>;

@Injectable({ providedIn: 'root' })
export class PfCollectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/pf-collections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfCollection: NewPfCollection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfCollection);
    return this.http
      .post<RestPfCollection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pfCollection: IPfCollection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfCollection);
    return this.http
      .put<RestPfCollection>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pfCollection: PartialUpdatePfCollection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfCollection);
    return this.http
      .patch<RestPfCollection>(`${this.resourceUrl}/${this.getPfCollectionIdentifier(pfCollection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPfCollection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPfCollection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfCollectionIdentifier(pfCollection: Pick<IPfCollection, 'id'>): number {
    return pfCollection.id;
  }

  comparePfCollection(o1: Pick<IPfCollection, 'id'> | null, o2: Pick<IPfCollection, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfCollectionIdentifier(o1) === this.getPfCollectionIdentifier(o2) : o1 === o2;
  }

  addPfCollectionToCollectionIfMissing<Type extends Pick<IPfCollection, 'id'>>(
    pfCollectionCollection: Type[],
    ...pfCollectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfCollections: Type[] = pfCollectionsToCheck.filter(isPresent);
    if (pfCollections.length > 0) {
      const pfCollectionCollectionIdentifiers = pfCollectionCollection.map(
        pfCollectionItem => this.getPfCollectionIdentifier(pfCollectionItem)!
      );
      const pfCollectionsToAdd = pfCollections.filter(pfCollectionItem => {
        const pfCollectionIdentifier = this.getPfCollectionIdentifier(pfCollectionItem);
        if (pfCollectionCollectionIdentifiers.includes(pfCollectionIdentifier)) {
          return false;
        }
        pfCollectionCollectionIdentifiers.push(pfCollectionIdentifier);
        return true;
      });
      return [...pfCollectionsToAdd, ...pfCollectionCollection];
    }
    return pfCollectionCollection;
  }

  protected convertDateFromClient<T extends IPfCollection | NewPfCollection | PartialUpdatePfCollection>(pfCollection: T): RestOf<T> {
    return {
      ...pfCollection,
      transactionDate: pfCollection.transactionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPfCollection: RestPfCollection): IPfCollection {
    return {
      ...restPfCollection,
      transactionDate: restPfCollection.transactionDate ? dayjs(restPfCollection.transactionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPfCollection>): HttpResponse<IPfCollection> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPfCollection[]>): HttpResponse<IPfCollection[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
