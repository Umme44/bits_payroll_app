import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmploymentHistory, NewEmploymentHistory } from '../employment-history.model';

export type PartialUpdateEmploymentHistory = Partial<IEmploymentHistory> & Pick<IEmploymentHistory, 'id'>;

type RestOf<T extends IEmploymentHistory | NewEmploymentHistory> = Omit<T, 'effectiveDate'> & {
  effectiveDate?: string | null;
};

export type RestEmploymentHistory = RestOf<IEmploymentHistory>;

export type NewRestEmploymentHistory = RestOf<NewEmploymentHistory>;

export type PartialUpdateRestEmploymentHistory = RestOf<PartialUpdateEmploymentHistory>;

export type EntityResponseType = HttpResponse<IEmploymentHistory>;
export type EntityArrayResponseType = HttpResponse<IEmploymentHistory[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employment-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employmentHistory: NewEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .post<RestEmploymentHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .put<RestEmploymentHistory>(`${this.resourceUrl}/${this.getEmploymentHistoryIdentifier(employmentHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employmentHistory: PartialUpdateEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .patch<RestEmploymentHistory>(`${this.resourceUrl}/${this.getEmploymentHistoryIdentifier(employmentHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmploymentHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmploymentHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmploymentHistoryIdentifier(employmentHistory: Pick<IEmploymentHistory, 'id'>): number {
    return employmentHistory.id;
  }

  compareEmploymentHistory(o1: Pick<IEmploymentHistory, 'id'> | null, o2: Pick<IEmploymentHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmploymentHistoryIdentifier(o1) === this.getEmploymentHistoryIdentifier(o2) : o1 === o2;
  }

  addEmploymentHistoryToCollectionIfMissing<Type extends Pick<IEmploymentHistory, 'id'>>(
    employmentHistoryCollection: Type[],
    ...employmentHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employmentHistories: Type[] = employmentHistoriesToCheck.filter(isPresent);
    if (employmentHistories.length > 0) {
      const employmentHistoryCollectionIdentifiers = employmentHistoryCollection.map(
        employmentHistoryItem => this.getEmploymentHistoryIdentifier(employmentHistoryItem)!
      );
      const employmentHistoriesToAdd = employmentHistories.filter(employmentHistoryItem => {
        const employmentHistoryIdentifier = this.getEmploymentHistoryIdentifier(employmentHistoryItem);
        if (employmentHistoryCollectionIdentifiers.includes(employmentHistoryIdentifier)) {
          return false;
        }
        employmentHistoryCollectionIdentifiers.push(employmentHistoryIdentifier);
        return true;
      });
      return [...employmentHistoriesToAdd, ...employmentHistoryCollection];
    }
    return employmentHistoryCollection;
  }

  protected convertDateFromClient<T extends IEmploymentHistory | NewEmploymentHistory | PartialUpdateEmploymentHistory>(
    employmentHistory: T
  ): RestOf<T> {
    return {
      ...employmentHistory,
      effectiveDate: employmentHistory.effectiveDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEmploymentHistory: RestEmploymentHistory): IEmploymentHistory {
    return {
      ...restEmploymentHistory,
      effectiveDate: restEmploymentHistory.effectiveDate ? dayjs(restEmploymentHistory.effectiveDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmploymentHistory>): HttpResponse<IEmploymentHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmploymentHistory[]>): HttpResponse<IEmploymentHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
