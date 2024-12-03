import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICalenderYear, NewCalenderYear } from '../calender-year.model';

export type PartialUpdateCalenderYear = Partial<ICalenderYear> & Pick<ICalenderYear, 'id'>;

type RestOf<T extends ICalenderYear | NewCalenderYear> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestCalenderYear = RestOf<ICalenderYear>;

export type NewRestCalenderYear = RestOf<NewCalenderYear>;

export type PartialUpdateRestCalenderYear = RestOf<PartialUpdateCalenderYear>;

export type EntityResponseType = HttpResponse<ICalenderYear>;
export type EntityArrayResponseType = HttpResponse<ICalenderYear[]>;

@Injectable({ providedIn: 'root' })
export class CalenderYearService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/calender-years');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(calenderYear: NewCalenderYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calenderYear);
    return this.http
      .post<RestCalenderYear>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(calenderYear: ICalenderYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calenderYear);
    return this.http
      .put<RestCalenderYear>(`${this.resourceUrl}/${this.getCalenderYearIdentifier(calenderYear)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(calenderYear: PartialUpdateCalenderYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calenderYear);
    return this.http
      .patch<RestCalenderYear>(`${this.resourceUrl}/${this.getCalenderYearIdentifier(calenderYear)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCalenderYear>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCalenderYear[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCalenderYearIdentifier(calenderYear: Pick<ICalenderYear, 'id'>): number {
    return calenderYear.id;
  }

  compareCalenderYear(o1: Pick<ICalenderYear, 'id'> | null, o2: Pick<ICalenderYear, 'id'> | null): boolean {
    return o1 && o2 ? this.getCalenderYearIdentifier(o1) === this.getCalenderYearIdentifier(o2) : o1 === o2;
  }

  addCalenderYearToCollectionIfMissing<Type extends Pick<ICalenderYear, 'id'>>(
    calenderYearCollection: Type[],
    ...calenderYearsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const calenderYears: Type[] = calenderYearsToCheck.filter(isPresent);
    if (calenderYears.length > 0) {
      const calenderYearCollectionIdentifiers = calenderYearCollection.map(
        calenderYearItem => this.getCalenderYearIdentifier(calenderYearItem)!
      );
      const calenderYearsToAdd = calenderYears.filter(calenderYearItem => {
        const calenderYearIdentifier = this.getCalenderYearIdentifier(calenderYearItem);
        if (calenderYearCollectionIdentifiers.includes(calenderYearIdentifier)) {
          return false;
        }
        calenderYearCollectionIdentifiers.push(calenderYearIdentifier);
        return true;
      });
      return [...calenderYearsToAdd, ...calenderYearCollection];
    }
    return calenderYearCollection;
  }

  protected convertDateFromClient<T extends ICalenderYear | NewCalenderYear | PartialUpdateCalenderYear>(calenderYear: T): RestOf<T> {
    return {
      ...calenderYear,
      startDate: calenderYear.startDate?.format(DATE_FORMAT) ?? null,
      endDate: calenderYear.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCalenderYear: RestCalenderYear): ICalenderYear {
    return {
      ...restCalenderYear,
      startDate: restCalenderYear.startDate ? dayjs(restCalenderYear.startDate) : undefined,
      endDate: restCalenderYear.endDate ? dayjs(restCalenderYear.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCalenderYear>): HttpResponse<ICalenderYear> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCalenderYear[]>): HttpResponse<ICalenderYear[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
