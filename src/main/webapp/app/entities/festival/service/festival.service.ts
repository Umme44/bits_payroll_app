import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFestival, NewFestival } from '../festival.model';
import { Filter } from '../../../common/employee-address-book/filter.model';

export type PartialUpdateFestival = Partial<IFestival> & Pick<IFestival, 'id'>;

type RestOf<T extends IFestival | NewFestival> = Omit<T, 'festivalDate' | 'bonusDisbursementDate'> & {
  festivalDate?: string | null;
  bonusDisbursementDate?: string | null;
};

export type RestFestival = RestOf<IFestival>;

export type NewRestFestival = RestOf<NewFestival>;

export type PartialUpdateRestFestival = RestOf<PartialUpdateFestival>;

export type EntityResponseType = HttpResponse<IFestival>;
export type EntityArrayResponseType = HttpResponse<IFestival[]>;

@Injectable({ providedIn: 'root' })
export class FestivalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/festivals/list');

  protected resourceUrl1 = this.applicationConfigService.getEndpointFor('api/payroll-mgt/festivals');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(festival: NewFestival): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(festival);
    return this.http
      .post<RestFestival>(this.resourceUrl1, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(festival: IFestival): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(festival);
    return this.http
      .put<RestFestival>(this.resourceUrl1, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(festival: PartialUpdateFestival): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(festival);
    return this.http
      .patch<RestFestival>(`${this.resourceUrl}/${this.getFestivalIdentifier(festival)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFestival>(`${this.resourceUrl1}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFestival[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  queryWithFilter(filterDTO: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IFestival[]>(this.resourceUrl + '-search', filterDTO, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl1}/${id}`, { observe: 'response' });
  }

  getFestivalIdentifier(festival: Pick<IFestival, 'id'>): number {
    return festival.id;
  }

  compareFestival(o1: Pick<IFestival, 'id'> | null, o2: Pick<IFestival, 'id'> | null): boolean {
    return o1 && o2 ? this.getFestivalIdentifier(o1) === this.getFestivalIdentifier(o2) : o1 === o2;
  }

  addFestivalToCollectionIfMissing<Type extends Pick<IFestival, 'id'>>(
    festivalCollection: Type[],
    ...festivalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const festivals: Type[] = festivalsToCheck.filter(isPresent);
    if (festivals.length > 0) {
      const festivalCollectionIdentifiers = festivalCollection.map(festivalItem => this.getFestivalIdentifier(festivalItem)!);
      const festivalsToAdd = festivals.filter(festivalItem => {
        const festivalIdentifier = this.getFestivalIdentifier(festivalItem);
        if (festivalCollectionIdentifiers.includes(festivalIdentifier)) {
          return false;
        }
        festivalCollectionIdentifiers.push(festivalIdentifier);
        return true;
      });
      return [...festivalsToAdd, ...festivalCollection];
    }
    return festivalCollection;
  }

  protected convertDateFromClient<T extends IFestival | NewFestival | PartialUpdateFestival>(festival: T): RestOf<T> {
    return {
      ...festival,
      festivalDate: festival.festivalDate?.format(DATE_FORMAT) ?? null,
      bonusDisbursementDate: festival.bonusDisbursementDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFestival[]>): HttpResponse<IFestival[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertDateFromServer(restFestival: RestFestival): IFestival {
    return {
      ...restFestival,
      festivalDate: restFestival.festivalDate ? dayjs(restFestival.festivalDate) : undefined,
      bonusDisbursementDate: restFestival.bonusDisbursementDate ? dayjs(restFestival.bonusDisbursementDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFestival>): HttpResponse<IFestival> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  downloadReport(type: string, festivalId: number): Observable<HttpResponse<Blob>> {
    const urlForSummary = SERVER_API_URL + '/api/payroll-mgt/fb-gen-summary-export/' + festivalId;
    const urlForRCE = SERVER_API_URL + '/api/payroll-mgt/fb-gen-rce-export/' + festivalId;
    const urlForCE = SERVER_API_URL + '/api/payroll-mgt/fb-gen-ce-export/' + festivalId;
    const urlForProRata = SERVER_API_URL + '/api/payroll-mgt/fb-gen-pro-rata-export/' + festivalId;
    switch (type) {
      case 'summary':
        return this.http.get(urlForSummary, { responseType: 'blob', observe: 'response' });
        break;
      case 'rce':
        return this.http.get(urlForRCE, { responseType: 'blob', observe: 'response' });
        break;
      case 'ce':
        return this.http.get(urlForCE, { responseType: 'blob', observe: 'response' });
      case 'pro-rata':
        return this.http.get(urlForProRata, { responseType: 'blob', observe: 'response' });
        break;
      default:
        return this.http.get(urlForSummary, { responseType: 'blob', observe: 'response' });
        break;
    }
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((festival: IFestival) => {
        festival.festivalDate = festival.festivalDate ? dayjs(festival.festivalDate) : undefined;
        festival.bonusDisbursementDate = festival.bonusDisbursementDate ? dayjs(festival.bonusDisbursementDate) : undefined;
      });
    }
    return res;
  }
}
