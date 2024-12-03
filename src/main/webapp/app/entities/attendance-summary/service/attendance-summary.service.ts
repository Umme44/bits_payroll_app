import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttendanceSummary, NewAttendanceSummary } from '../attendance-summary.model';

export type PartialUpdateAttendanceSummary = Partial<IAttendanceSummary> & Pick<IAttendanceSummary, 'id'>;

type RestOf<T extends IAttendanceSummary | NewAttendanceSummary> = Omit<
  T,
  'attendanceRegularisationStartDate' | 'attendanceRegularisationEndDate'
> & {
  attendanceRegularisationStartDate?: string | null;
  attendanceRegularisationEndDate?: string | null;
};

export type RestAttendanceSummary = RestOf<IAttendanceSummary>;

export type NewRestAttendanceSummary = RestOf<NewAttendanceSummary>;

export type PartialUpdateRestAttendanceSummary = RestOf<PartialUpdateAttendanceSummary>;

export type EntityResponseType = HttpResponse<IAttendanceSummary>;
export type EntityArrayResponseType = HttpResponse<IAttendanceSummary[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceSummaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/attendance-summaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendanceSummary: NewAttendanceSummary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceSummary);
    return this.http
      .post<RestAttendanceSummary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(attendanceSummary: IAttendanceSummary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceSummary);
    return this.http
      .put<RestAttendanceSummary>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(attendanceSummary: PartialUpdateAttendanceSummary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceSummary);
    return this.http
      .patch<RestAttendanceSummary>(`${this.resourceUrl}/${this.getAttendanceSummaryIdentifier(attendanceSummary)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAttendanceSummary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAttendanceSummary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttendanceSummary[]>(`${this.resourceUrl}/${year}/${month}`, {
      params: options,
      observe: 'response',
    });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttendanceSummaryIdentifier(attendanceSummary: Pick<IAttendanceSummary, 'id'>): number {
    return attendanceSummary.id;
  }

  compareAttendanceSummary(o1: Pick<IAttendanceSummary, 'id'> | null, o2: Pick<IAttendanceSummary, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttendanceSummaryIdentifier(o1) === this.getAttendanceSummaryIdentifier(o2) : o1 === o2;
  }

  addAttendanceSummaryToCollectionIfMissing<Type extends Pick<IAttendanceSummary, 'id'>>(
    attendanceSummaryCollection: Type[],
    ...attendanceSummariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attendanceSummaries: Type[] = attendanceSummariesToCheck.filter(isPresent);
    if (attendanceSummaries.length > 0) {
      const attendanceSummaryCollectionIdentifiers = attendanceSummaryCollection.map(
        attendanceSummaryItem => this.getAttendanceSummaryIdentifier(attendanceSummaryItem)!
      );
      const attendanceSummariesToAdd = attendanceSummaries.filter(attendanceSummaryItem => {
        const attendanceSummaryIdentifier = this.getAttendanceSummaryIdentifier(attendanceSummaryItem);
        if (attendanceSummaryCollectionIdentifiers.includes(attendanceSummaryIdentifier)) {
          return false;
        }
        attendanceSummaryCollectionIdentifiers.push(attendanceSummaryIdentifier);
        return true;
      });
      return [...attendanceSummariesToAdd, ...attendanceSummaryCollection];
    }
    return attendanceSummaryCollection;
  }

  protected convertDateFromClient<T extends IAttendanceSummary | NewAttendanceSummary | PartialUpdateAttendanceSummary>(
    attendanceSummary: T
  ): RestOf<T> {
    return {
      ...attendanceSummary,
      attendanceRegularisationStartDate: attendanceSummary.attendanceRegularisationStartDate?.format(DATE_FORMAT) ?? null,
      attendanceRegularisationEndDate: attendanceSummary.attendanceRegularisationEndDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAttendanceSummary: RestAttendanceSummary): IAttendanceSummary {
    return {
      ...restAttendanceSummary,
      attendanceRegularisationStartDate: restAttendanceSummary.attendanceRegularisationStartDate
        ? dayjs(restAttendanceSummary.attendanceRegularisationStartDate)
        : undefined,
      attendanceRegularisationEndDate: restAttendanceSummary.attendanceRegularisationEndDate
        ? dayjs(restAttendanceSummary.attendanceRegularisationEndDate)
        : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAttendanceSummary>): HttpResponse<IAttendanceSummary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAttendanceSummary[]>): HttpResponse<IAttendanceSummary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
