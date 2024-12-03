import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHolidays, NewHolidays } from '../holidays.model';

export type PartialUpdateHolidays = Partial<IHolidays> & Pick<IHolidays, 'id'>;

type RestOf<T extends IHolidays | NewHolidays> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestHolidays = RestOf<IHolidays>;

export type NewRestHolidays = RestOf<NewHolidays>;

export type PartialUpdateRestHolidays = RestOf<PartialUpdateHolidays>;

export type EntityResponseType = HttpResponse<IHolidays>;
export type EntityArrayResponseType = HttpResponse<IHolidays[]>;

@Injectable({ providedIn: 'root' })
export class HolidaysService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/holidays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holidays: NewHolidays): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holidays);
    return this.http
      .post<RestHolidays>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(holidays: IHolidays): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holidays);
    return this.http
      .put<RestHolidays>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(holidays: PartialUpdateHolidays): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holidays);
    return this.http
      .patch<RestHolidays>(`${this.resourceUrl}/${this.getHolidaysIdentifier(holidays)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHolidays>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHolidays[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHolidaysIdentifier(holidays: Pick<IHolidays, 'id'>): number {
    return holidays.id;
  }

  compareHolidays(o1: Pick<IHolidays, 'id'> | null, o2: Pick<IHolidays, 'id'> | null): boolean {
    return o1 && o2 ? this.getHolidaysIdentifier(o1) === this.getHolidaysIdentifier(o2) : o1 === o2;
  }

  addHolidaysToCollectionIfMissing<Type extends Pick<IHolidays, 'id'>>(
    holidaysCollection: Type[],
    ...holidaysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const holidays: Type[] = holidaysToCheck.filter(isPresent);
    if (holidays.length > 0) {
      const holidaysCollectionIdentifiers = holidaysCollection.map(holidaysItem => this.getHolidaysIdentifier(holidaysItem)!);
      const holidaysToAdd = holidays.filter(holidaysItem => {
        const holidaysIdentifier = this.getHolidaysIdentifier(holidaysItem);
        if (holidaysCollectionIdentifiers.includes(holidaysIdentifier)) {
          return false;
        }
        holidaysCollectionIdentifiers.push(holidaysIdentifier);
        return true;
      });
      return [...holidaysToAdd, ...holidaysCollection];
    }
    return holidaysCollection;
  }

  protected convertDateFromClient<T extends IHolidays | NewHolidays | PartialUpdateHolidays>(holidays: T): RestOf<T> {
    return {
      ...holidays,
      startDate: holidays.startDate?.format(DATE_FORMAT) ?? null,
      endDate: holidays.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHolidays: RestHolidays): IHolidays {
    return {
      ...restHolidays,
      startDate: restHolidays.startDate ? dayjs(restHolidays.startDate) : undefined,
      endDate: restHolidays.endDate ? dayjs(restHolidays.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHolidays>): HttpResponse<IHolidays> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHolidays[]>): HttpResponse<IHolidays[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  queryByYear(year: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IHolidays[]>(`${this.resourceUrl}/get-by-year/${year}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((holidays: IHolidays) => {
        holidays.startDate = holidays.startDate ? dayjs(holidays.startDate) : undefined;
        holidays.endDate = holidays.endDate ? dayjs(holidays.endDate) : undefined;
      });
    }
    return res;
  }
}
