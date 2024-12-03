import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOffer, NewOffer } from '../offer.model';

export type PartialUpdateOffer = Partial<IOffer> & Pick<IOffer, 'id'>;

type RestOf<T extends IOffer | NewOffer> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestOffer = RestOf<IOffer>;

export type NewRestOffer = RestOf<NewOffer>;

export type PartialUpdateRestOffer = RestOf<PartialUpdateOffer>;

export type EntityResponseType = HttpResponse<IOffer>;
export type EntityArrayResponseType = HttpResponse<IOffer[]>;

@Injectable({ providedIn: 'root' })
export class OfferService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/offers');
  public resourceUrlCommon = this.applicationConfigService.getEndpointFor('api/common/offers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  // create(offer: NewOffer): Observable<EntityResponseType> {
  //   const copy = this.convertDateFromClient(offer);
  //   return this.http
  //   .post<RestOffer>(this.resourceUrl, copy, { observe: 'response' })
  //   .pipe(map(res => this.convertResponseFromServer(res)));
  // }

  create(file: File, offer: IOffer): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(offer);
    formData.append('offer', new Blob([JSON.stringify(copy)], { type: 'application/json' }));

    return this.http
      .post<RestOffer>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map(res=> this.convertResponseFromServer(res)));
  }

  update(offer: IOffer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .put<RestOffer>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  updateWithMultipartData(file: File, offer: IOffer): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(offer);
    formData.append('offer', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<RestOffer>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(offer: PartialUpdateOffer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .patch<RestOffer>(`${this.resourceUrl}/${this.getOfferIdentifier(offer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOffer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOffer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOfferIdentifier(offer: Pick<IOffer, 'id'>): number {
    return offer.id;
  }

  compareOffer(o1: Pick<IOffer, 'id'> | null, o2: Pick<IOffer, 'id'> | null): boolean {
    return o1 && o2 ? this.getOfferIdentifier(o1) === this.getOfferIdentifier(o2) : o1 === o2;
  }

  addOfferToCollectionIfMissing<Type extends Pick<IOffer, 'id'>>(
    offerCollection: Type[],
    ...offersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const offers: Type[] = offersToCheck.filter(isPresent);
    if (offers.length > 0) {
      const offerCollectionIdentifiers = offerCollection.map(offerItem => this.getOfferIdentifier(offerItem)!);
      const offersToAdd = offers.filter(offerItem => {
        const offerIdentifier = this.getOfferIdentifier(offerItem);
        if (offerCollectionIdentifiers.includes(offerIdentifier)) {
          return false;
        }
        offerCollectionIdentifiers.push(offerIdentifier);
        return true;
      });
      return [...offersToAdd, ...offerCollection];
    }
    return offerCollection;
  }

  protected convertDateFromClient<T extends IOffer | NewOffer | PartialUpdateOffer>(offer: T): RestOf<T> {
    return {
      ...offer,
      createdAt: offer.createdAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOffer: RestOffer): IOffer {
    return {
      ...restOffer,
      createdAt: restOffer.createdAt ? dayjs(restOffer.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOffer>): HttpResponse<IOffer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOffer[]>): HttpResponse<IOffer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  recentList(): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestOffer[]>(this.resourceUrlCommon + '/recent-list', { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  archiveList(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOffer[]>(this.resourceUrlCommon + '/archive-list', { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
}
