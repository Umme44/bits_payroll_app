import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOfficeNotices, NewOfficeNotices } from '../office-notices.model';

export type PartialUpdateOfficeNotices = Partial<IOfficeNotices> & Pick<IOfficeNotices, 'id'>;

type RestOf<T extends IOfficeNotices | NewOfficeNotices> = Omit<T, 'publishForm' | 'publishTo' | 'createdAt' | 'updatedAt'> & {
  publishForm?: string | null;
  publishTo?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestOfficeNotices = RestOf<IOfficeNotices>;

export type NewRestOfficeNotices = RestOf<NewOfficeNotices>;

export type PartialUpdateRestOfficeNotices = RestOf<PartialUpdateOfficeNotices>;

export type EntityResponseType = HttpResponse<IOfficeNotices>;
export type EntityArrayResponseType = HttpResponse<IOfficeNotices[]>;

@Injectable({ providedIn: 'root' })
export class OfficeNoticesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/office-notices');
  public userEndResourceUrl = SERVER_API_URL + 'api/common/archive-office-notices';


  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(officeNotices: NewOfficeNotices): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(officeNotices);
    return this.http
      .post<RestOfficeNotices>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(officeNotices: IOfficeNotices): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(officeNotices);
    return this.http
      .put<RestOfficeNotices>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(officeNotices: PartialUpdateOfficeNotices): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(officeNotices);
    return this.http
      .patch<RestOfficeNotices>(`${this.resourceUrl}/${this.getOfficeNoticesIdentifier(officeNotices)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOfficeNotices>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOfficeNotices[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryForUserEnd(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOfficeNotices[]>(this.userEndResourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOfficeNoticesIdentifier(officeNotices: Pick<IOfficeNotices, 'id'>): number {
    return officeNotices.id;
  }

  compareOfficeNotices(o1: Pick<IOfficeNotices, 'id'> | null, o2: Pick<IOfficeNotices, 'id'> | null): boolean {
    return o1 && o2 ? this.getOfficeNoticesIdentifier(o1) === this.getOfficeNoticesIdentifier(o2) : o1 === o2;
  }

  addOfficeNoticesToCollectionIfMissing<Type extends Pick<IOfficeNotices, 'id'>>(
    officeNoticesCollection: Type[],
    ...officeNoticesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const officeNotices: Type[] = officeNoticesToCheck.filter(isPresent);
    if (officeNotices.length > 0) {
      const officeNoticesCollectionIdentifiers = officeNoticesCollection.map(
        officeNoticesItem => this.getOfficeNoticesIdentifier(officeNoticesItem)!
      );
      const officeNoticesToAdd = officeNotices.filter(officeNoticesItem => {
        const officeNoticesIdentifier = this.getOfficeNoticesIdentifier(officeNoticesItem);
        if (officeNoticesCollectionIdentifiers.includes(officeNoticesIdentifier)) {
          return false;
        }
        officeNoticesCollectionIdentifiers.push(officeNoticesIdentifier);
        return true;
      });
      return [...officeNoticesToAdd, ...officeNoticesCollection];
    }
    return officeNoticesCollection;
  }

  protected convertDateFromClient<T extends IOfficeNotices | NewOfficeNotices | PartialUpdateOfficeNotices>(officeNotices: T): RestOf<T> {
    return {
      ...officeNotices,
      publishForm: officeNotices.publishForm?.format(DATE_FORMAT) ?? null,
      publishTo: officeNotices.publishTo?.format(DATE_FORMAT) ?? null,
      createdAt: officeNotices.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: officeNotices.updatedAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOfficeNotices: RestOfficeNotices): IOfficeNotices {
    return {
      ...restOfficeNotices,
      publishForm: restOfficeNotices.publishForm ? dayjs(restOfficeNotices.publishForm) : undefined,
      publishTo: restOfficeNotices.publishTo ? dayjs(restOfficeNotices.publishTo) : undefined,
      createdAt: restOfficeNotices.createdAt ? dayjs(restOfficeNotices.createdAt) : undefined,
      updatedAt: restOfficeNotices.updatedAt ? dayjs(restOfficeNotices.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOfficeNotices>): HttpResponse<IOfficeNotices> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOfficeNotices[]>): HttpResponse<IOfficeNotices[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
